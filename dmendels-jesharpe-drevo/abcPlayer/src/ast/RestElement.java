package ast;

import java.util.ArrayList;
import java.util.List;

import player.Fraction;
import player.Token;

public class RestElement implements SyncElement {
	private Fraction duration;
	
	/**
	 * Constructor for RestElement, if a duration is specified
	 * @param t - a DURATION token that specifies the duration multiplier
	 */
	public RestElement(Token t) {
		switch(t.getType()) {
			case DURATION:
				String multiplier = "1";
				multiplier = t.getValue();
				duration = new Fraction(multiplier);
				break;
			default:
				throw new RuntimeException("Error while parsing rest");
		}
	}
	
	/**
	 * Constructor for default RestElement, multiplier is set to "1/1"
	 */
	public RestElement() {
		duration = new Fraction(1,1);
	}

	/**
	 * Accessor method
	 * @return Copy of the duration field
	 */
	@Override
	public Fraction getDuration() {
		return new Fraction(duration.num,duration.denom);
	}

	/**
	 * The notes in this SyncElement
	 * Since a rest is no notes, the result of this SyncElement call is an empty list
	 * @returns an empty list of NoteElement
	 */
	@Override
	public List<NoteElement> expand() {
		//return empty list
		return new ArrayList<NoteElement>();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RestElement))
			return false;
		RestElement that = (RestElement)obj;
		return that.getDuration().equals(that.getDuration());
	}
	
	/**
	 * Override method from SyncElement
	 * @return the denominator of the duration multiplier
	 */
	@Override
	public int getLcmOfDenoms() {
		return getDuration().getDenom();
	}
	
	/**
	 * Override method from SyncElement
	 * @param the scale Fraction by which to multiply the duraiton field
	 * @modifies The duration field, multiplying it by scale
	 */
	@Override
	public void scaleDuration(Fraction scale) {
		duration = duration.multiply(scale);
	}
}
