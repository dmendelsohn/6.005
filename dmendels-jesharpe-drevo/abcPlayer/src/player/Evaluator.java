package player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import sound.PlayableNote;
import sound.SequencePlayer;
import ast.BarlineElement;
import ast.Element;
import ast.Header;
import ast.HighElement;
import ast.Measure;
import ast.NoteElement;
import ast.NthRepeatElement;
import ast.Song;
import ast.SyncElement;
import ast.TupletElement;
import ast.Voice;

/**
 * Evaluator
 * 
 * Takes the parse AST and converts it to a SequencePlayer with MIDI notes 
 * ready to be played.
 * 
 * >>> Note:
 * - We allow incomplete measures to be played, just like in Fur Elise
 *
 */
public class Evaluator {
	private SequencePlayer player;
	private Song song;
	private Header h;
	private List<Voice> voices;
	
	private Fraction defaultNoteLength;
	private Fraction meter;
	private int tempo;
	private Key key;
	
	private Map<String,Integer> keyMap;
	
	private int numTicksPerQuarter;
	private int numTicksPerMeasure;

	/**
	 * Dummy testing harness for glassbox testing
	 * 
	 * @param i - so that constructor is overloaded
	 */
	public Evaluator(int i) {
		// Do nothing here!
	}
	
	/**
	 * Testing harness. Used for testing individual methods. i denotes the particualr test
	 * and s is the Song object to evaluate. 
	 * 
	 * @param i
	 * @param s
	 * @return
	 */
	public List<Measure> EvalTest(int i, Song s) /*throws MidiUnavailableException, InvalidMidiDataException*/ {
		switch(i){
		case 1 :
			return (ArrayList<Measure>) expandRepeats(s.getVoices().get(0));
			//break;
		case 2 :
			expandTuplets(s.getVoices().get(0));
			
			break;
		}
		
		
		return null;
	}
	
	/**
	 * Processes header and body, result is a SequencePlayer object, ready to play music
	 * 
	 * @param song
	 * @throws MidiUnavailableException
	 * @throws InvalidMidiDataException
	 */
	public Evaluator(Song song) throws MidiUnavailableException, InvalidMidiDataException {
		this.song = song;
		this.h = song.getHeader();
		this.voices = song.getVoices();
		evalHeader();
		List<PlayableNote> notes = evalBody();
		
		int tempoForPlayer = defaultNoteLength.multiply(new Fraction(tempo*4,1)).toInt();
		
		player = new SequencePlayer(tempoForPlayer, numTicksPerQuarter);
		for (PlayableNote pn : notes) {
			player.addNote(pn.getPitch().toMidiNote(), pn.getStartTick(), pn.getNumTicks());
		}
		
		player.play();
	}
	
	/**
	 * Evaluates the header, instantiates time-oriented settings
	 * for tempo and note lengths.
	 */
	private void evalHeader() {
		tempo = Integer.parseInt(h.tempo);
		defaultNoteLength = new Fraction(h.length);
		if (h.meter.equals("C"))
			meter = new Fraction(4, 4);
		else if (h.meter.equals("C|"))
			meter = new Fraction(2,2);
		else
			meter = new Fraction(h.meter);
		key = new Key(h.key);
		keyMap = key.getKeyMap();
	}
	
	/**
	 * Evaluates body, one Voice at a time. 
	 * 
	 * @precondition: evalHeader() has already been called
	 */
	private List<PlayableNote> evalBody() {
		for (Voice v: voices) {
			expandTuplets(v);
		}
		numTicksPerQuarter = determineNumTicksPerQuarter();
		numTicksPerMeasure = determineNumTicksPerMeasure();
		List<PlayableNote> notes = new ArrayList<PlayableNote>();
		for (Voice v: voices) {
			List<PlayableNote> voiceNotes = evalVoice(v);
			notes.addAll(voiceNotes);
		}
		return notes;
	}
	
	/**
	 * Evaluation of a single voice in a Song
	 * 
	 * @param voice
	 * @return list of PlayableNotes
	 */
	private List<PlayableNote> evalVoice(Voice voice) {
		List<Measure> measures = expandRepeats(voice);
		List<PlayableNote> notes = new ArrayList<PlayableNote>();
		int currentTick = 0;
		for (int i = 0; i < measures.size(); i++) {
			ListIntegerPair lip;
			if (i == 0 || i == measures.size() - 1)
				lip = evalMeasure(measures.get(i), currentTick, false); //needn't obey meter
			else
				lip = evalMeasure(measures.get(i), currentTick, true); //must obey meter
			notes.addAll(lip.getList());
			int ticksInMeasure = lip.getNumTicks();
			currentTick += ticksInMeasure;
		}
		return notes;
	}
	
	/**
	 * Break up tuplets into individual SyncElement notes
	 * 
	 * @param voice
	 */
	private void expandTuplets(Voice voice) {
		List<HighElement> highElts = voice.getElements();
		for (int i = 0; i < highElts.size(); i++) {
			HighElement h = highElts.get(i);
			if (h instanceof Measure)
				highElts.set(i, replaceTupletInMeasure((Measure) h));
		}
	}
	
