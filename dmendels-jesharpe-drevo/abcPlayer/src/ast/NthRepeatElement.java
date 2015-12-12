package ast;

import player.Token;

public class NthRepeatElement implements HighElement {
	/**
	 * Used to indicate Nth repeats as per grammar
	 */
	private String index; //"1" or "2"
	
	/**
	 * Constructs NthRepeatElement from Token
	 * @param Token t
	 */
	public NthRepeatElement(Token t) {
		switch(t.getType()) {
		case NTH_REPEAT:
			index = t.getValue();
			break;
		default:
			throw new RuntimeException("NthRepeatElement must be initialized with token of type NTH_REPEAT");
	}
	}
	
	/**
	 * Indicates which nth repeat it is; 1 or 2
	 * @return String index
	 */
	public String getIndex() {
		return index;
	}
	
	/**
	 * 
	 * @return index as int
	 */
	public int getIndexAsInt() {
		return Integer.parseInt(index);
	}

	/**
	 * Checks Equality
	 * @return true if other object is Nth repeat of same index
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NthRepeatElement))
			return false;
		NthRepeatElement that = (NthRepeatElement)obj;
		return this.getIndex().equals(that.getIndex());
	}

}
