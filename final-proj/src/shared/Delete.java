package shared;

/**
 * This class represents a deletion within a document.  It specifies
 * the number of characters to be deleted and at what position
 * @author dmendels
 *
 */
public class Delete implements Change {
	private Integer position;
	private Integer numChars;
	
	/**
	 * Constructor for this object
	 * @param position Sets the position field
	 * @param numChars Sets the numChars field
	 */
	public Delete(Integer position, Integer numChars) {
		this.position = position;
		this.numChars = numChars;
	}
	
	/**
	 * Get method for position
	 * @return the position of the deletion
	 */
	public Integer getPosition(){
		return position;
	}
	
	/**
	 * Get method for numChars
	 * @return the number of characters that this deletion calls for
	 */
	public Integer getNumChars(){
		return numChars;
	}
}