	/**
	 * Helper for expandTuplets(), replaces tuplet in measure with elements
	 * 
	 * @param measure
	 * @return new Measure without tuplets
	 */
	private Measure replaceTupletInMeasure(Measure measure) {
		List<Element> measureElements = measure.getElements();
		List<Element> newMeasureElements = new ArrayList<Element>();
		for (Element elt : measureElements) {
			if (elt instanceof TupletElement) {
				TupletElement te = (TupletElement)elt;
				int tupletSize = ((TupletElement) elt).getDegree();
				Fraction noteScale;
				switch (tupletSize) {
				case 2:
					noteScale = new Fraction(3,2);
					break;
				case 3:
					noteScale = new Fraction(2,3);
					break;
				case 4:
					noteScale = new Fraction(3,4);
					break;
				default:
					throw new RuntimeException("Tuplet can only have 2, 3, or 4 elements");
				}
				for (SyncElement se : te.getNoteElements()) {
					se.scaleDuration(noteScale);
					newMeasureElements.add(se);
				}
			} else {
				newMeasureElements.add(elt);
			}
		}
		return new Measure(newMeasureElements);
	}
	
	/**
	 * Ensures our rep invariants are not broken with respect to ordering of elements in our parsed Measures
	 * and AST. 
	 * 
	 * Checks:
	 * 1) all bars are followed by a measure or nth repeat (or nothing)
	 * 2) all nth repeats are followed by a measure
	 * 3) all measures are followed by a bar (or nothing)
	 * 
	 * @return if ordering is valid ? true : false
	 */
	private boolean isValidOrder(List<HighElement> elements) {
		if (elements.size() == 0)
			return true;
		else {
			HighElement previous = elements.get(0);
			HighElement current;
			for (int i = 1; i < elements.size(); i++) {
				current = elements.get(i);
				if (previous instanceof BarlineElement) {
					if (current instanceof BarlineElement && !((BarlineElement) current).getBarString().equals("|:")) {
						return false;
					}
				} else if (previous instanceof NthRepeatElement) {
					if (!(current instanceof Measure)) {
						return false;
					}
				} else if (previous instanceof Measure) {
					if (!(current instanceof BarlineElement)) {
						return false;
					}
				} else {
					throw new RuntimeException("Error validating measures, bars, and nth repeats");
				}
				previous = current;
			}
			//if we didn't get a failure, then it is a valid order
			return true;
		}
	}
	
	/**
	 * Flatten list of Measures by expanding repeated sections, also removes barlines and repeats.
	 * 
	 * @param voice
	 * @return List<Measure>
	 */
	private List<Measure> expandRepeats(Voice voice) {
		List<HighElement> elements = voice.getElements();
		
		//verify the sequence of types in elements is possible
		if (!isValidOrder(elements))
			throw new RuntimeException("Invalid order of HighElements");
		
		//remove opening bar element, unless it's an invalid kind of bar, in which case throw exception
		if (elements.size() > 0 && elements.get(0) instanceof BarlineElement) {
			String barString = ((BarlineElement)elements.get(0)).getBarString();
			if (!barString.equals("|:") && !barString.equals("|")) { //opening bar is invalid
				throw new RuntimeException("Opening bar symbol is invalid");
			}
		}
		
		int i = 0;
		HighElement h;
		boolean seekingSecondNthRepeat = false;
		boolean repeatModePossible = true;
		List<Measure> measures = new ArrayList<Measure>();
		List<Measure> buffer = new ArrayList<Measure>();
		while (i < elements.size()) {
			h = elements.get(i);
			if (h instanceof NthRepeatElement) {
				NthRepeatElement n = (NthRepeatElement)h;
				if (n.getIndexAsInt() == 1) {
					if (!repeatModePossible || seekingSecondNthRepeat) //shouldn't get [1
						throw new RuntimeException("Got 1st nth repeat at invalid time");
					else
						seekingSecondNthRepeat = true;
				} else { //we have the [2 case
					//2nd repeats are handled inside the barline case
					throw new RuntimeException("Got second nth repeat at invalid time");
				}
			} else if (h instanceof BarlineElement) {
				BarlineElement b = (BarlineElement) h;
				String bs = b.getBarString();
				if (bs.equals("||") || bs.equals("|]") || bs.equals("|:")) {
					repeatModePossible = true;
					buffer.clear();
				} else if (bs.equals(":|")) {
					if (!repeatModePossible)
						throw new RuntimeException("Got end repeat at invalid time");
					if (seekingSecondNthRepeat) { //we must ensure next element is a "[2"
						i++;
						if (i < elements.size()) {
							HighElement next = elements.get(i);
							if (!(next instanceof NthRepeatElement)) {
								throw new RuntimeException("Missing expected second ending of repeat");
							} else {
								NthRepeatElement n = (NthRepeatElement)next;
								if (n.getIndexAsInt() == 1)
									throw new RuntimeException("Missing expected second ending of repeat");
							}
						} else {
							throw new RuntimeException("Piece ended while expecting second ending of repeat");
						}
					}
					measures.addAll(buffer);
					buffer.clear();
					repeatModePossible = false;
					seekingSecondNthRepeat = false;
				} else { // we got a "|", and we don't have to do anything
					//do nothing
				}
			} else if (h instanceof Measure) {
				measures.add((Measure) h);
				if (repeatModePossible && !seekingSecondNthRepeat)
					buffer.add((Measure) h);
			}
			i++; //increment iterating variable at end of while block
		}
		//if we're still seeking a second nth repeat after reading all the elements
		if (seekingSecondNthRepeat)
			throw new RuntimeException("Missing expected second ending of repeat");
		measures.addAll(buffer);
		return measures;
	}
	
