package ast;

import java.util.List;

import player.Fraction;

public interface SyncElement extends Element{
	/**
	 * 
	 * @return List<NoteElement> contained in SyncElement
	 */
	public List<NoteElement> expand();
	
	/**
	 * Gets the duration of SyncElement as a Fraction
	 * 	of default note length
	 * 
	 * @return duration of SyncElement
	 * 	For chords it's the longest note
	 */
	public Fraction getDuration();
	
	/**
	 * Scales the SyncElements Duration by a scalar in Fraction form
	 * @modifies Duration
	 * @param scale
	 */
	public void scaleDuration(Fraction scale);
	
	/**
	 * Finds the Least Common Multiple of the denominators
	 * 	of the Duration(s). Used for num tick calculations
	 * @return LCM(denominators of durations)
	 */
	public int getLcmOfDenoms();
}
