package ast;

import java.util.ArrayList;
import java.util.List;


public class Voice {
	/**
	 * Each voice has a list of HighElements (Measures and Barlines and NthRepeats).
	 * 
	 * Also a String name, this is used to identify the voice. 
	 */
	private List<HighElement> elements = new ArrayList<HighElement>();
	private String name;
	
	/**
	 * Voice constructor
	 * 
	 * @param n, the name of the voice
	 */
	public Voice(String n) {
		this.name = n;
	}
	
	/**
	 * Adds a HighElement to this voice's playlist
	 * 
	 * @param e
	 */
	public void addElement(HighElement e) {
		elements.add(e);
	}
	
	public String getName() {
		return name;
	}
	
	public List<HighElement> getElements() {
		return elements;
	}

	/**
	 * Recurses down a level to Elements to determine equality of 
	 * these two Voices
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Voice))
			return false;
		Voice that = (Voice)obj;
		
		if (!this.getName().equals(that.getName()))
			return false;
		if (this.getElements().size() != that.getElements().size())
			return false;
		for (int i = 0; i < this.getElements().size(); i++) {
			if (!this.getElements().get(i).equals(that.getElements().get(i)))
				return false;
		}
		return true;
	}
}
