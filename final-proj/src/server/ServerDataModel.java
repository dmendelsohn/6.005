package server;

import javax.swing.text.BadLocationException;

import shared.BasicDataModel;
import shared.ChangeRequest;
import shared.Delete;
import shared.Insert;
import shared.NewDoc;


/**
 * Data model used by the server to store documents. It is only accessible by the server.
 * When the server is asked for edits, it sends this data model's log to the requesting client
 * so that the client's VerifiedClientDataModel receives the changes and can change accordingly.
 * 
 * ServerDataModel is a subclass of BasicDataModel.
 * 
 * @author jcotler
 */
public class ServerDataModel extends BasicDataModel {
	private int versionCounter;
	
	/**
	 * Creates a new instance of ServerDataModel
	 */
	public ServerDataModel() {
		super();
		versionCounter = 0;
	}
    
	@Override
    public void newDoc(Integer docID, String title, Integer clientID) {
    	super.newDoc(docID, title, clientID);
    	NewDoc nd = new NewDoc(title);
		ChangeRequest cr = new ChangeRequest(ChangeRequest.OperationType.NEWDOC, docID, clientID, ++versionCounter, nd);
		log.add(cr);
    }
    
    @Override
    public void insert(Integer docID, Integer position, String text, Integer clientID,
    					boolean isBold, boolean isItalic, boolean isUnderline) throws BadLocationException {
    	super.insert(docID, position, text, clientID, isBold, isItalic, isUnderline);
    	Insert i = new Insert(position, text, isBold, isItalic, isUnderline);
		ChangeRequest cr = new ChangeRequest(ChangeRequest.OperationType.INSERT, docID, clientID, ++versionCounter, i);
		log.add(cr);
	}
    
    @Override
    public void delete(Integer docID, Integer position, Integer numChars, Integer clientID) throws BadLocationException {
    	super.delete(docID, position, numChars, clientID);
    	Delete d = new Delete(position, numChars);
		ChangeRequest cr = new ChangeRequest(ChangeRequest.OperationType.DELETE, docID, clientID, ++versionCounter, d);
		log.add(cr);
	}
}