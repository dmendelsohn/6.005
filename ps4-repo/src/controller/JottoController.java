package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JTextField;
import javax.swing.SwingWorker;

import model.JottoModel;


/**
 * A class that represents a JottoController.  It handles user input to the GUI, interprets the input
 * and calls the model appropriately using worker threads.
 */
public class JottoController {
	private static final int NUM_RANDOM_PUZZLES = 10000;
	
	private JottoModel model;
	private NewPuzzleListener newPuzzleListener;
	private GuessListener guessListener;
	
	/**
	 * Sets the "model" instance variable for this object
	 * @param model
	 */
	public JottoController(JottoModel model) {
		this.model = model;
	}
	
	/**
	 * Gets the controller's NewPuzzleListener object
	 * @return the NewPuzzleListener object
	 */
	public NewPuzzleListener getNewPuzzleListener() {
		return newPuzzleListener;
	}
	
	/**
	 * Sets the text field that allows users to set a new puzzle number
	 * @param textField
	 */
	public void setNewPuzzleListener(JTextField textField) {
		newPuzzleListener = new NewPuzzleListener(textField);
	}
	
	/**
	 * Gets the controller's GuessListener object
	 * @return the controller's GuessListener object
	 */
	public GuessListener getGuessListener() {
		return guessListener;
	}
	
	/**
	 * Sets the text field that allows users to make guesses
	 * @param textField
	 */
	public void setGuessListener(JTextField textField) {
		guessListener = new GuessListener(textField);
	}
	
	/**
	 * A listener for user requests for a new puzzle
	 */
	class NewPuzzleListener implements ActionListener {
		private final JTextField mTextField;
		
		/**
		 * Constructor sets the textfield to which this listener listens
		 * @param textField
		 */
		public NewPuzzleListener(JTextField textField) {
			mTextField = textField;
		}
		
		/**
		 * Makes new worker thread to handle the request
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			NewPuzzleWorker worker = new NewPuzzleWorker(mTextField.getText());
			worker.execute();
		}
	}
	
	class NewPuzzleWorker extends SwingWorker<Void, Void> {
		private String mText;
		
		public NewPuzzleWorker(String inputText) {
			mText = inputText;
		}
		
		/**
		 * Handles the contents of the new puzzle text field and calls the model accordingly
		 */
		@Override
		protected Void doInBackground() throws Exception {
			int number;
			try {
				number = Integer.parseInt(mText);
			} catch (NumberFormatException e) {
				number = (int) (Math.random() * NUM_RANDOM_PUZZLES);
			}
			model.setCurrentPuzzleNum(number);
			return null;
		}
	}
	
	/**
	 * A listener for user guess requests
	 */
	class GuessListener implements ActionListener {
		private final JTextField mTextField;
		
		/**
		 * Constructor sets the textfield to which this listener listens
		 * @param textField
		 */
		public GuessListener(JTextField textField) {
			mTextField = textField;
		}

		/**
		 * Makes a new worker thread to handle the request
		 */
		@Override
		public void actionPerformed(ActionEvent event) {
			GuessWorker worker = new GuessWorker(mTextField.getText());
			worker.execute();
		}
	}
	
	class GuessWorker extends SwingWorker<Void, Void> {
		private String mText;
		
		public GuessWorker(String inputText) {
			mText = inputText;
		}
		
		/**
		 * Handles the contents of the guess text field and calls the model accordingly
		 */
		@Override
		protected Void doInBackground() throws Exception {
			if (mText.length() > 0) {
				try {
					model.makeGuess(mText);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}
}
