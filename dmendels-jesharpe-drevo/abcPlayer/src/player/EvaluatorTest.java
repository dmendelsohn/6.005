package player;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Assert;
import org.junit.Test;

import player.Token.Type;

import ast.*;


public class EvaluatorTest {
	/**
	 * Glassbox test for ensuring we handle basic repeats. 
	 * 
	 * @throws SyntaxErrorException
	 */
	@Test
	public void testExpandingRepeats() throws SyntaxErrorException {
		Lexer l = new Lexer("sample_abc/repeat_test");
        Parser p = new Parser(l);
        Evaluator e = new Evaluator(1);
        
        List<Measure> actual = e.EvalTest(1, p.getSong());
        List<Measure> correct = new ArrayList<Measure>();
        
        // measure 1
        List<Token> tokens1 = new ArrayList<Token>();
        tokens1.add(new Token(Token.Type.BASENOTE, "a"));
        NoteElement note1 = new NoteElement(tokens1);
        List<Element> measure1elems = new ArrayList<Element>();
        measure1elems.add(note1);
        Measure m1 = new Measure(measure1elems);
        
        // measure 2
        List<Token> tokens2 = new ArrayList<Token>();
        tokens2.add(new Token(Token.Type.BASENOTE, "B"));
        NoteElement note2 = new NoteElement(tokens2);
        List<Element> measure2elems = new ArrayList<Element>();
        measure2elems.add(note2);
        Measure m2 = new Measure(measure2elems);
        
        // measure 3
        List<Token> tokens3 = new ArrayList<Token>();
        tokens3.add(new Token(Token.Type.BASENOTE, "c"));
        NoteElement note3 = new NoteElement(tokens3);
        List<Element> measure3elems = new ArrayList<Element>();
        measure3elems.add(note3);
        Measure m3 = new Measure(measure3elems);
        
        // measure 4
        List<Token> tokens4 = new ArrayList<Token>();
        tokens4.add(new Token(Token.Type.BASENOTE, "B"));
        NoteElement note4 = new NoteElement(tokens4);
        List<Element> measure4elems = new ArrayList<Element>();
        measure4elems.add(note4);
        Measure m4 = new Measure(measure4elems);
        
        // measure 5
        List<Token> tokens5 = new ArrayList<Token>();
        tokens5.add(new Token(Token.Type.BASENOTE, "c"));
        NoteElement note5 = new NoteElement(tokens5);
        List<Element> measure5elems = new ArrayList<Element>();
        measure5elems.add(note5);
        Measure m5 = new Measure(measure5elems);
        
        // measure 5
        List<Token> tokens6 = new ArrayList<Token>();
        tokens6.add(new Token(Token.Type.BASENOTE, "d"));
        NoteElement note6 = new NoteElement(tokens6);
        List<Element> measure6elems = new ArrayList<Element>();
        measure6elems.add(note6);
        Measure m6 = new Measure(measure6elems);
        
        correct.add(m1);
        correct.add(m2);
        correct.add(m3);
        correct.add(m4);
        correct.add(m5);
        correct.add(m6);
        
        assertTrue(actual.size() == correct.size());
        for (int i = 0; i < correct.size(); ++i) {
        	assertTrue(actual.get(i).equals(correct.get(i)));
        }
	}
	
	/**
	 * Glassbox test for more complex repeats, ie, nth repeats
	 * 
	 * @throws SyntaxErrorException
	 */
	@Test
	public void testExpandingNthRepeats() throws SyntaxErrorException {
		Lexer l = new Lexer("sample_abc/nthrepeat_test");
        Parser p = new Parser(l);
        Evaluator e = new Evaluator(1);
        
        List<Measure> actual = e.EvalTest(1, p.getSong());
        List<Measure> correct = new ArrayList<Measure>();
        
        // measure 1
        List<Token> tokens1 = new ArrayList<Token>();
        tokens1.add(new Token(Token.Type.BASENOTE, "a"));
        NoteElement note1 = new NoteElement(tokens1);
        List<Element> measure1elems = new ArrayList<Element>();
        measure1elems.add(note1);
        Measure m1 = new Measure(measure1elems);
        
        // measure 2
        List<Token> tokens2 = new ArrayList<Token>();
        tokens2.add(new Token(Token.Type.BASENOTE, "B"));
        NoteElement note2 = new NoteElement(tokens2);
        List<Element> measure2elems = new ArrayList<Element>();
        measure2elems.add(note2);
        Measure m2 = new Measure(measure2elems);
        
        // measure 3
        List<Token> tokens3 = new ArrayList<Token>();
        tokens3.add(new Token(Token.Type.BASENOTE, "c"));
        NoteElement note3 = new NoteElement(tokens3);
        List<Element> measure3elems = new ArrayList<Element>();
        measure3elems.add(note3);
        Measure m3 = new Measure(measure3elems);
        
        // measure 4
        List<Token> tokens4 = new ArrayList<Token>();
        tokens4.add(new Token(Token.Type.BASENOTE, "B"));
        NoteElement note4 = new NoteElement(tokens4);
        List<Element> measure4elems = new ArrayList<Element>();
        measure4elems.add(note4);
        Measure m4 = new Measure(measure4elems);
        
        // measure 5
        List<Token> tokens5 = new ArrayList<Token>();
        tokens5.add(new Token(Token.Type.BASENOTE, "d"));
        NoteElement note5 = new NoteElement(tokens5);
        List<Element> measure5elems = new ArrayList<Element>();
        measure5elems.add(note5);
        Measure m5 = new Measure(measure5elems);
        
        correct.add(m1);
        correct.add(m2);
        correct.add(m3);
        correct.add(m4);
        correct.add(m5);
        
        assertTrue(actual.size() == correct.size());
        for (int i = 0; i < correct.size(); ++i) {
        	assertTrue(actual.get(i).equals(correct.get(i)));
        }
	}
	
