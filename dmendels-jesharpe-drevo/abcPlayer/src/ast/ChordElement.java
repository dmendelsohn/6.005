package ast;

import java.util.ArrayList;
import java.util.List;

import player.Evaluator;
import player.Fraction;

public class ChordElement implements SyncElement {
	private List<NoteElement> notes;
	
	/**
	 * Construct ChordElement from list of notes it will contain
	 * @param noteList
	 */
	public ChordElement(List<NoteElement> noteList) {
		notes = new ArrayList<NoteElement>(noteList);
	}
	
	/**
	 * 
	 * @return notes in Chord
	 */
	public List<NoteElement> getNotes() {
		return new ArrayList<NoteElement>(notes);
	}
	
	/**
	 * @return notes in Chord
	 */
	@Override
	public List<NoteElement> expand() {
		return new ArrayList<NoteElement>(notes);
	}

	/**
	 * @return duration of longest note in chord
	 */
	@Override
	public Fraction getDuration() {
		Fraction maxFrac = new Fraction("0/1"); //signifies 0 length
		for (NoteElement note: notes) {
			if (note.getDuration().compareTo(maxFrac) > 0)
				maxFrac = note.getDuration();
		}
		return maxFrac;
	}

	/**
	 * test equality
	 * @param Object obj
	 * @return true if obj is chord with equal elements
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ChordElement))
			return false;
		ChordElement that = (ChordElement)obj;
		if (this.getNotes().size() != that.getNotes().size())
			return false;
		for (int i = 0; i < this.getNotes().size(); i++)
			if (!this.getNotes().get(i).equals(that.getNotes().get(i)))
				return false;
		return true;
	}
	
	/**
	 * Finds the Least Common Multiple of denominators of Notes chord contains
	 * @return LCM(denominators of notes)
	 */
	@Override
	public int getLcmOfDenoms(){
		int LCM = 1;
		for (NoteElement note : getNotes()) {
			LCM = Evaluator.lcm(LCM, note.getDuration().getDenom());
		}
		return LCM;
	}

	/**
	 * Scales duration of each element by a Fraction
	 * @param Fraction scale
	 */
	@Override
	public void scaleDuration(Fraction scale) {
		for (NoteElement note : notes) {
			note.scaleDuration(scale);
		}
	}
}
