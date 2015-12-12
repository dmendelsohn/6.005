package player;

public class Token {
	/**
	 * Fields for token:
	 * - value is the literal string of the Token foudn in the ABC file
	 * - type is an enum
	 */
	private final Type type;
	private final String value;
	
	/**
	 * Token types, used by Lexer and handed to parser for AST creation
	 * 
	 * For both Header and Body elements
	 */
	public enum Type {
		// body tokens
		OCTAVE,
		BASENOTE,
		ACCIDENTAL,
		REST,
		DURATION,
		BAR,
		NTH_REPEAT,
		TUPLE_START,
		CHORD_START,
		CHORD_END,
		VOICE_BODY,
		COMMENT,
		EOF,
		
		// header tokens
		INDEX,
        TITLE,
        COMPOSER,
        LENGTH,
        METER,
        TEMPO,
        VOICE,
        KEY,
        EOH
	}
	
	/**
	 * Creates a new Token. Token must have non-null String value and
	 * non-null type enum
	 * 
	 * @param t
	 * @param val
	 */
	public Token(Type t, String val) {
		if (t == null || val == null) {
			throw new RuntimeException("Cannot instantiate token with null value or type.");
		}
		
		this.type = t;
		this.value = val;
	}
	
	public Type getType(){
		return type;
	}

	public String getValue(){
		return value;
	}
	
	/**
	 * Returns String representation of this Token
	 */
	public String toString() {
		return "Type = " + getType() + ", Value = " + getValue();
	}
	
	/**
	 * Returns true if type and String value are both equivielent. 
	 * 
	 * @param that
	 * @return
	 */
	public boolean equals(Token that) {
		if (!(that instanceof Token))
			return false;
		return this.getType().equals(((Token) that).getType()) 
			&& this.getValue().equals(((Token) that).getValue());
	}
}