	/**
	 * Evalutes a single Measure. 
	 * 
	 * @param measure
	 * @param startTick
	 * @param mustObeyMeter
	 * @return list of ListIntegerPair struct pairs, 
	 * 			int is numTicks in measure
	 * 			List is list of elements
	 */
	private ListIntegerPair evalMeasure(Measure measure, int startTick, boolean mustObeyMeter) {
		int currentTick = startTick;
		Map<String, Integer> measureKeyMap = new HashMap<String, Integer>(keyMap);
		List<PlayableNote> result = new ArrayList<PlayableNote>();
		
		for (Element e : measure.getElements()) {
			SyncElement se = (SyncElement)e; //we can do this because we've expanded tuplets out
			List<NoteElement> notes = se.expand();
			for (NoteElement note : notes) {
				//note that the Pitch constructor updates measureKeyMap if need be
				PlayableNote pn = new PlayableNote(note, 
						currentTick, 
						defaultNoteLength,
						numTicksPerQuarter,
						measureKeyMap);
				result.add(pn);			
			}
			//update currentTick
			Fraction duration = se.getDuration();
			currentTick += convertMultiplierToNumTicks(duration);			
		}
		
		//check to make sure number of beats in this measure matches the meter
		int totalTicks = currentTick - startTick;
		if (mustObeyMeter && (totalTicks != numTicksPerMeasure)) {
			//throw new RuntimeException("The number of beats per measure must match the meter");
			//commented out because sample files failed this.  For example, fur_elise.abc has middle measures
			//that disobey the meter
		}
			
		return new ListIntegerPair(result, totalTicks);
	}
	
	/**
	 * Returns GCD of integers p and q
	 * 
	 * @param p
	 * @param q
	 * @return gcd(p,q)
	 */
	public static int gcd(int p, int q) {
		if (q == 0) {
			return p;
		}
		return gcd(q, p % q);
	}
	
	/**
	 * Returns LCM of two integers a, b.
	 * 
	 * Depends on GCD method.
	 * 
	 * @param a
	 * @param b
	 * @return lcm(a, b)
	 */
	public static int lcm(int a, int b) {
	    return a * (b / gcd(a, b));
	}

	/**
	 * Determines number of ticks per quarter note for purposes of timing and sequencing notes.
	 * 
	 * @precondition: all voices have had their tuplets expanded
	 */
	private int determineNumTicksPerQuarter() {
		// for note
		// ArrayList<Integer> qBeatBuffer = new ArrayList<Integer>();
		//int currentQBeat;
		int LCM = 1;
		List<Integer> denominators = new ArrayList<Integer>();
		
		for (Voice voice : song.getVoices()){
			for (HighElement highElt : voice.getElements()){
				if (highElt instanceof Measure){
					for (Element elem : ((Measure) highElt).getElements()){
						if (elem instanceof SyncElement){
							denominators.add(((SyncElement)elem).getLcmOfDenoms());
						}
						
					}
				}
			}
		}
		
		for (int x : denominators) {
			LCM = lcm(LCM, x);
		}
		
		//LCM is the number of ticks per DefaultNoteLength
		Fraction fracDefaultLengthPerQuarter = defaultNoteLength.multiply(new Fraction(4, 1));
		return LCM*fracDefaultLengthPerQuarter.getDenom();
	}
	
	/**
	 * Helper method used in timing of notes
	 * 
	 * @precondition: defaultNoteLength and numTicksPerQuarter are filled correctly
	 * @param frac
	 */
	public int convertMultiplierToNumTicks(Fraction frac) {
		Fraction fracDurationInQuarterBeats = frac.multiply(defaultNoteLength).multiply(new Fraction(4, 1));
		Fraction fracDurationInTicks = fracDurationInQuarterBeats.multiply(new Fraction(numTicksPerQuarter, 1));
		int durationInTicks = fracDurationInTicks.toInt();
		if (durationInTicks == -1)
			throw new RuntimeException("Incorrectly calculated number of ticks");
		return durationInTicks;
	}
	
	/**
	 * Helper method used in timing of notes
	 * 
	 * @precondition: numTicksPerQuarter has been initialized
	 */
	private int determineNumTicksPerMeasure() {
		Fraction fracQuartersPerMeasure = meter.multiply(new Fraction(4,1));
		Fraction fracTicksPerMeasure = fracQuartersPerMeasure.multiply(new Fraction(numTicksPerQuarter, 1));
		return fracTicksPerMeasure.toInt();
	}
}
