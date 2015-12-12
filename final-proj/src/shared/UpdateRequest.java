package shared;

/**
 * This is a JSON object for messages sent by the client requesting an update from the server.
 * The client sends its last seen versionID along with its own clientID so that the server
 * can send it the appropriate unseen updates.
 * 
 * @author dmendels
 *
 */
public class UpdateRequest {
	private Integer clientID;
	private Integer versionID;
	
	/**
	 * Creates a new instance of UpdateRequest with a specified clientID and a versionID
	 * @param clientID Integer representing the unique client ID of the client sending a request
	 * @param versionID Integer representing the last seen version of documents by the client
	 */
	public UpdateRequest(Integer clientID, Integer versionID) {
		this.clientID = clientID;
		this.versionID = versionID;
	}
	
	/**
	 * Returns client ID.
	 * @return Integer representing clientID stored in instance of UpdateRequest
	 */
	public Integer getClientID() {
		return clientID;
	}
	
	/**
	 * Returns version ID.
	 * @return Integer representing versionID stored in instance of UpdateRequest
	 */
	public Integer getVersionID() {
		return versionID;
	}
}
