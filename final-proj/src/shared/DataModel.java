package shared;

import java.util.ArrayList;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;


/**
 * Interface for the basic model of data stored in clients and server. 
 * The DataModel interface contains get- and set- methods for the data stored in Client and Server.
 * 
 * @author jcotler
 * 
 */
public interface DataModel {

    /**
     * Returns a pair of String and StyledDocument representing the title and content of a document with a particular ID.
     * @param docID Integer representing the document's unique ID in the data model
     * @return Pair<String, StyledDocument> representing the document
     */
    public Pair<String, StyledDocument> getPair(Integer docID);
    
    /**\
     * Returns the StyledDocument of the document with a particular document ID.
     * @param docID Integer representing document ID
     * @return StyledDocument with this ID
     */
    public StyledDocument getDoc(Integer docID);
    
    /**
     * Returns the title of the document with a particular document ID.
     * @param docID Integer representing the document ID
     * @return String representing the title of the document with this ID
     */
    public String getTitle(Integer docID);
    
    /**
     * Adds a document to the DataModel if the document ID is new.
     * Replaces document in DataModel if the document ID already exists.  
     * @param docID Integer ID of document to add
     * @param title String title of document
     * @param value StyledDocument content of document
     */
    public void newDoc(Integer docID, String title, Integer clientID);
    
    /**
     * Operation inserting text into document at certain position.
     * @param docID Integer ID of document to insert text into
     * @param position Integer position of index to begin insertion of text. Must be >= 0 and < document length.
     * @param text String text to insert
     * @param clientID Integer ID of client requesting insertion of text.
     * @param isBold - indicates inserted text should be bold
     * @param isItalic - indicates inserted text should be italic
     * @param isUnderline - indicates inserted text should be underline
     * @throws BadLocationException
     */
    public void insert(Integer docID, Integer position, String text, Integer clientID, boolean isBold, boolean isItalic, boolean isUnderline) throws BadLocationException;
    
    /**
     * Operation deleting text from document at certain position
     * @param docID Integer ID of document to delete text from
     * @param position Integer position of index to begin deletion of text. Must be >=0 and < document length.
     * @param numChars Integer number of characters to delete starting at this position
     * @param clientID Integer ID of client requesting deletion of text.
     * @throws BadLocationException
     */
    public void delete(Integer docID, Integer position, Integer numChars, Integer clientID) throws BadLocationException;
        
    /**
     * Returns whether DataModel has document with certain ID
     * @param docID Integer representing document ID
     * @return True if DataModel has document with ID docID, false otherwise
     */
    public boolean hasDoc(Integer docID);
    
    /**
     * Returns a list of changes made to the DataModel in the order they were made
     * @return ArrayList<ChangeRequest> representing history of all changes made to the DataModel
     */
    public ArrayList<ChangeRequest> getLog();
    
}
