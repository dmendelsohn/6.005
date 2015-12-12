package player;

import java.util.ArrayList;
import java.util.List;

import ast.BarlineElement;
import ast.ChordElement;
import ast.Element;
import ast.Header;
import ast.HighElement;
import ast.Measure;
import ast.NoteElement;
import ast.NthRepeatElement;
import ast.RestElement;
import ast.Song;
import ast.SyncElement;
import ast.TupletElement;
import ast.Voice;

public class Parser {
	private final Lexer lex;
    private List<Voice> voices;
    private Voice defaultVoice = new Voice("DEFAULT");
    private Song song;
    
    // state (minimal)
    private Voice currentVoice = defaultVoice;
    
    /**
     * Takes in Lexer and parses Tokens into a Song
     * 
     * @param lexer
     * @throws SyntaxErrorException
     */
    public Parser(Lexer lexer) throws SyntaxErrorException{
        this.lex = lexer;
        this.song = parse();
    }
    
    /**
     * Initializes voices from Header and then parses body.
     * 
     * @return new Song
     * @throws SyntaxErrorException
     */
    public Song parse() throws SyntaxErrorException {
    	// Header
        Header header = parseHeader();
        
        // Initialize voices
        voices = initVoices(header);
        
        parseBody(lex.next());
        
        return new Song(header, voices);
    }
    
    /**
     * Takes Header h and returns a list of voices
     * 
     * @param h
     * @return List<Voice>
     */
    public List<Voice> initVoices(Header h) {
    	List<Voice> voices = new ArrayList<Voice>();
    	
    	// If there is no explicit voice in header
    	if (h.getVoiceNames().size() == 0) {
    		voices.add(defaultVoice);
    	} else {
    		for (String s : h.getVoiceNames()) {
    			// add the voice
    			voices.add(new Voice(s));
    		}
    	}
    	
		return voices;
    }
    
    /**
     * Searches list of Voices to find matching Voice by String name
     * 
     * Inefficient, but number of Voices are so small
     * 
     * @param name
     * @return Voice found
     */
    public Voice findVoiceByName(String name) {
    	for (Voice v : voices) {
    		if (v.getName().equals(name)) {
    			return v;
    		}
    	}
    	return null;
    }

    /**
     * Parses body given the starting token
     * 
     * @param Token t
     * @throws SyntaxErrorException
     */
    public void parseBody(Token t) throws SyntaxErrorException {
    	while (t.getType() != Token.Type.EOF) {
    		switch (t.getType()) {
    		case BAR:
    			currentVoice.addElement((HighElement) new BarlineElement(t));
    			t = lex.next();
    			break;
    		case NTH_REPEAT:
    			currentVoice.addElement((HighElement) new NthRepeatElement(t));
    			t = lex.next();
    			break;
    		case VOICE_BODY:
    			Voice found = findVoiceByName(t.getValue());
    			if (found == null) {
    				throw new RuntimeException("Voice encountered was not in Header");
    			} else currentVoice = found;
    			t = lex.next();
    			break;
    		
    		case BASENOTE:
    		case ACCIDENTAL:
    		case CHORD_START:
    		case TUPLE_START:
    		case REST:
    			TokenElementPair tep = parseMeasure(t);
    			t = tep.t;
    			currentVoice.addElement(tep.h);
    			break;
    		default:
    			throw new RuntimeException("Measure began with invalid Token.");
    		}
    	}
    }
    
    /**
     * Lower level parsing of Measure, calls even lower level parsing functions, passing
     * up the chain TokenElementPairs
     * 
     * @param t
     * @return
     * @throws SyntaxErrorException
     */
    public TokenElementPair parseMeasure(Token t) throws SyntaxErrorException {
    	List<Element> measureBuffer = new ArrayList<Element>();
    	
    	while (t.getType() != Token.Type.BAR && t.getType() != Token.Type.EOF) {
    		TokenElementPair tep;
    		
    		switch(t.getType()) {
    		case BASENOTE:
    		case ACCIDENTAL:
    			tep = parseNote(t);
    			measureBuffer.add((Element) tep.h); 
    			t = tep.t;
    			break;
    		case CHORD_START:
    			tep = parseChord(t);
    			measureBuffer.add((Element) tep.h); 
    			t = tep.t;
    			break;
    		case TUPLE_START:
    			tep = parseTuplet(t);
    			measureBuffer.add((Element) tep.h); 
    			t = tep.t;
    			break;
    		case REST:
    			tep = parseRest(t);
    			measureBuffer.add((Element) tep.h); 
    			t = tep.t;
    			break;
    		default:
    			throw new RuntimeException("Failure parsing measure.");
    		}
    	}
    	
    	Measure m = new Measure(measureBuffer);
    	return new TokenElementPair(t, m);
    }
    
