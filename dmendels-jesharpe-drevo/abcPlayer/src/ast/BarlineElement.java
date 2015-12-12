package ast;

import player.Token;

public class BarlineElement implements HighElement {
	private String barString;
	
	/**
	 * Constructs Barline from Barline Token
	 * @param Token t
	 */
	public BarlineElement(Token t) {
		switch(t.getType()) {
		case BAR:
			barString = t.getValue();
			break;
		default:
			throw new RuntimeException("BarlineElement must be initialized with token of type BAR");
	}	}
	
	/**
	 * Used to find exact type of Barline
	 * @return Barline String representation
	 */
	public String getBarString() {
		return barString;
	}
	
	/**
	 * Test equality
	 * @param Object obj
	 * @return true if other object is Barline with matching barString attribute
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BarlineElement))
			return false;
		BarlineElement that = (BarlineElement)obj;
		return this.getBarString().equals(that.getBarString());
	}

}
