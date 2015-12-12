package ast;

import java.util.ArrayList;
import java.util.List;

public class TupletElement implements Element{

    private int degree;
    private List<SyncElement> noteElements;
    
    /**
     * Creates new TupletElement of correct length from list of notes
     * @param noteElemList
     */
    public TupletElement(List<SyncElement> noteElemList){
    	degree = noteElemList.size();
        noteElements = new ArrayList<SyncElement>(noteElemList);
    }
    
    /**
     * 
     * @return notes in Tuplet
     */
    public List<SyncElement> getNoteElements() {
    	return new ArrayList<SyncElement>(noteElements);
    }
    
    /**
     * Distinguishes duplet, triplet, quadruplet
     * @return int representing type of tuplet
     */
    public int getDegree() {
    	return degree;
    }
    
    /**
     * Checks equality with other object
     * @param Object obj
     * @return true if obj is tuplet with identical Elements
     */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TupletElement))
			return false;
		TupletElement that = (TupletElement)obj;
		if (this.getNoteElements().size() != that.getNoteElements().size())
			return false;
		for (int i = 0; i < this.getNoteElements().size(); i++)
			if (!this.getNoteElements().get(i).equals(that.getNoteElements().get(i)))
				return false;
		return true;
	}
	
}
