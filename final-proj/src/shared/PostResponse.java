package shared;

/**
 * PostResponse is a JSON object used to send acknowledgment to the client that
 * the server received a post update.
 * 
 * @author dmendels
 *
 */
public class PostResponse {
	private String message;
	
	/**
	 * Creates a new instance of PostResponse based on a message
	 * @param message String representing the message
	 */
	public PostResponse(String message) {
		this.message = message;
	}
	
	/**
	 * Returns the PostResponse message
	 * @return String representing the message stored in instance of PostResponse
	 */
	public String getMessage() {
		return message;
	}
}
