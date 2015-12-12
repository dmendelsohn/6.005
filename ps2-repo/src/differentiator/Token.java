package differentiator;


/**
 * A token is a lexical item that the parser uses.
 */
public class Token {
    /**
     * LP: (
     * RP: )
     * ADD: +
     * MULT: *
     * NUM: Combinations of 0-9 maybe including a decimal point
     * LET: a-z A-Z
     * EOF: an end of file token that we the Lexer generates
     * 
     */
    public static enum Type {
        LP,
        RP,
        ADD,
        MULT,
        NUM,
        WORD,
        EOF;
    }
    
    private final Type tokenType;
    private final String tokenVal;

    /**
     * Constructs a Token object
     * @param type
     * @param value
     * Requires value be:
     *		"+" when type = ADD
     *		"*" when type = MULT
     *		"(" when type = LP
     *		")" when type = RP
     */
    public Token(Type type, String value) {
    	tokenType = type;
    	tokenVal = value;
    }
    
    public Type getType() {
    	return tokenType;
    }
    
    public String getVal() {
    	return tokenVal;
    }
    
    @Override
    public String toString() {
    	if (tokenType.equals(Type.EOF))
    		return "EOF";
    	else
    		return getVal();
    	
    }
}