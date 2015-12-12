package ast;

import java.util.ArrayList;
import java.util.List;

public class Measure implements HighElement {
	private List<Element> elements;
	
	/**
	 * Constructs Measure from list of elements
	 * @param elements
	 */
	public Measure(List<Element> elements) {
		this.elements = new ArrayList<Element>(elements);
	}
	
	/**
	 * 
	 * @return elements in Measure
	 */
	public List<Element> getElements() {
		return elements;
	}
	
	/**
	 * Tests equality
	 * @param Object obj
	 * @return true if obj is Measure with identical elements
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Measure))
			return false;
		Measure that = (Measure) obj;
		if (this.getElements().size() != that.getElements().size())
			return false;
		for (int i = 0; i < this.getElements().size(); i++)
			if (!this.getElements().get(i).equals(that.getElements().get(i)))
				return false;
		return true;
	}
}
