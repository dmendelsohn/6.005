package shared;

/**
 *  JSON object for when a server responds to a client connect.  Acknowledges the connection.
 *  This JSON object stores and transmits to the client the new clientID 
 *  assigned to that particular client upon connection.
 * 
 * @author dmendels
 *
 */
public class ConnectResponse {
	private Integer clientID;
	
	/**
	 * Creates a new instance of ConnectResponse with a client ID
	 * @param clientID Integer new client ID of client
	 */
	public ConnectResponse(Integer clientID) {
		this.clientID = clientID;
	}
	
	/**
	 * Returns client ID
	 * @return Integer representing clientID stored in instance of ConnectResponse
	 */
	public Integer getClientID() {
		return clientID;
	}
}
