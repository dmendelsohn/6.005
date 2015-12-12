package differentiator;

/**
 * A lexer takes a string and splits it into tokens that are meaningful to a
 * parser.
 */
public class Lexer {
	private final String input;
	
    /**
     * Creates the lexer over the passed string.
     * @param string The string to tokenize.
     */
    public Lexer(String string) {
        input = string;
    }
    
    public String getInput() {
    	return input;
    }
    
    /**
     * 
     * @param s - any input string
     * @return - a Token[] representing the tokenization of the input including an EOF token at the end
     * 
     * throws Runtime exception if the string cannot be validly parsed
     * into tokens (according to the assignment). There will be
     * an exception of any symbols that arent alphanumeric or +,*,(,).
     * There will be an exception if a string with spaces contains both numbers
     * and letters.  There will be an exception of a number has multiple decimal points.
     */
    public Token[] lex() {
    	String input = cleanString(getInput());
    	String[] tokenStrings = input.split(" ");
    	Token[] tokens = new Token[tokenStrings.length+1];
    	for (int i = 0; i < tokenStrings.length; i++) {
    		Token t = getToken(tokenStrings[i]);
    		if (t == null) {
    			throw new RuntimeException("[lexer] didn't get token");
    		} else {
    			tokens[i] = t;
    		}
    	}
    	tokens[tokens.length-1] = new Token(Token.Type.EOF, null);
    	return tokens;
    }
    
    /**
     * 
     * @param s - a string with length > 0
     * @return the token that is encoded by that string, or null if no such token exists
     */
    private Token getToken(String s) {
    	if (s.equals("("))
    		return new Token(Token.Type.LP, s);
    	else if (s.equals(")"))
    		return new Token(Token.Type.RP, s);
    	else if (s.equals("+"))
    		return new Token(Token.Type.ADD, s);
    	else if (s.equals("*"))
    		return new Token(Token.Type.MULT, s);
    	else {
    		char firstChar = s.charAt(0);
    		if (isNumChar(firstChar))
    			return getNumberToken(s);
    		else if (isLetterChar(firstChar))
    			return getStringToken(s);
    		else {
    			return null;
    		}
    	}
    }
    
    /**
     * 
     * @param s - a string with length > 0
     * @return the token of type NUM that is encoded by that string, or null if no such token exists
     * 
     */ 
    private Token getNumberToken(String s) {
    	boolean hasDecimalPoint = false;
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < s.length(); i++) {
    		char currentChar = s.charAt(i);
    		if (isNumChar(currentChar)) {
    			if (currentChar != '.' || !hasDecimalPoint)
    				sb.append(currentChar);
    			else
    				return null;
    			if (currentChar == '.')
    				hasDecimalPoint = true;
    		} else
    			return null;
    	}
    	return new Token(Token.Type.NUM, sb.toString());
    }
    
    /**
     * 
     * @param s - a string with length > 0
     * @return the token of type WORD that is encoded by that string, or null if no such token exists
     * 
     */ 
    private Token getStringToken(String s) {
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < s.length(); i++) {
    		char currentChar = s.charAt(i);
    		if (isLetterChar(currentChar))
    			sb.append(currentChar);
    		else
    			return null;
    	}
    	return new Token(Token.Type.WORD, sb.toString());    }
    
    /**
     * 
     * @param c - a non-null char
     * @return true if that character is 0-9 or .
     * 	false otherwise
     */
    private boolean isNumChar(char c) {
    	return ((c >= '0' && c <= '9') || c == '.');
    }
    
    /**
     * 
     * @param c - a non-null char
     * @return true if that character is a-z, A-Z; false otherwise
     */
    private boolean isLetterChar(char c) {
    	return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
    }
    
    
    /**
     * @param input - a String
     * @return an altered version of the same string that:
     * - has no leading spaces
     * - has no trailing spaces
     * - has no consecutive spaces
     * the returned string also has an inserted single space anywhere that doesn't change
     * the logic.  That is, between any two symbols (plus, mult, parens) or between any symbol and
     * an alphanumeric.
     */
    public String cleanString(String input) {
    	String result = input;
    	result = trimDuplicates(result);
    	result = trimBeginning(result);
    	result = removeTrailingSpace(result);
    	result = putSpacesIn(result);
    	return result;
    }
    
    /**
     * 
     * @param input - a string
     * @return  has an inserted single space anywhere that doesn't change the logic.
     * That is, between any two symbols (plus, mult, parens) or between any symbol and
     * an alphanumeric.
     */
    public String putSpacesIn(String input) {
    	if (input.length() < 2)
    		return input;
    	StringBuilder sb = new StringBuilder();
    	sb.append(input.charAt(0));
    	char lastChar = input.charAt(0);
    	for (int i = 1; i < input.length(); i ++) {
    		char currentChar = input.charAt(i);
    		if (shouldPutSpace(lastChar, currentChar)) {
    			sb.append(' ');
    		}
    		sb.append(currentChar);
    		lastChar = currentChar;
    	}
    	return sb.toString();
    }
    
    
    /**
     * 
     * @param a - a char
     * @param b - a char
     * @return true if those chars can have a space between them, as per the spec of putSpacesIn()
     * false otherwise
     */
    private boolean shouldPutSpace(char a, char b) {
    	if (a == ' ' || b == ' ')
    		return false;
    	if ((isNumChar(a) || isLetterChar(a)) && (isNumChar(b) || isLetterChar(b)))
    		return false;
    	else
    		return true;
    }
    
    /**
     * 
     * @param input - a String
     * @return a string identical to input, except all instances of multiple spaces in a row have been
     * condensed to single space
     */
    public String trimDuplicates(String input) {
    	boolean lastCharWasSpace = false;
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < input.length(); i++) {
    		char currentChar = input.charAt(i);
    		if (!lastCharWasSpace || currentChar != ' ')
    			sb.append(currentChar);
    		lastCharWasSpace = (currentChar == ' ');
    	}
    	return sb.toString();
    }
    
    /**
     * 
     * @param input - a String
     * @return a string identical to input, except all leading spaces have been removed
     */
    private String trimBeginning(String input) {
    	boolean hasHitChar = false;
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < input.length(); i++) {
    		char currentChar = input.charAt(i);
    		if (hasHitChar || currentChar != ' ')
    			sb.append(currentChar);
    		if (currentChar != ' ')
    			hasHitChar = true;
    	}
    	return sb.toString();
    }
    
    /**
     * 
     * @param input
     * @return a string identical to input, except the last character is truncated if it is a space.
     */
    private String removeTrailingSpace(String input) {
    	int len = input.length();
    	if (len == 0)
    		return input;
    	else if (input.charAt(len-1) == ' '){
    		return input.substring(0, len-1);
    	} else
    		return input;
    }
}
