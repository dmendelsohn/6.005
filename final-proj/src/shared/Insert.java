package shared;

/**
 * This class represents a insertion within a document.  It specifies
 * the text to be inserted and at what position.  It also specifies
 * if that text should be bold and/or underlined and/or italic
 * @author dmendels
 *
 */
public class Insert implements Change {
	private Integer position;
	private String text;
	private boolean isBold;
	private boolean isUnderline;
	private boolean isItalic;
	
	/**
	 * Constructor for Insert
	 * @param position - the position at which to insert
	 * @param text - the text to insert at that position
	 * @param isBold - true if the text is to be bold
	 * @param isItalic - true if the text is to be italic
	 * @param isUnderline - true if the text is to be underlined
	 */
	public Insert(Integer position, String text, boolean isBold, boolean isItalic, boolean isUnderline) {
		this.position = position;
		this.text = text;
		this.isBold = isBold;
		this.isItalic = isItalic;
		this.isUnderline = isUnderline;
	}
	
	/**
	 * Get-method for position
	 * @return the position at which the insert is to occur
	 */
	public Integer getPosition(){
		return position;
	}
	
	/**
	 * Get-method for text
	 * @return the inserted text
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Get-method for isBold
	 * @return true iff the inserted text is bold
	 */
	public boolean isBold() {
		return isBold;
	}
	
	/**
	 * Get-method for isItalic
	 * @return true iff the inserted text is italic
	 */
	public boolean isItalic() {
		return isItalic;
	}
	
	/**
	 * Get-method for isUnderline
	 * @return true iff the inserted text is underlined
	 */
	public boolean isUnderline() {
		return isUnderline;
	}
}
