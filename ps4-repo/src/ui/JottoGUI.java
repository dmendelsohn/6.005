package ui;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import model.JottoModel;
import controller.JottoController;

/**
 * Thread safety argument of Jotto game:
 * 
 * All the synchronization happens in JottoModel.  That class synchronizes all of its public methods except the
 * beginning of makeGuess (which allows for server latency).  The non-synchronized part of JottoModel does not
 * modify the state of the object at all.  Furthermore, all JottoModel fields are private.  Importantly, all calls
 * to the GUI happen synchronously.
 */

/**
 * TESTING STRATEGY:
 * My testing was entirely manual, because automatic JUnit testing of GUI is outside the scope of this assignment.
 * 
 * Problem 1 - I tested this method thoroughly once I had my GUI operating.  Details in problem 3.
 * 
 * Problem 2
 * 	- I ensured that the new puzzle button was functioning and setting the puzzle number to
 * 		a random number less than 10000 when the associated text field was blank or the value in
 * 		the text field was invalid (could not be parsed into an int). To do this I tried various int
 * 		values and a host of values that were too large or not integers
 *  - I ensured that pressing "enter" while the text field was in focus had the same behavior.
 *  
 *  Problem 3
 *  - I ensured that the button and text field (pressing enter) were both responsive.
 *  - I ensured that in several "typical" cases, I would get the correct print out.
 *  - I ensured that various invalid strings would be identified as such including:
 *  	strings of the wrong length
 *  	strings that are not dictionary works
 *  	strings including spaces (the server didn't do a great job of handling these)
 *  - This implicitly tested my makeGuess function from problem 1.
 *  - I also tested the "You win" case.
 *  
 *  Problem 4
 *  - I ensured that the the responses from Problem 3 would show up in the GUI as expected.
 *  - This includes "normal" guesses, invalid guesses, and "you win" guesses.
 *  
 *  Problem 5
 *  - To test the thread safety of my program, I tried various ways to "break" it.
 *  - Making guesses while a guess was being handled by the server.
 *  - Changing the puzzle while a guess was being handled by the server (the guess should NOT show up)
 *  - Doing combinations of these things quickly and in various combinations.
 */


/**
 * This is class represents Graphical User Interface for the game Jotto.
 * Users can pick a puzzle (either randomly or by ID number), make guesses,
 * See the result of those guesses, and be informed when they've won the game.
 * It will perform well regardless of server latency, in that it will populate
 * the result of a Jotto guess once that data becomes available, assuming
 * the user has changed puzzles in the interim.
 */
@SuppressWarnings("serial")
public class JottoGUI extends JFrame {
	private final static String PUZZLE_LABEL_TEMPLATE = "Current puzzle ID: %d";

	final private JButton newPuzzleButton;
	final private JTextField newPuzzleNumber;
	final private JLabel puzzleNumber;
	final private JLabel guessLabel;
	final private JTextField guess;
	final private JTable guessTable;
	final private JScrollPane guessTableScroller;
	
	private JottoModel model;
	private JottoController controller;

	public JottoGUI() {
		
		//initialize views
		newPuzzleButton = new JButton("New Puzzle");
		newPuzzleButton.setName("newPuzzleButton");
		newPuzzleNumber = new JTextField();
		newPuzzleNumber.setName("newPuzzleNumber");
		puzzleNumber = new JLabel();
		puzzleNumber.setName("puzzleNumber");
		puzzleNumber.setText("Puzzle number pi!");
		guessLabel = new JLabel();
		guessLabel.setName("guessLabel");
		guessLabel.setText("Make a guess!");
		guess = new JTextField();
		guess.setName("guess");
	    
		
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("Guess");
		tableModel.addColumn("In common");
		tableModel.addColumn("In place");
		tableModel.addRow(new Object[]{"Guess","In common","In place"});
		guessTable = new JTable(tableModel);
		guessTable.setName("guessTable");
		guessTableScroller = new JScrollPane(guessTable);
		
		//intialize model and controller
		model = new JottoModel();
		controller = new JottoController(model);
		model.setController(controller);
		model.setGUI(this);
		model.initializeData();
		
		//initialize and set up new puzzle listener
		controller.setNewPuzzleListener(newPuzzleNumber);
		newPuzzleButton.addActionListener(controller.getNewPuzzleListener());
		newPuzzleNumber.addActionListener(controller.getNewPuzzleListener());
		
		//initialize and set up guess listener
		controller.setGuessListener(guess);
		guess.addActionListener(controller.getGuessListener());
		
		
		//build content pane and its layout
		JPanel contentPane = new JPanel();
		GroupLayout layout = new GroupLayout(contentPane);
		contentPane.setLayout(layout);
		
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addGroup(layout.createSequentialGroup()
					.addComponent(puzzleNumber)
					.addComponent(newPuzzleButton)
					.addComponent(newPuzzleNumber))
				.addGroup(layout.createSequentialGroup()
					.addComponent(guessLabel)
					.addComponent(guess))
				.addComponent(guessTableScroller)
		);	
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(puzzleNumber)
					.addComponent(newPuzzleButton)
					.addComponent(newPuzzleNumber))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(guessLabel)
					.addComponent(guess))
				.addComponent(guessTableScroller)
		);
		
		//attach the content pane to the JottoGUI
		this.setContentPane(contentPane);
		this.pack();
	}
	
	public void setPuzzleNum(int num) {
		puzzleNumber.setText(String.format(PUZZLE_LABEL_TEMPLATE, num));
		newPuzzleNumber.setText("");
		clearAllGuesses();
	}
	
	public void addValidGuessToTable(int rowID, String guessString, int first, int second) {
		DefaultTableModel tableModel = (DefaultTableModel)guessTable.getModel();
		tableModel.setValueAt(new Integer(first), rowID, 1);
		tableModel.setValueAt(new Integer(second), rowID, 2);
	}
	
	public void addMessageToTable(int rowID, String guessString, String message) {
		DefaultTableModel tableModel = (DefaultTableModel)guessTable.getModel();
		tableModel.setValueAt(message, rowID, 1);
	}
	
	public void clearGuess() {
		guess.setText("");
	}
	
	public void clearAllGuesses() {
		((DefaultTableModel)guessTable.getModel()).setRowCount(0);
	}
	
	public int putGuess(String guessString) {
		DefaultTableModel tableModel = (DefaultTableModel)guessTable.getModel();
		int numRows = tableModel.getRowCount();
		tableModel.addRow(new Object[]{guessString});
		return numRows;
	}
	
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JottoGUI main = new JottoGUI();
				main.setSize(400, 300);

				main.setVisible(true);
				main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}
