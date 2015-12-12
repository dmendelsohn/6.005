package player;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class LexerTest {
	/**
	 * Tests correct lexing of Header
	 * 
	 * @throws SyntaxErrorException
	 */
	@Test
	public void headerLexTest() throws SyntaxErrorException {
		Lexer lexer = new Lexer("sample_abc/body_test.abc");
		
		Token t;
		List<Token> actual = new ArrayList<Token>();
		t = lexer.nextHeader();
		while (t.getType() != Token.Type.EOH) {
			actual.add(t);
			t = lexer.nextHeader();
		}
		
		List<Token> expected = new ArrayList<Token>();
		expected.add(new Token(Token.Type.INDEX, "1"));
		expected.add(new Token(Token.Type.TITLE, "Piece No.1"));
		expected.add(new Token(Token.Type.VOICE, "upper"));
		expected.add(new Token(Token.Type.METER, "C"));
		expected.add(new Token(Token.Type.LENGTH, "1/4"));
		expected.add(new Token(Token.Type.TEMPO, "140"));
		expected.add(new Token(Token.Type.KEY, "C"));
		
		assertEquals(actual.size(), expected.size());
		
		for (int i = 0; i < actual.size(); i++) {
			if (!expected.get(i).equals(actual.get(i)))
				assertTrue(false);
		}
	}
	
	/**
	 * Simply ensures that on a complex but valid piece of music, no
	 * SyntaxErrorExceptions are thrown
	 * 
	 * @throws SyntaxErrorException
	 */
	@Test
	public void doesItFailOnFurElise() throws SyntaxErrorException {
		Lexer lexer = new Lexer("sample_abc/fur_elise.abc");
		assertTrue(true);
	}
	
	/**
	 * Tests very simple bug we saw early on in the project
	 * 
	 * @throws SyntaxErrorException
	 */
	@Test
	public void simpleLetter() throws SyntaxErrorException {
		Lexer lexer = new Lexer("sample_abc/scratch");
		
		Token t;
		List<Token> actual = new ArrayList<Token>();
		t = lexer.next();
		while (t.getType() != Token.Type.EOF) {
			actual.add(t);
			t = lexer.next();
		}
		
		List<Token> expected = new ArrayList<Token>();
		expected.add(new Token(Token.Type.BASENOTE, "G"));
		expected.add(new Token(Token.Type.OCTAVE, ","));
		expected.add(new Token(Token.Type.BASENOTE, "G"));
		
		assertEquals(actual.size(), expected.size());
		
		for (int i = 0; i < actual.size(); i++) {
			if (!expected.get(i).equals(actual.get(i)))
				assertTrue(false);
		}
	}
	
	/**
	 * Tests lexing of body ABC text
	 * 
	 * @throws SyntaxErrorException
	 */
	@Test
	public void bodyLexTest() throws SyntaxErrorException {
		Lexer lexer = new Lexer("sample_abc/body_test.abc");
		
		Token t;
		List<Token> actual = new ArrayList<Token>();
		t = lexer.next();
		while (t.getType() != Token.Type.EOF) {
			actual.add(t);
			t = lexer.next();
		}
		
		List<Token> expected = new ArrayList<Token>();
		expected.add(new Token(Token.Type.VOICE_BODY, "upper"));
		expected.add(new Token(Token.Type.BASENOTE, "C"));
		expected.add(new Token(Token.Type.OCTAVE, "'"));
		expected.add(new Token(Token.Type.TUPLE_START, "3"));
		expected.add(new Token(Token.Type.BASENOTE, "e"));
		expected.add(new Token(Token.Type.BASENOTE, "e"));
		expected.add(new Token(Token.Type.BASENOTE, "f"));
		expected.add(new Token(Token.Type.BASENOTE, "a"));
		expected.add(new Token(Token.Type.OCTAVE, ",,"));
		expected.add(new Token(Token.Type.BASENOTE, "b"));
		expected.add(new Token(Token.Type.OCTAVE, "'''"));
		expected.add(new Token(Token.Type.BASENOTE, "e"));
		expected.add(new Token(Token.Type.ACCIDENTAL, "^^"));
		expected.add(new Token(Token.Type.BASENOTE, "g"));
		expected.add(new Token(Token.Type.ACCIDENTAL, "__"));
		expected.add(new Token(Token.Type.VOICE_BODY, "upper"));
		expected.add(new Token(Token.Type.BASENOTE, "f"));
		expected.add(new Token(Token.Type.ACCIDENTAL, "^"));
		expected.add(new Token(Token.Type.BASENOTE, "g"));
		expected.add(new Token(Token.Type.ACCIDENTAL, "_"));
		expected.add(new Token(Token.Type.BAR, "|:"));
		expected.add(new Token(Token.Type.BAR, "|"));
		expected.add(new Token(Token.Type.BASENOTE, "a"));
		expected.add(new Token(Token.Type.BAR, "|]"));
		expected.add(new Token(Token.Type.BAR, "||"));
		expected.add(new Token(Token.Type.BASENOTE, "b"));
		expected.add(new Token(Token.Type.BAR, "|"));
		expected.add(new Token(Token.Type.NTH_REPEAT, "2"));	
		expected.add(new Token(Token.Type.CHORD_START, "["));
		expected.add(new Token(Token.Type.BASENOTE, "G"));
		expected.add(new Token(Token.Type.BASENOTE, "E"));
		expected.add(new Token(Token.Type.CHORD_END, "]"));
		
		assertEquals(actual.size(), expected.size());
		
		for (int i = 0; i < actual.size(); i++) {
			if (!(expected.get(i).equals((Token)actual.get(i)))) {
				System.out.println(expected.toString() + "\n == \n" + actual.toString());
				assertTrue(false);
			}
		}
	}
}
