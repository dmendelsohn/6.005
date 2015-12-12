package player;

import java.util.ArrayList;
import java.util.List;

import sound.PlayableNote;
/**
 * Struct for holding List<PlayableNote> and a number of ticks in 
 * creating sequences of notes during the Evaluation process
 * 
 */
public class ListIntegerPair {
	private List<PlayableNote> notes;
	private int numTicks;
	
	public ListIntegerPair(List<PlayableNote> notes, int numTicks) {
		this.notes = new ArrayList<PlayableNote>(notes);
		this.numTicks = numTicks;
	}
	
	public List<PlayableNote> getList() {
		return new ArrayList<PlayableNote>(notes);
	}
	
	public int getNumTicks() {
		return numTicks;
	}
	
}
