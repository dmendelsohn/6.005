package client;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import shared.BasicDataModel;
import shared.Pair;


/**
 * Model part of the MVC pattern used to represent the client.
 * Directly backs the GUI.
 * Is a subclass of BasicDataModel.
 * Contains all of the documents editable from or visible on the graphical user interface.
 * 
 * @author jcotler
 * 
 */
public class ClientDataModel extends BasicDataModel {
    private ClientGUI gui;
    
    /**
     * Creates a new instance of ClientDataModel linked with a ClientGUI
     * @param gui ClientGUI that ClientDataModel is a model for
     */
    public ClientDataModel(ClientGUI gui) {
    	super();
        this.gui = gui;
    }
    
    /**
     * Used to update the data model based on changes made in the GUI.
     * @param docID Integer document ID of document to update
     * @param title String title of document to update
     * @param value StyledDocument is the document to replace the existing document with
     */
    public void replaceDoc(Integer docID, String title, StyledDocument value) {
    	docTable.put(docID, new Pair<String, StyledDocument>(title, value));
    	gui.updateGUI(docID);
    }
    
    @Override
	public void newDoc(Integer docID, String title, Integer clientID) {
        super.newDoc(docID, title, clientID);
        gui.updateGUI(docID);
    }

	@Override
	public void insert(Integer docID, Integer position, String text, Integer clientID,
							boolean isBold, boolean isItalic, boolean isUnderline) throws BadLocationException {
		super.insert(docID, position, text, clientID, isBold, isItalic, isUnderline);
		gui.updateGUI(docID);
	}

	@Override
	public void delete(Integer docID, Integer position, Integer numChars, Integer clientID) throws BadLocationException {
		super.delete(docID, position, numChars, clientID);
		gui.updateGUI(docID);
	}

}
