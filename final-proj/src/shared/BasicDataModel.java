package shared;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


/**
 * Parent data model for all other data models. Implements the DataModel interface. 
 * See DataModel interface for specifications.
 * Used as a superclass for all other data models (server, verified client, and client)
 * 
 * @author dmendels
 */
public class BasicDataModel implements DataModel {
    protected HashMap<Integer, Pair<String, StyledDocument>> docTable;
    protected ArrayList<ChangeRequest> log;
    
    /**
     * Creates a new instance of BasicDataModel
     */
    public BasicDataModel() {
        docTable = new HashMap<Integer, Pair<String, StyledDocument>>();
        log = new ArrayList<ChangeRequest>();
    }
	
	
	@Override
	public Pair<String, StyledDocument> getPair(Integer docID) {
		return docTable.get(docID);
	}

	@Override
	public StyledDocument getDoc(Integer docID) {
		if (docTable.containsKey(docID))
			return docTable.get(docID).getSecond();
		else
			return null;
	}

	@Override
	public String getTitle(Integer docID) {
    	if (docTable.containsKey(docID))
    		return docTable.get(docID).getFirst();
    	else
    		return null;
	}

	@Override
	public void newDoc(Integer docID, String title, Integer clientID) {
		docTable.put(docID, new Pair<String, StyledDocument>(title, new DefaultStyledDocument()));
	}

	@Override
	public void insert(Integer docID, Integer position, String text, Integer clientID,
						boolean isBold, boolean isItalic, boolean isUnderline) throws BadLocationException {
		if (!docTable.containsKey(docID)) {
			return;
		} else {
			SimpleAttributeSet sas = new SimpleAttributeSet();
			sas.addAttribute(StyleConstants.CharacterConstants.Bold, isBold);
			sas.addAttribute(StyleConstants.CharacterConstants.Italic, isItalic);
			sas.addAttribute(StyleConstants.CharacterConstants.Underline, isUnderline);
			docTable.get(docID).getSecond().insertString(position, text, sas);
		}
	}

	@Override
	public void delete(Integer docID, Integer position, Integer numChars, Integer clientID) throws BadLocationException {
		if (!docTable.containsKey(docID)) {
			return;
		} else {
			docTable.get(docID).getSecond().remove(position, numChars);
		}
	}

	@Override
	public boolean hasDoc(Integer docID) {
		return docTable.containsKey(docID);
	}

	@Override
	public ArrayList<ChangeRequest> getLog() {
		return log;
	}
	
	/**
	 * Returns the plain text of the specified doc
	 * @param docID
	 * @return
	 */
	public String getPlainTextDoc(Integer docID) {
		StyledDocument doc = docTable.get(docID).getSecond();
		try {
			return doc.getText(0, doc.getLength());
		} catch (BadLocationException e) {
			return "error";
		}
	}

}