    /**
     * Parses note.
     * 
     * @param t
     * @return
     * @throws SyntaxErrorException
     */
    public TokenElementPair parseNote(Token t) throws SyntaxErrorException {
    	List<Token> tokens = new ArrayList<Token>();
    	
    	// collect all possible note tokens
    	
    	if (t.getType() == Token.Type.ACCIDENTAL) {
    		tokens.add(t);
    		t = lex.next();
    	}
    	if (t.getType() == Token.Type.BASENOTE) {
    		tokens.add(t);
    		t = lex.next();
    	} else
    		throw new RuntimeException("Note must include basenote");
    	if (t.getType() == Token.Type.OCTAVE) {
    		tokens.add(t);
    		t = lex.next();
    	}
    	if (t.getType() == Token.Type.DURATION) {
    		tokens.add(t);
    		t = lex.next();
    	}
    	
    	NoteElement ne = new NoteElement(tokens);
    	return new TokenElementPair(t, ne);
    }
    /**
     * Parses chords.
     * 
     * @param t
     * @return
     * @throws SyntaxErrorException
     */
    public TokenElementPair parseChord(Token t) throws SyntaxErrorException {
    	List<NoteElement> elems = new ArrayList<NoteElement>();
    	
    	// current token is bracket, so move into chord
    	t = lex.next();
    	
    	while (t.getType() != Token.Type.CHORD_END) {
    		switch(t.getType()) {
    		case BASENOTE:
    		case ACCIDENTAL:
    			TokenElementPair tep = parseNote(t);
    			t = tep.t;
    			elems.add((NoteElement) tep.h);
    			break;
    		default:
    			throw new RuntimeException("Error parsing chord.");
    		}
    	}
    	
    	ChordElement c = new ChordElement(elems);
    	return new TokenElementPair(lex.next(), c);
    } 
    /**
     * Parses Tuplet
     * 
     * @param t
     * @return
     * @throws SyntaxErrorException
     */
    public TokenElementPair parseTuplet(Token t) throws SyntaxErrorException {
    	List<SyncElement> elems = new ArrayList<SyncElement>();
    	
    	// Get number of tuplet
    	int num = Integer.parseInt(t.getValue());
    	
    	// Increment lexer
    	t = lex.next();
    	
    	int i= 0;
    	while (i < num) {
    		TokenElementPair tep;
    		
    		switch(t.getType()) {
    		case BASENOTE:
    		case ACCIDENTAL:
    			tep = parseNote(t);
    			elems.add((SyncElement) tep.h); 
    			t = tep.t;
    			break;
    		case CHORD_START:
    			tep = parseChord(t);
    			elems.add((SyncElement) tep.h); 
    			t = tep.t;
    			break;
    		case REST:
    			tep = parseRest(t);
    			elems.add((SyncElement) tep.h); 
    			t = tep.t;
    			break;
    		default:
    			throw new RuntimeException("Error paring tuplet.");
    		}
    		
    		i++;
    	}
    	
    	TupletElement te = new TupletElement(elems);
    	
    	return new TokenElementPair(t, te);
    }
    
    /**
     * Parses rests
     * @param t
     * @return
     * @throws SyntaxErrorException
     */
    public TokenElementPair parseRest(Token t) throws SyntaxErrorException {
    	t = lex.next();
    	RestElement re;
    	if (t.getType() == Token.Type.DURATION) {
    		re = new RestElement(t);
    		t = lex.next();
    	} else {
    		re = new RestElement();
    	}
    	return new TokenElementPair(t, re);
    }
    
    /**
     * Parses header
     * 
     * @return ABC Header defined by tokens
     * @throws SyntaxErrorException 
     */
    public Header parseHeader() throws SyntaxErrorException {
        
        String index = "";         // X: index number; always comes first
        String title = "";      // T: title; always comes second
        String composer = "Unknown";   // C: 
        String length = "1/8";        // L:
        String meter = "4/4";         // M:
        String tempo = "100";         // Q:
        String key = "";   // K: key; Always comes last
        List<String> voiceNames = new ArrayList<String>();
        
        // 0 => index not found
        // 1 => index found
        // 2 => title found
        // 3 => key found
        int state = 0;
        
        Token t;
		t = lex.nextHeader();
		while (t.getType() != Token.Type.EOH) {
			// do something with token
			switch(t.getType()) {
			case INDEX:
				if (state != 0) {
					throw new RuntimeException("Index found out of order");
				}
				state++;
				
				index = t.getValue();
				break;
			case TITLE:
				if (state != 1) {
					throw new RuntimeException("Title found out of order");
				}
				state++;
				
				title = t.getValue();
				break;
			case COMPOSER:
				if (state != 2) {
					throw new RuntimeException("Composer found out of order");
				}
				
				composer = t.getValue();
				break;
			case LENGTH:
				if (state != 2) {
					throw new RuntimeException("Length found out of order");
				}
				length = t.getValue();
				break;
			case METER:
				if (state != 2) {
					throw new RuntimeException("Meter found out of order");
				}
				meter = t.getValue();
				break;
			case TEMPO:
				if (state != 2) {
					throw new RuntimeException("Tempo found out of order");
				}
				tempo = t.getValue();
				break;
			case VOICE:
				if (state != 2) {
					throw new RuntimeException("Voice found out of order");
				}
				voiceNames.add(t.getValue());
				break;
			case KEY:
				if (state != 2) {
					throw new RuntimeException("Key found out of order");
				}
				state++;
				key =  t.getValue();
				break;
			default:
				throw new RuntimeException("Invalid token found.");
			}
			
			t = lex.nextHeader();
		}
		
		// Test for null fields
		if (state != 3) {
			throw new RuntimeException("Null fields in header.");
		}
		
		// return new Header
		return new Header(index, title, composer, length, meter, tempo, voiceNames, key);
    }
    
    /**
     * Song accessor method
     * @return
     */
    public Song getSong() {
    	return this.song;
    }

}
