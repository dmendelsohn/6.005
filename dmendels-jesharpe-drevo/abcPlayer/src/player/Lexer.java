package player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	// Holds text in header and body, respectively
	private String abcBodyText;
	private String abcHeaderText;
	
	// counter for progress lexing body and header, respectively
	private int i = 0;
	private int j = 0;
	
	// Matchers for performing regex
    private Matcher matcher;
    private Matcher headerMatcher;
    
    // Regex matching the next token.
    private static final Pattern BODY_REGEX 
        = Pattern.compile (
             "^"  // anchors to the beginning of the match start, so that we don't skip any characters
        	 + "(MMMMMMMM)" // dummy value
        	 + "|"
        	 + "([A-Ga-g])" // BASENOTE
        	 + "|"
             + "(,+|'+)"  // OCTAVE
             + "|"
             + "(\\^\\^|\\^|__|_|\\=)" // ACCIDENTAL
             + "|"
             + "(z)" // REST
             + "|"
             + "(\\d*\\/\\d*)" // DURATION
             + "|"
             + "(\\d+)" // DURATION
             + "|"
             + "(\\|\\||\\|\\]|:\\||\\|:|\\|)" // BAR
             + "|"
             + "(\\([2-4])" // TUPLE_START
             + "|"
             + "(\\[1|\\[2)" // NTH_REPEAT
             + "|"
             + "V:\\s*(.+?)\n" // voice_body
             + "|"
             + "(\\[)" // CHORD_START
             + "|"
             + "(\\])" // CHORD_END
             + "|"
             + "%:\\s*(.+?)\n" // COMMENT
             + "|"
             + "(MMMMMMMM)" // dummy value
          );
   
    // The token types for each of the parenthesized patterns in TOKEN_REGEX.
    private static final Token.Type[] BODY_TOKENS
        = { 
    		null,
            Token.Type.BASENOTE,
            Token.Type.OCTAVE,
            Token.Type.ACCIDENTAL,
            Token.Type.REST,
            Token.Type.DURATION,
            Token.Type.DURATION,
            Token.Type.BAR,
            Token.Type.TUPLE_START,
            Token.Type.NTH_REPEAT,
            Token.Type.VOICE_BODY,
            Token.Type.CHORD_START,
            Token.Type.CHORD_END,
            Token.Type.COMMENT
          };
    
    private static final Pattern HEADER_REGEXES 
    = Pattern.compile (
         "^"  // anchors to the beginning of the match start, so that we don't skip any characters
    	 + "X:\\s*(\\d+)\n" // Index
    	 + "|"
         + "T:\\s*(.+?)\n"  // title
         + "|"
         + "C:\\s*(.+?)\n" // composer
         + "|"
         + "L:\\s*(\\d+\\/\\d+)\n" // length
         + "|"
         + "M:\\s*(C|C\\||\\d+\\/\\d+)\n" // meter
         + "|"
         + "Q:\\s*(\\d+)\n" // tempo
         + "|"
         + "V:\\s*(.+?)\n" // voice
         + "|"
         + "K:\\s*([A-Ga-g][#|b]?m?)\n" // key
         + "|"
         + "(MMMMMMMM)" // dummy value
      );
    
    // The token types for each of the parenthesized patterns in TOKEN_REGEX.
    private static final Token.Type[] HEADER_TOKENS
        = { 
            Token.Type.INDEX,
            Token.Type.TITLE,
            Token.Type.COMPOSER,
            Token.Type.LENGTH,
            Token.Type.METER,
            Token.Type.TEMPO,
            Token.Type.VOICE,
            Token.Type.KEY
          };
	
    /**
     * Processes ABC file stream and keeps a resulting list of in order Tokens
     * 
     * @param filename
     * @throws SyntaxErrorException
     */
	public Lexer(String filename) throws SyntaxErrorException {
		// Read the file into a string
		try {
			this.abcBodyText = readFileIntoString(filename).trim();
		} catch (IOException e) {
			throw new RuntimeException("Could not read ABC file.");
		}
		
		// split into header and body
		Pattern keyRegex = Pattern.compile("K:(.+?)\n");
		Matcher m = keyRegex.matcher(this.abcBodyText);
		if (m.find()) {
			int end = m.end();
			this.abcHeaderText = this.abcBodyText.substring(0, end);
			this.abcBodyText = this.abcBodyText.substring(end).replaceAll(" ", "").trim();
		} else {
			throw new RuntimeException("Could not find a key in header!");
		}
		
		// Apply the pattern to the matcher
		this.matcher = BODY_REGEX.matcher(abcBodyText);
		this.headerMatcher = HEADER_REGEXES.matcher(abcHeaderText);
	}
	
	/**
	 * Helper method for sanitizing header token values
	 * 
	 * @param headString
	 * @return cleaned String
	 */
	public String cleanHeaderInputString(String headString) {
		return headString.substring(2).trim();
	}
	
	/**
	 * Helper method for sanitizing header token values
	 * 
	 * @param s
	 * @return first character removed
	 */
	public String removeFirstChar(String s) {
		return s.substring(1).trim();
	}
	
	/**
	 * next() Token method for processing and lexing the Header
	 * 
	 * @return new Token found
	 * @throws SyntaxErrorException
	 */
	public Token nextHeader() throws SyntaxErrorException {
        if (i >= abcHeaderText.length()) {
            return new Token(Token.Type.EOH, "");            
        }
        
        // Look for the next token
        if (!headerMatcher.find(i)) {
            // No token found
            throw new SyntaxErrorException("syntax error at " + abcHeaderText.substring(i));
        }
        
        // Get the part of the string that the regex matched,
        // and advance our state
        String value = headerMatcher.group(0);
        i = headerMatcher.end();
        
        // Each set of parentheses in TOKEN_REGEX is a "capturing group", which
        // means that the regex matcher remembers where it matched and returns it
        // with the method group(i), where i=1 is the first set of parens.
        // Only one of the groups can match, so look for a non-null group.
        for (int i = 1; i < headerMatcher.groupCount(); ++i) {
            if (headerMatcher.group(i) != null) {
                // since i is 1-based, use i-1 to find the token type for this pattern
                return new Token(HEADER_TOKENS[i-1], cleanHeaderInputString(value)); 
            }
        }
        
        // if we got here, there's a bug in the regex -- Matcher said we matched the 
        // expression, but it didn't match any of the parens
        throw new AssertionError("shouldn't get here");
    }
	
	/**
	 * next() method for returning token at current pointer in the input String
	 * 
	 * @return new Token
	 * @throws SyntaxErrorException
	 */
	public Token next() throws SyntaxErrorException {
        if (j >= abcBodyText.length()) {
            return new Token(Token.Type.EOF, "");            
        }
        
        // Look for the next token
        if (!matcher.find(j)) {
            // No token found
            throw new SyntaxErrorException("syntax error at " + abcBodyText.substring(j));
        }
        
        // Get the part of the string that the regex matched,
        // and advance our state
        String value = matcher.group(0);
        j = matcher.end();
        
        // Each set of parentheses in TOKEN_REGEX is a "capturing group", which
        // means that the regex matcher remembers where it matched and returns it
        // with the method group(i), where i=1 is the first set of parens.
        // Only one of the groups can match, so look for a non-null group.
        for (int i = 1; i < matcher.groupCount(); ++i) {
            if (matcher.group(i) != null) {
                // since i is 1-based, use i-1 to find the token type for this pattern
            	if (BODY_TOKENS[i-1] == Token.Type.VOICE_BODY)
            		value = cleanHeaderInputString(value);
            	else if (BODY_TOKENS[i-1] == Token.Type.NTH_REPEAT || 
            			BODY_TOKENS[i-1] == Token.Type.TUPLE_START)
            		value = removeFirstChar(value);
            	else if (BODY_TOKENS[i-1] == Token.Type.COMMENT)
            		return this.next();
                return new Token(BODY_TOKENS[i-1], value); 
            }
        }
        
        // if we got here, there's a bug in the regex -- Matcher said we matched the 
        // expression, but it didn't match any of the parens
        throw new AssertionError("shouldn't get here");
    }
	
	/**
	 * Reads a text file into a String
	 * Source:
	 * 	http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
	 * 
	 * @param: filename
	 */
	public String readFileIntoString(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader (filename));
	    String line = null;
	    StringBuilder sb = new StringBuilder();
	    String ls = System.getProperty("line.separator");

	    while((line = reader.readLine()) != null ) {
	        sb.append(line);
	        sb.append(ls);
	    }

	    return sb.toString();
	}
	
	/**
	 * Getter method for abcBodyText
	 * @return body String text
	 */
	public String getText() {
		return this.abcBodyText + "";
	}
}
