package differentiator;

import nodes.AddNode;
import nodes.MultNode;
import nodes.Node;
import nodes.NumberNode;
import nodes.VarNode;
import differentiator.Token.Type;

/**
 * The parser gets a bunch of tokens from the lexer and determines what
 * expression was written by the user.
 */

/**
 * OP ::= + | *
 * LEFT ::= (
 * RIGHT ::= )
 * CONST ::= ([0-9])*.?([0-9])*
 * VAR ::= ([a-zA-Z])*
 * 
 * EXP ::= LEFT (CONST | VAR) (OP (CONST | VAR))? RIGHT
 *
 */
public class Parser {
	private Token[] inputTokens;
	private Lexer mLexer;
	
    /**
     * Creates a new parser over the lexer.  This parser will use the passed
     * lexer to get tokens--which it will then parse.
     * @param lexer The lexer.
     */
    public Parser(Lexer lexer) {
        mLexer = lexer;
        inputTokens = mLexer.lex();
    }
    
    /**
     * An accessor method for inputTokens
     * @return the inputTokens instance variable
     */
    public Token[] getInputTokens() {
    	return inputTokens;
    }
    
    /**
     * Parses the tokens in inputTokens and turns it into a AST.
     * @return the node at the root of the AST
     * 
     * throws runtime exceptions if the tokens don't comprise a valid expression,
     * according to the above grammar
     */
    public Node parse() {
    	if (inputTokens.length == 0)
    		return null;
    	else {
    		int lp = 0;
    		if (!inputTokens[lp].getType().equals(Token.Type.LP)) //doesn't start with LP
    			throw new RuntimeException("[parser] must begin with LP");
    		int rp = findMatchingParen(lp);
    		if (!inputTokens[rp+1].getType().equals(Token.Type.EOF)) //doesn't end with RP
    			throw new RuntimeException("[parser] input must be a validly parenthesized string, bookended by parens");
    		return parsePart(lp, rp);
    	}
    }
    
    /**
     * 
     * Does the same thing as parse, but for a specific subset of inputTokens (producing an AST)
     * @param lp - an int representing an index in inputTokens that necessarily contains a LP token
     * @param rp - an int representing an index in inputTokens that necessarily contains a RP token
     * @return the node at the root of the AST resulting from the subset of inputTokens between
     * lp and rp (exlusive)
     */
    public Node parsePart(int lp, int rp) {
    	int nextToken = -1; //dummy
    	Node leftTree = new NumberNode(); //dummy
    	Node opNode = new AddNode(); //dummy
    	Node rightTree = new NumberNode(); //dummy
    	//set up left side
    	if (inputTokens[lp+1].getType().equals(Type.LP)) { //dealing with subtree
    		int rightParen = findMatchingParen(lp+1);
    		nextToken = rightParen + 1;
    		leftTree = parsePart(lp+1, rightParen);
    	} else if (inputTokens[lp+1].getType().equals(Type.WORD)){
    		leftTree = new VarNode();
    		leftTree.setToken(inputTokens[lp+1]);
    		nextToken = lp + 2;
    	} else if (inputTokens[lp+1].getType().equals(Type.NUM)) {
    		leftTree = new NumberNode();
    		leftTree.setToken(inputTokens[lp+1]);
    		nextToken = lp +2;
    	} else {
    		throw new RuntimeException("[parser] We didn't have a RP or WORD or NUM when we needed one");
    	}
    	
    	//see if either we need have a right subtree or we're done or it's bad input
    	if (inputTokens[nextToken].getType().equals(Type.RP)) {
    		return leftTree;
    	} else if (inputTokens[nextToken].getType().equals(Type.ADD)) {
    		
    		//deal with right side of add operation
        	if (inputTokens[nextToken+1].getType().equals(Type.LP)) { //dealing with subtree
        		int rightParen = findMatchingParen(nextToken+1);
        		rightTree = parsePart(nextToken+1, rightParen);
        		nextToken = rightParen + 1;
        	} else if (inputTokens[nextToken+1].getType().equals(Type.WORD)){
        		rightTree = new VarNode();
        		rightTree.setToken(inputTokens[nextToken+1]);
        		nextToken = nextToken + 2;
        	} else if (inputTokens[nextToken+1].getType().equals(Type.NUM)) {
        		rightTree = new NumberNode();
        		rightTree.setToken(inputTokens[nextToken+1]);
        		nextToken = nextToken +2;
        	}
        	
        	if (nextToken == rp) { //we've parsed the whole statement, so finish
        		opNode = new AddNode();
        		opNode.setToken(new Token(Token.Type.ADD, "+"));
        		opNode.setLeftChild(leftTree);
        		opNode.setRightChild(rightTree);
        		return opNode;
        	} else {
        		throw new RuntimeException("[parser] too much stuff in a set of parens");
        	}
    	} else if (inputTokens[nextToken].getType().equals(Type.MULT)) {
    		
    		//deal with right side of mult operation
        	if (inputTokens[nextToken+1].getType().equals(Type.LP)) { //dealing with subtree
        		int rightParen = findMatchingParen(nextToken+1);
        		rightTree = parsePart(nextToken+1, rightParen);
        		nextToken = rightParen + 1;
        	} else if (inputTokens[nextToken+1].getType().equals(Type.WORD)){
        		rightTree = new VarNode();
        		rightTree.setToken(inputTokens[nextToken+1]);
        		nextToken = nextToken + 2;
        	} else if (inputTokens[nextToken+1].getType().equals(Type.NUM)) {
        		rightTree = new NumberNode();
        		rightTree.setToken(inputTokens[nextToken+1]);
        		nextToken = nextToken +2;
        	}
        	
        	if (nextToken == rp) { //we've parsed the whole statement, so finish
        		opNode = new MultNode();
        		opNode.setToken(new Token(Token.Type.MULT, "*"));
        		opNode.setLeftChild(leftTree);
        		opNode.setRightChild(rightTree);
        		return opNode;
        	} else {
        		throw new RuntimeException("[parser] too much stuff in a set of parens");
        	}
    	} else { // we didn't have a RP or ADD or MULT token when we needed one
    		throw new RuntimeException("[parser] we didn't have a RP or ADD or MULT when we needed one");
    	}
    }
    
    /**
     * Given the index of an LP token in inputTokens, find the index of the matching RP token
     * @param lp - an index in inputTokens that necessarily contains an LP token
     * @return the index of the matching RP token, if one exists.  Otherwise it will throw a
     * Runtime exception
     */
    public int findMatchingParen(int lp) {
    	int surplus = 1;
    	for (int i = lp + 1; i < inputTokens.length; i++) {
    		if (inputTokens[i].getType().equals(Token.Type.LP))
    			surplus++;
    		else if (inputTokens[i].getType().equals(Token.Type.RP)) {
    			surplus--;
    			if (surplus == 0)
    				return i;
    		}
    	}
    	throw new RuntimeException("[parser] no matching right paren");
    }
}
