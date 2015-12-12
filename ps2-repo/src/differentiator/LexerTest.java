package differentiator;

import org.junit.Test;
import static org.junit.Assert.*;


import differentiator.Token.Type;

public class LexerTest {
	@Test
	public void lexTestGoodInput() {
		//test string includes:
			//leading and trailing spaces, as well duplicate spaces,
			//sometimes no spaces between characters that don't require a space between them.
			//variable names including lower and upper case letters
		//order of tokens is not syntactically valid, but parser deals with that; lexer will be fine.
		Lexer lexer = new Lexer("  ( HeLLO)+*(bar (.151151(   ");
		Token[] tokens = lexer.lex();
		assertEquals(tokens.length, 11);
		assertEquals(tokens[0].getType(), Type.LP);
		assertEquals(tokens[1].getType(), Type.WORD);
		assertEquals(tokens[1].getVal(), "HeLLO");
		assertEquals(tokens[3].getType(), Type.ADD);
		assertEquals(tokens[4].getType(), Type.MULT);
		assertEquals(tokens[8].getVal(), ".151151");
		assertEquals(tokens[9].getType(), Type.LP);
		assertEquals(tokens[10].getType(), Type.EOF);
	}
	
	@Test (expected=RuntimeException.class)
	public void lexTestCombinedNumberWord() {
		//test that we don't allow combined number/words (e.g. 125cat)
		Lexer lexer = new Lexer("(x + 125cat)");
		lexer.lex();
	}
	
	@Test (expected=RuntimeException.class)
	public void lexTestMultipleDecimals() {
		//test that we don't allow more than one decimal point in a number
		Lexer lexer = new Lexer("(x + 12.12.12)");
		lexer.lex();
	}
	
	@Test (expected=RuntimeException.class)
	public void lexTestUnknownSymbol() {
		//test that we don't allow any symbol that isn't a terminal in our grammar
		Lexer lexer = new Lexer("(x + ?)");
		lexer.lex();
	}
}
