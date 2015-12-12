package server;

import shared.ChangeRequest;

/**
 * This is a helper class that compresses the "change" field of a ChangeRequest
 * into a String (using JSON compression).  This is necessary because ChangeRequest
 * isn't sufficient on its own.  When being compressed into JSON, we lose the information
 * about the type of ChangeRequest's change field (which is very important), and that disallows
 * casting.  That means we need to do separate JSON compression of the Change field (done in Utils.java)
 * and rebuild the Change subclasses using that compression (also done in Utils.java).
 * @author danielmendelsohn
 *
 */
public class ChangeRequestJsonHelper {
	private ChangeRequest.OperationType operation;
	private Integer docID;
	private Integer versionID;
	private Integer clientID;
	private String change;
	
	/**
	 * Constructor for ChangeRequestJsonHelper
	 * @param operation - the operation type of this change
	 * @param docID - the ID of the document being changed
	 * @param versionID - the version of this change (or in the client to server case,
	 * its the version of the most recently processed changed)
	 * @param clientID - the clientID of the client who made this change
	 * @param jsonChangeString - the String that represents the compressed form of a Change object
	 */
	public ChangeRequestJsonHelper(ChangeRequest.OperationType operation,
										Integer docID, Integer versionID,
										Integer clientID, String jsonChangeString) {
		this.operation = operation;
		this.docID = docID;
		this.versionID = versionID;
		this.clientID = clientID;
		this.change = jsonChangeString;
	}
	
	/**
	 * Get-method for operation
	 * @return the type of the operation that this change performs
	 */
	public ChangeRequest.OperationType getOperationType() {
		return operation;
	}
	
	/**
	 * Get-method for docID
	 * @return the ID of the document being changed.  Null in the case of a new doc reqeust from the client to the server
	 */
	public Integer getDocID() {
		return docID;
	}
	
	/**
	 * Get-method for clientID
	 * @return the ID of the client that made this change
	 */
	public Integer getClientID() {
		return clientID;
	}
	
	/**
	 * Get-method for versionID
	 * @return the versionID associated with this edit
	 */
	public Integer getVersionID() {
		return versionID;
	}
	
	/**
	 * Get-method for change String
	 * @return the String representing a JSON-compressed Change object
	 */
	public String getJsonChangeString() {
		return change;
	}
}