	/**
	 * Glassbox test to ensure we expand tuplets correctly. 
	 * 
	 * @throws SyntaxErrorException
	 * @throws MidiUnavailableException
	 * @throws InvalidMidiDataException
	 */
	@Test
	public void testBasicExpandingTuplets() throws SyntaxErrorException, MidiUnavailableException, InvalidMidiDataException {
	    Voice voice = new Voice("test");
	    
	    ArrayList<Voice> voices = new ArrayList<Voice> ();
	    voices.add(voice);
	    
	    Token tok = new Token(Type.BASENOTE, "C");
	    ArrayList<Token> noteToken = new ArrayList<Token>();
	    noteToken.add(tok);
	    
	    ArrayList<Element> notes = new ArrayList<Element>();
	    notes.add(new NoteElement(noteToken));
	    notes.add(new NoteElement(noteToken));
	    
	    ArrayList<SyncElement> tupletNotes = new ArrayList<SyncElement> ();
	    tupletNotes.add(new NoteElement(noteToken));
	    tupletNotes.add(new NoteElement(noteToken));
	    tupletNotes.add(new NoteElement(noteToken));
	    notes.add(new TupletElement(tupletNotes));
	    
	    notes.add(new NoteElement(noteToken));
	    
	    voice.addElement(new Measure(notes));
	    
	    ArrayList<String> voiceNames = new ArrayList<String>();
	    voiceNames.add("test");
	    
	    Song song = new Song(new Header("1", "T", "unknown", "1/4", "4/4", "100", voiceNames, "C"), voices);
	    
	    Evaluator e = new Evaluator(2);
	    e.EvalTest(2, song);
	    
	    Measure measure = (Measure)song.getVoices().get(0).getElements().get(0);
	    ArrayList<String> outputNotes = new ArrayList<String>();
	    ArrayList<Fraction> outputDurations = new ArrayList<Fraction> ();
        for(Element note : measure.getElements()) {
            outputNotes.add(((NoteElement)note).getBasenote());
            outputDurations.add(((NoteElement)note).getDuration());
        }
        
        String[] expectedNotes = {"C","C","C","C","C","C"};
        Fraction[] expectedLengths = {new Fraction(1,1),new Fraction(1,1), new Fraction(2,3), new Fraction(2,3), new Fraction(2,3),new Fraction(1,1)  };
        
        Assert.assertArrayEquals((Object[])expectedNotes, outputNotes.toArray());
        Assert.assertArrayEquals(expectedLengths, (Object[])outputDurations.toArray());
	}
	
	/**
	 * More advanced glassbox test for expanding tuplets
	 * 
	 * @throws SyntaxErrorException
	 */
	@Test
    public void testExpandingTuplets() throws SyntaxErrorException {
        
        Lexer l = new Lexer("sample_abc/tuplet_test");
        Parser p = new Parser(l);
        Evaluator e = new Evaluator(2);
        e.EvalTest(2, p.getSong());
        
        
        ArrayList<String> outputNotes = new ArrayList<String>();
        ArrayList<Fraction> outputDurations = new ArrayList<Fraction> ();        
        
        
        ArrayList<HighElement> measures = (ArrayList<HighElement>)p.getSong().getVoices().get(0).getElements();
        
        for(HighElement hElem : measures){
            if (hElem instanceof Measure){
                for (Element note : ((Measure) hElem).getElements()) {
                    outputNotes.add(((NoteElement)note).getBasenote());
                    outputDurations.add(((NoteElement)note).getDuration());
                }  
            }
        }
        
        String[] expectedNotes = {"A","B","C","D","E"};
        Fraction[] expectedLengths = {new Fraction(1,1),new Fraction(1,1), new Fraction(1,1), new Fraction(3,2), new Fraction(3,2)};
        
        Assert.assertArrayEquals((Object[])expectedNotes, outputNotes.toArray());
        Assert.assertArrayEquals(expectedLengths, (Object[])outputDurations.toArray());
	}
}
