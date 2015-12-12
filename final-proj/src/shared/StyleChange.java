package shared;

/**
 * This class represents a style change within a document.  It specifies
 * the type of style change, whether it's toggling that change on or off,
 * the position at which that change begins, and the number of subsequenct
 * characters to which that change applies
 * @author dmendels
 *
 */
public class StyleChange implements Change {
	private StyleType styleType;
	private Integer position;
	private Integer numChars;
	private Boolean isEnabling;
	
	/**
	 * Constructor for StyleChange
	 * @param styleType - the StyleType of the change in question
	 * @param isEnabling - true if enabling that style, false if disabling it
	 * @param position - position at which style change begins
	 * @param numChars - the number of subsequent characters (including the first) to which this change applies
	 */
	public StyleChange(StyleType styleType, Boolean isEnabling, Integer position, Integer numChars) {
		this.styleType = styleType;
		this.position = position;
		this.numChars = numChars;
		this.isEnabling = isEnabling;
	}
	
	/**
	 * This enum specifies the supported Style types
	 * @author dmendels
	 */
	public static enum StyleType {
		BOLD,
		ITALIC,
		UNDERLINE;
	}
	
	/**
	 * Get-method for position
	 * @return the position at which this style change starts
	 */
	public Integer getPosition() {
		return position;
	}
	
	/**
	 * Get-method for numChars
	 * @return the number of characters that this change applies to
	 */
	public Integer getNumChars() {
		return numChars;
	}
	
	/**
	 * Get-method for style type
	 * @return the StyleType that this change involves
	 */
	public StyleType getStyleType() {
		return styleType;
	}
	
	/**
	 * Get-method for isEnabling
	 * @return true if styleType is being enabled, false if it is being disabled
	 */
	public boolean isEnabling() {
		return isEnabling;
	}
}
