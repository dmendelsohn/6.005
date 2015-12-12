package shared;

/**
 * ChangeRequest is a JSON object which is used to send requests across the HTTP channel.
 * 
 * We use Google's Gson package to convert the JSON object into a string representation before sending,
 * and also to convert received strings back into JSON objects for field parsing.
 * 
 * This object stores the requested operation type, the document ID to modify, the client requesting the change,
 * the client's last seen versionID, the text to insert (if insert), and the title of the document edited (if doc change)
 * 
 * @author dmendels
 *
 */
public class ChangeRequest {
	private OperationType operation;
	private Integer docID;
	private Integer clientID;
	private Integer versionID;
	private Change change;
	
	/**
	 * Enum to list the possible types of operations which can be sent across the channel.
	 */
	public static enum OperationType {
		NEWDOC,
		INSERT,
		DELETE,
		STYLECHANGE;
	}
	
	/**
	 * Constructor for a new JSON request object
	 * @param operation The type of operation
	 * @param docID Integer referring to the document which needs to be modified
	 * @param clientID Integer referring to the client sending the modification
	 * @param versionID Integer of this client's last seen versionID
	 * @param position Integer of the position at which to make the edit.
	 * @param text String representing text to insert (can be null if non-insert)
	 * @param title String title of document to modify (can be null if not document modification)
	 */
	public ChangeRequest(OperationType operation,
							Integer docID,
							Integer clientID,
							Integer versionID,
							Change change) {
		this.operation = operation;
		this.versionID = versionID;
		this.change = change;
		this.docID = docID;
		this.clientID = clientID;
	}
	
	/**
	 * Static method to convert a string representing an operation into a proper ENUM type.
	 * @param opString String to convert to ENUM type
	 * @return ENUM type
	 * @throws Exception when string is invalid / does not refer to a type
	 */
	public static OperationType convertStringToOperationType(String opString) {
		if (opString.equals("insert")) {
			return OperationType.INSERT;
		} else if (opString.equals("delete")) {
			return OperationType.DELETE;
		} else if (opString.equals("newdoc")) {
			return OperationType.NEWDOC;
		} else {
			throw new RuntimeException("Invalid operation string");
		}
	}
	
	/**
	 * Get-method for operation type
	 * @return Instance of operation
	 */
	public OperationType getOperationType() {
		return operation;
	}
	
	/**
	 * Get-method for DocID
	 * @return Integer of docID for this JSON object
	 */
	public Integer getDocID() {
		return docID;
	}
	
	/**
	 * Get-method for VersionID
	 * @return Integer of versionID associated with this JSON
	 */
	public Integer getVersionID() {
		return versionID;
	}
	
	/**
	 * Get-method for the Change object of this change request
	 * @return change field of this object
	 */
	public Change getChange() {
		return change;
	}
	
	/**
	 * Get-method for clientID
	 * @return Integer for client's ID associated with this JSON
	 */
	public Integer getClientID() {
		return clientID;
	}
}
