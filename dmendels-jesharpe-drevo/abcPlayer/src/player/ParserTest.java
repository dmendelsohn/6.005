package player;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import player.Token.Type;
import ast.BarlineElement;
import ast.ChordElement;
import ast.Element;
import ast.Measure;
import ast.NoteElement;
import ast.Song;
import ast.SyncElement;
import ast.TupletElement;
import ast.Voice;

public class ParserTest {

    @Test
    public void basicTest() throws SyntaxErrorException{
        Lexer l = new Lexer("sample_abc/parser_test");
        Parser p = new Parser(l);
        Song outputSong = p.getSong();
        
        //System.out.println(p.getSong().getVoices().get(0));
        //System.out.println(p.getSong().getVoices().get(1));
        
        Voice low = new Voice("low");
        
        // Lower voice
        List<NoteElement> chordElems = new ArrayList<NoteElement>();
        List<Token> tokens1 = new ArrayList<Token>();
        
        tokens1.add(new Token(Token.Type.BASENOTE, "D"));
        chordElems.add(new NoteElement(tokens1));
        tokens1.clear();
        tokens1.add(new Token(Token.Type.BASENOTE, "E"));
        chordElems.add(new NoteElement(tokens1));
        tokens1.clear();
        tokens1.add(new Token(Token.Type.BASENOTE, "F"));
        chordElems.add(new NoteElement(tokens1));
        tokens1.clear();
        
        List<Element> measure1elems = new ArrayList<Element>();
        
        measure1elems.add(new ChordElement(chordElems));
        Measure m1 = new Measure(measure1elems);
        low.addElement(m1);
        
        low.addElement(new BarlineElement(new Token(Type.BAR, "|")));
        
        List<SyncElement> tupletElems = new ArrayList<SyncElement>();
        List<Token> tokens2 = new ArrayList<Token>();
        
        tokens2.add(new Token(Token.Type.BASENOTE, "A"));
        tupletElems.add(new NoteElement(tokens2));
        tokens2.clear();
        tokens2.add(new Token(Token.Type.BASENOTE, "B"));
        tupletElems.add(new NoteElement(tokens2));
        tokens2.clear();
        tokens2.add(new Token(Token.Type.BASENOTE, "C"));
        tupletElems.add(new NoteElement(tokens2));
        tokens2.clear();
        
        List<Element> measure2elems = new ArrayList<Element>();
        
        measure2elems.add(new TupletElement(tupletElems));
        Measure m2 = new Measure(measure2elems);
        low.addElement(m2);
        
        low.addElement(new BarlineElement(new Token(Type.BAR, "|")));
        
        /*
         * By inspection in the debugger the structures were the same. 
         * 
         * Whoooo!
         */
    }
    
}
