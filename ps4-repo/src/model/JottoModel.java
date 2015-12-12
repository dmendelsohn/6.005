package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import ui.JottoGUI;
import controller.JottoController;

/**
 * This is a class that represents a model for the game Jotto. It handles all game logic; namely
 * making guesses and changing the current puzzle.  It receive input from the JottoController,
 * and sends commands to JottoGUI
 */
public class JottoModel {
	private final static String URL_BASE = "http://courses.csail.mit.edu/6.005/jotto.py?puzzle=%1$d&guess=%2$s";
	private final static int DEFAULT_PUZZLE_NUM = 1;
	private final static String GUESS = "guess";
	private final static String ERROR = "error";
	private final static String WIN = "you win!";
	private final static String INVALID_GUESS = "Invalid guess.";

	@SuppressWarnings("unused")
	private JottoController controller;
	private JottoGUI gui;

	private int currentPuzzleNum;
	private int puzzleSession; //this increments each time the puzzle changes, so we know when to ignore server response

	/**
	 * Sets the "controller" instance variable
	 * @param controller - the object to which "controller" instance variable will point
	 */
	public synchronized void setController(JottoController controller) {
		this.controller = controller;
	}

	/**
	 * Sets the "gui" instance variable
	 * @param gui - the object to which "gui" instance variable will point
	 */
	public synchronized void setGUI(JottoGUI gui) {
		this.gui = gui;
	}

	/**
	 * Sets the state of the JottoModel to the default state
	 */
	public synchronized void initializeData() {
		setCurrentPuzzleNum(DEFAULT_PUZZLE_NUM);
		puzzleSession = 0;
	}

	/**
	 * Queries the Jotto server with a guess, receives response, and alerts the GUI appropriately
	 * @param guess - the input guess by the user
	 * This method uses whatever the current value of currentPuzzleNum is to query the server.
	 * Upon receiving server response, it only alerts the GUI if the puzzle number has not changed
	 * since the beginning of the call to this method.
	 */
	public void makeGuess(String guess) throws MalformedURLException, IOException {
		gui.clearGuess();
		int rowID = gui.putGuess(guess);
		int puzzleSessionID = puzzleSession;
		
		String guessWithoutSpaces = guess.replace(" ", "%20");
		URL url = new URL(getURLString(currentPuzzleNum, guessWithoutSpaces));

		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String inputLine = in.readLine();
		in.close();        

		synchronized (this) {
			if (puzzleSessionID == puzzleSession){ //check that we haven't changed puzzles since the beginning of the method
				String lineHeader = inputLine.substring(0,5);
				if (lineHeader.equals(GUESS)) {
					String[] args = inputLine.split(" ");
					int inCommon = Integer.parseInt(args[1]);
					int inPlace = Integer.parseInt(args[2]);
					if (inCommon == 5 && inPlace == 5) {
						gui.addMessageToTable(rowID, guess, WIN);
					} else {
						gui.addValidGuessToTable(rowID, guess, inCommon, inPlace);
					}
				} else if (lineHeader.equals(ERROR)) {
					char errorCode = inputLine.charAt(6);
					if (errorCode == '2') {
						gui.addMessageToTable(rowID, guess, INVALID_GUESS);
					} else { //This should never happen, if the model functions according to spec
						gui.clearGuess();
						System.out.println(inputLine);
					}
				}
			}
		}
	}

	private String getURLString(int puzzleNum, String guess) {
		return String.format(URL_BASE, puzzleNum, guess);
	}

	/**
	 * Sets the current puzzle number
	 * @param num - the number we would like to set currentPuzzleNum to
	 * This method also increments the puzzle session and updates the GUI
	 */
	public synchronized void setCurrentPuzzleNum(int num) {
		currentPuzzleNum = num;
		puzzleSession++;
		gui.setPuzzleNum(currentPuzzleNum);
	}
}
