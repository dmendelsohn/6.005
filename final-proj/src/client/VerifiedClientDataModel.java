package client;

import javax.swing.text.BadLocationException;

import shared.BasicDataModel;

/**
 * The ServerVerifiedDataModel lives on the client.  It only changes due to updates
 * from the server. That way, the ServerVerifiedDataModel stays synchronized across all
 * clients at all times, because the server sends this class the same edits in the same order.
 * The behavior is deterministic.  When ServerVerifiedDataModel registers a change, it updates
 * the ClientDataModel (which backs the GUI directly).  User input can change the ClientDataModel,
 * but leaves the ServerVerifiedDataModel untouched.
 * 
 * @author dmendels
 * 
 */
public class VerifiedClientDataModel extends BasicDataModel {
    private ClientDataModel guiModel;
    private int clientID;
    
    /**
     * Creates a new instance of VerifiedClientDataModel linked with a ClientDataModel
     * @param model ClientDataModel that VerifiedClientDataModel manages.
     */
    public VerifiedClientDataModel(ClientDataModel model) {
    	super();
        guiModel = model;
    }
    
    /**
     * Set-method for client ID.
     * @param clientID int representing unique identification number of client.
     */
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }
    
    /**
     * Puts StyledDocument into data with associated docID and title and
     * calls GUI-backing data model to also make update
     * @param docID - the ID of the doc, as generated by the server
     * @param title - The title of the doc
     * @param value - the StyledDocument to be inserted
     * @modifies docList - to include new (or changed) StyledDocument
     */
    @Override
    public void newDoc(Integer docID, String title, Integer clientID) {
        guiModel.newDoc(docID, title, clientID);
    }

	@Override
	public void insert(Integer docID, Integer position, String text, Integer clientID,
						boolean isBold, boolean isItalic, boolean isUnderline) throws BadLocationException {
		super.insert(docID, position, text, clientID, isBold, isItalic, isUnderline);
	    if (clientID.intValue() != this.clientID) {
	        guiModel.insert(docID, position, text, clientID, isBold, isItalic, isUnderline);
	    }
	}

	@Override
	public void delete(Integer docID, Integer position, Integer numChars, Integer clientID) throws BadLocationException {
	    if (clientID.intValue() != this.clientID) {
	        guiModel.delete(docID, position, numChars, clientID);
	    }
	}
	
	/**
	 * This method forces all the data is VerifiedClientDataModel in the ClientDataModel,
	 * thus enforcing that synchronization is happening, even if individual inserts and deletes got mixed up
	 * It should only be called once in a while, for best user experience
	 */
	public void pushAllDocs() {
		System.out.print("Called push all docs");
		for (Integer docID : docTable.keySet()) {
			guiModel.replaceDoc(docID, getTitle(docID), getDoc(docID));
		}
	}

}
