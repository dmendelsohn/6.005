package differentiator;
import static org.junit.Assert.assertEquals;
import nodes.Node;

import org.junit.Test;


public class ParserTest {
	//Test where input token array is a valid expression including:
		//nested operations
		//redundant parentheses
		//parentheses something containing operation, and sometimes just containing number/variable
	@Test
	public void parseTestValidTokens() {
		Lexer lexer = new Lexer("(((2.9) + ((bar))) * (((2.5 + baz) * foo)))");
		Parser parser = new Parser(lexer);
		Node n = parser.parse();
		assertEquals(n.convertTreeToString(), "((2.9+bar)*((2.5+baz)*foo))");
	}
	
	@Test(expected=RuntimeException.class)
	public void parseTestTooManyOperatorsInParens() {
		Lexer lexer = new Lexer("(5 + 5 + 5");
		Parser parser = new Parser(lexer);
		parser.parse();
	}
	
	@Test(expected=RuntimeException.class)
	public void parseTestLackingOuterParens() {
		Lexer lexer = new Lexer("x");
		Parser parser = new Parser(lexer);
		parser.parse();
	}
	
	@Test(expected=RuntimeException.class)
	public void parseTestEmptyParens() {
		Lexer lexer = new Lexer("(x + ())");
		Parser parser = new Parser(lexer);
		parser.parse();
	}
	
	@Test(expected=RuntimeException.class)
	public void parseTestOuterParensDontMatch() {
		Lexer lexer = new Lexer("(x + y) (x)");
		Parser parser = new Parser(lexer);
		parser.parse();
	}
	
	@Test(expected=RuntimeException.class)
	public void parseTestNotValidParenthesization() {
		Lexer lexer = new Lexer("(x))");
		Parser parser = new Parser(lexer);
		parser.parse();
	}
	
	@Test(expected=RuntimeException.class)
	public void parseTestTwoOperatorsInARow() {
		Lexer lexer = new Lexer("(x + * y)");
		Parser parser = new Parser(lexer);
		parser.parse();
	}
	
	@Test(expected=RuntimeException.class)
	public void parseTestTwoNumbersOrVariablesInARow() {
		Lexer lexer = new Lexer("(x 2.0)");
		Parser parser = new Parser(lexer);
		parser.parse();
	}
}
