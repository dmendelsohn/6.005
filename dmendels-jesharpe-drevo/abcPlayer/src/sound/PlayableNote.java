package sound;

import java.util.Map;

import player.Fraction;
import ast.NoteElement;

public class PlayableNote {
	public static final int NATURAL = 0;
	public static final int SHARP = 1;
	public static final int DOUBLE_SHARP = 2;
	public static final int FLAT = -1;
	public static final int DOUBLE_FLAT = -2;
	
	private Pitch pitch;
	private int numTicks;
	private int startTick;
	
	/**
	 * Constructor for PlayableNote.  Note that this object is immutable.
	 * @param note - a NoteElement thats contains information about the basenote, accidental, and octave
	 * @param startTick - the tick at which the sequencer should play this note
	 * @param defaultNoteLength - the default note length defined in the header
	 * @param numTicksPerQuarter - our calculated value for the number of ticks in a quarter note
	 * @param measureKeyMap - a copy of the key signature map for this key
	 * 
	 * @modifies measureKeyMap - modifies the key map if this note has an accidental, since 
	 * that accidental applies for all notes of the basenote in the rest of the measure
	 */
	public PlayableNote(NoteElement note, int startTick, Fraction defaultNoteLength, 
			int numTicksPerQuarter, Map<String,Integer> measureKeyMap){
		String basenote = note.getBasenote();
		String accidental = note.getAccidental();
		String octave = note.getOctave();
		
		char originalC = basenote.charAt(0);
		char c = basenote.toUpperCase().charAt(0);
		Pitch p = new Pitch(c);
		if (originalC >= 'a' && originalC <= 'g') //lower case letters are an octave higher
			p = p.transpose(Pitch.OCTAVE);
		
		if (octave.length() > 0)
			p = p.transpose(octaveStringToNumOctaves(octave)*Pitch.OCTAVE);
		
		if (accidental.length() > 0) {
			int accidentalStep = accidentalStringToInt(accidental);
			p = p.transpose(accidentalStep);
			measureKeyMap.put(new Character(c).toString(), accidentalStep);
		} else {
			int accidentalStep = measureKeyMap.get(new Character(c).toString());
			p = p.transpose(accidentalStep);
		}
			
		Fraction duration = note.getDuration();
		int durationInTicks = duration.multiply(defaultNoteLength)
								.multiply(new Fraction(4,1))
								.multiply(new Fraction(numTicksPerQuarter, 1))
								.toInt();
		
		this.pitch = p;
		this.numTicks = durationInTicks;
		this.startTick = startTick;
	}
	
	
	/**
	 * Gets the number of octaves represented by a string of , or '
	 * @param the value held by an OCTAVE token
	 * @return the number of octaves represented by the string, where higher octaves are positive
	 * and lower octaves are negative
	 */
	private int octaveStringToNumOctaves(String octave) {
		if (octave.length() == 0)
			return 0;
		else if (octave.charAt(0) == '\'')
			return octave.length();
		else if (octave.charAt(0) == ',')
			return -1*octave.length();
		else
			throw new RuntimeException("Error while evalulating octave string");
	}
	
	
	/**
	 * Convert an accidental in string form to a number of tonal steps
	 * @param a possible value of an accidental token
	 * @return the number of steps that token calls for.  sharping is +1, flatting is -1
	 */
	private int accidentalStringToInt(String accidental) {
		if (accidental.length() == 0 || accidental.equals("="))
			return NATURAL;
		else if(accidental.equals("^"))
			return SHARP;
		else if(accidental.equals("^^"))
			return DOUBLE_SHARP;
		else if(accidental.equals("_"))
			return FLAT;
		else if(accidental.equals("__"))
			return DOUBLE_FLAT;
		else
			throw new RuntimeException("Error while evaluating accidental string");
		}

	/**
	 * Returns the pitch field of this playable note
	 * @return the pitch of this PlayableNote
	 */
	public Pitch getPitch() {
		return pitch; //since Pitch is immutable, PlayableNote is still immutable
	}
	
	/**
	 * Gets the number of ticks that this note should be played for
	 * @return the numTicks field of this Playable Note
	 */
	public int getNumTicks() {
		return numTicks;
	}
	
	/**
	 * Gets the tick at which this note should start
	 * @return the startTick field of this PlayableNote
	 */
	public int getStartTick() {
		return startTick;
	}
	
}
