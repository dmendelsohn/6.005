package shared;

/**
 * This class a insertion the creation of a new document.  It specifies
 * the title of that document
 * @author dmendels
 *
 */
public class NewDoc implements Change {
	private String title;
	
	/**
	 * Constructor for NewDoc
	 * @param title - the title of the new document
	 */
	public NewDoc(String title) {
		this.title = title;
	}
	
	/**
	 * Get-method for title
	 * @return the title of the new document
	 */
	public String getTitle() {
		return title;
	}

}
