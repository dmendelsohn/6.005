package client;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

import shared.StyleChange;


/**
 * View part of the MVC pattern
 * Client runs the main method in this class to open up a window and modify documents.
 * It is directly connected to a ClientDataModel, where it stores all of its data.
 * Changes in the ClientDataModel are reflected in ClientGUI, and vice versa. 
 * ClientGUI is the front-end bridge between user input and data modification.
 * It exists for display purposes.
 * 
 * @author jcotler
 * 
 */
public class ClientGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JScrollPane scrollPane;
    private JTextPane textPane;
    
    private String currentTitle;
    private StyledDocument currentDoc;
    private Integer currentDocID;
    
    private JButton bButton;
    private JButton iButton;
    private JButton uButton;
    
    private boolean isBold;
    private boolean isItalic;
    private boolean isUnderline;
    
    private JComboBox docSelect;
    private ArrayList<Integer> docIDList;
    private JButton newDoc;
    
    private ClientDataModel dm;
    private VerifiedClientDataModel vcdm;
    private ClientController controller;
    
    private String host;
    private int port;
    
    /**
     * Creates a new instance of ClientGUI with the first document having name "Document"
     */
    public ClientGUI() {
        initTitle("Create a new document");
        init();
    }
    
    /**
     * Creates a new instance of ClientGUI with the first document having name title
     * @param title String representing the name of the first document
     */
    public ClientGUI(String title) {
        initTitle(title);
        init();
    }
    
    /**
     * Creates a new instance of ClientGUI based on an existing ClientDataModel
     * @param dm ClientDataModel representing the client's data.
     */
    public ClientGUI(ClientDataModel dm) {
        this.dm = dm; 
        init();
    }
    
    /**
     * Initializes the title and ID of the document being worked on.
     * @param title Title of the document currently displayed. Is not null.
     */
    private void initTitle(String title) {
        this.currentTitle = title;
        this.setTitle("Realtime Collaborative Editing!");
    }
    
    /**
     * Initializes all fields and makes the graphical user interface visible.
     */
    private void init() {
        initOtherComponents();
        initHostRequest();
        initPortRequest();
        initFrame();
        initPane();
        initFormattingButtons();
        initDocSelect();
        createGUI();
    }    
    
    /**
     * Initializes ClientDataModel, ServerVerifiedDataModel, and ClientController for ClientGUI
     * The ServerVerifiedDataModel is connected to the data model that backs the GUI
     * The ClientController is connected to the server verified data model
     */
    private void initOtherComponents() {
        dm = new ClientDataModel(this);
        vcdm = new VerifiedClientDataModel(dm);
    }
    
    /**
     * Initializes JDialog for selecting server host.
     * If string entered is not a valid host, default host is used.
     */
    private void initHostRequest() {
        String s = (String)JOptionPane.showInputDialog(
                ClientGUI.this,
                "Enter host:",
                "Select Host",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "127.0.0.1");
        if ((s != null) && (!s.equals(""))) {
            host = s;
        }
    }
    
    /**
     * Initializes JDialog for selecting port to connect to server at.
     * If string entered is not a valid integer, controller is initialized at default port.
     */
    private void initPortRequest() {
        String s = (String)JOptionPane.showInputDialog(
                ClientGUI.this,
                "Enter port name:",
                "Select Port",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "4444");
        if ((s != null) && (!s.equals(""))) {
            try {
                port = Integer.parseInt(s);
                if (host != null)
                    controller = new ClientController(vcdm, host, port);
                else
                    controller = new ClientController(vcdm, port);
            } catch (NumberFormatException e) {
                if (host != null)
                    controller = new ClientController(vcdm, host);
                else
                    controller = new ClientController(vcdm);
            }
        }
        else
            if (host != null)
                controller = new ClientController(vcdm, host);
            else
                controller = new ClientController(vcdm);
    }
    
    /**
     * Initializes main JFrame for the GUI.
     */
    private void initFrame() {
        this.setSize(600, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * Initializes the JTextPane and JScrollPane in the GUI.
     * This is where document text is entered.
     */
    private void initPane() {
        textPane = new JTextPane();
        textPane.setEditable(false);
        currentDoc = textPane.getStyledDocument();
        TextPaneListener tpLis = new TextPaneListener();
        textPane.addKeyListener(tpLis);
        scrollPane = new JScrollPane(textPane);
    }
    
    /**
     * Initializes buttons for bold, italic, and underline operations.
     */
    private void initFormattingButtons() {
        bButton = new JButton("B");
        iButton = new JButton("I");
        uButton = new JButton("U");
        
        isBold = false;
        isItalic  = false;
        isUnderline = false;
        
        BoldListener bLis = new BoldListener();
        bButton.addActionListener(bLis);
        
        ItalicListener iLis = new ItalicListener();
        iButton.addActionListener(iLis);

        UnderlineListener uLis = new UnderlineListener();
        uButton.addActionListener(uLis);
    }
    
    /**
     * Initializes the DefaultComboBoxModel and JComboBox for different document selection.
     */
    private void initDocSelect() {
        docSelect = new JComboBox();
        docIDList = new ArrayList<Integer>();
        newDoc = new JButton("New Document");
        
        NewDocListener ndLis = new NewDocListener(); 
        newDoc.addActionListener(ndLis);
        
        ChangeDocListener cdLis = new ChangeDocListener();
        docSelect.addActionListener(cdLis);
    }
    
    /**
     * Initializes the GUI display for an instance of ClientGUI
     */
    private void createGUI() {
        Container panel = new Container();
        panel = this.getContentPane();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(bButton)
                        .addComponent(iButton)
                        .addComponent(uButton)
                        .addComponent(docSelect)
                        .addComponent(newDoc)
                        )
                .addComponent(scrollPane)
                );
        
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(bButton)
                        .addComponent(iButton)
                        .addComponent(uButton)
                        .addComponent(docSelect)
                        .addComponent(newDoc)
                        )
                .addComponent(scrollPane)
                );
    }
    
    /**
     * Get-method for the cursor position with the document window
     * @return the cursor position
     */
    private Integer getCursorPosition() {
        return new Integer(textPane.getCaretPosition());
    }
    
    /**
     * Ensures that the data model reflects the current title and content of the document being displayed
     */
    private void updateDocToDataModel() {
        dm.replaceDoc(currentDocID, currentTitle, currentDoc);
    }
    
    /**
     * Updates the screen to be consistent with the internal ClientDataModel representation.
     * @param t String representing the title of the document. Is guaranteed not to be null.
     */
    protected void updateGUI(Integer d) {
        if (currentDocID == d) { //document changed is document being worked on
        	if (dm.hasDoc(d))
        		currentDoc = dm.getDoc(d);
        	else { //current document has been deleted
        		currentDoc = new DefaultStyledDocument();
        	}
            textPane.setStyledDocument(currentDoc);
        }
        else { //document changed is not being worked on
        	if (docIDList.contains(d) && !dm.hasDoc(d)) { //remove deleted document from selector
        		int positionOfDoc = docIDList.indexOf(d);
        		docSelect.remove(positionOfDoc);
        		docIDList.remove(positionOfDoc);
        	} else if (!docIDList.contains(d) && dm.hasDoc(d)) { //put in new document into selector
        		docIDList.add(d);
        		docSelect.addItem(dm.getTitle(d));
        	}
        }
    }
    
    /**
     * Listener for "B" button; bolds and unbolds text
     * @author jcotler
     */
    class BoldListener extends StyledEditorKit.StyledTextAction implements ActionListener {
        private static final long serialVersionUID = 1L;

        /**
         * Creates new BoldListener
         */
        public BoldListener() {
            super("font-bold");
        }
        
    	@Override
        public void actionPerformed(ActionEvent e) {
            JEditorPane editor = getEditor(e);
            if (editor != null) {
                StyledEditorKit kit = getStyledEditorKit(editor);
                MutableAttributeSet attr = kit.getInputAttributes();
                isBold = (StyleConstants.isBold(attr)) ? false : true;
                SimpleAttributeSet sas = new SimpleAttributeSet();
                StyleConstants.setBold(sas, isBold);
                setCharacterAttributes(editor, sas, false);
                boolean isEnabling = (StyleConstants.isBold(attr)) ? false : true;
                Integer position = getCursorPosition();
                if (editor != null && editor.getSelectedText() != null && editor.getSelectedText().length() > 0)
                	controller.sendStyleChangeRequest(currentDocID, StyleChange.StyleType.BOLD, isEnabling, position, editor.getSelectedText().length());
                updateDocToDataModel();
            }
        }
    }
    
    /**
     * Listener for "I" button; italicizes and unitalicizes text
     * @author jcotler
     */
    class ItalicListener extends StyledEditorKit.StyledTextAction implements ActionListener {
        private static final long serialVersionUID = 1L;

        /**
         * Creates new ItalicListener
         */
        public ItalicListener() {
            super("font-italic");
        }
        
    	@Override
        public void actionPerformed(ActionEvent e) {
            JEditorPane editor = getEditor(e);
            if (editor != null) {
                StyledEditorKit kit = getStyledEditorKit(editor);
                MutableAttributeSet attr = kit.getInputAttributes();
                isItalic = (StyleConstants.isItalic(attr)) ? false : true;
                SimpleAttributeSet sas = new SimpleAttributeSet();
                StyleConstants.setItalic(sas, isItalic);
                setCharacterAttributes(editor, sas, false);
                boolean isEnabling = (StyleConstants.isBold(attr)) ? false : true;
                Integer position = getCursorPosition();
                if (editor != null && editor.getSelectedText() != null && editor.getSelectedText().length() > 0)
                	controller.sendStyleChangeRequest(currentDocID, StyleChange.StyleType.ITALIC, isEnabling, position, editor.getSelectedText().length());
                updateDocToDataModel();
            }
        }
    }
    
    /**
     * Listener for "U" button; underlines and ununderlines text 
     * @author jcotler
     */
    class UnderlineListener extends StyledEditorKit.StyledTextAction implements ActionListener {
        private static final long serialVersionUID = 1L;

        /**
         * Creates new UnderLineListener
         */
        public UnderlineListener() {
            super("font-underline");
        }
        
    	@Override
        public void actionPerformed(ActionEvent e) {
            JEditorPane editor = getEditor(e);
            if (editor != null) {
                StyledEditorKit kit = getStyledEditorKit(editor);
                MutableAttributeSet attr = kit.getInputAttributes();
                isUnderline = (StyleConstants.isUnderline(attr)) ? false : true;
                SimpleAttributeSet sas = new SimpleAttributeSet();
                StyleConstants.setUnderline(sas, isUnderline);
                setCharacterAttributes(editor, sas, false);
                boolean isEnabling = (StyleConstants.isBold(attr)) ? false : true;
                Integer position = getCursorPosition();
                if (editor != null && editor.getSelectedText() != null && editor.getSelectedText().length() > 0)
                	controller.sendStyleChangeRequest(currentDocID, StyleChange.StyleType.UNDERLINE, isEnabling, position, editor.getSelectedText().length());
                updateDocToDataModel();
            }
        }
    }
    
    /**
     * Listener for creating a new document using the "New Document" button 
     * @author jcotler
     */
    class NewDocListener implements ActionListener {
    	@Override
        public void actionPerformed(ActionEvent e) {
            String s = (String)JOptionPane.showInputDialog(
                    ClientGUI.this,
                    "Enter new document name:",
                    "New Document",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    "Untitled");
            if (s != null) {
                controller.sendNewDocRequest(s);
                textPane.setEditable(true);
            }
        }
    }

    /**
     * Listener for selecting a different document to work on from the JComboBox 
     * @author jcotler
     */
    class ChangeDocListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Integer d = docIDList.get(docSelect.getSelectedIndex());
            if (dm.hasDoc(d)) {
                textPane.setEditable(true);
                currentDocID = d;
            	currentTitle = dm.getTitle(d);
            	currentDoc = dm.getDoc(d);
            }
            textPane.setDocument(currentDoc);
        }
    }

    /**
     * Listener for typing changes in the text pane. 
     * @author jcotler
     */
    class TextPaneListener implements KeyListener {
        Integer position = getCursorPosition();
        
    	@Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                position = getCursorPosition();
                if (position.intValue() > 0) { //backspace is valid
                    controller.sendDeleteRequest(currentDocID, position-1, 1);
                    updateDocToDataModel();
                }
            }
            else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                position = getCursorPosition();
                if (position.intValue() < currentDoc.getLength()) { //delete is valid
                    controller.sendDeleteRequest(currentDocID, position, 1);
                    updateDocToDataModel();
                }
            }
        }
    	@Override
        public void keyReleased(KeyEvent e) {
    		return;
        }
    	@Override
        public void keyTyped(KeyEvent e) {
            String s = ""+e.getKeyChar();
            if (s.matches("\\p{Graph}") || s.matches("\\p{Space}")) {
                position = getCursorPosition();
                controller.sendInsertRequest(currentDocID, position, ""+e.getKeyChar(), isBold, isItalic, isUnderline);
                updateDocToDataModel();
            }
        }
        
    }
    
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ClientGUI main = new ClientGUI();
                main.setVisible(true);
            }
        });
    }
}
