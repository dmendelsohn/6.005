package minesweeper.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
/**
 * 
 * @author danielmendelsohn
 * 
 * The Board class is thread-safe because:
 * 
 * 1) All of it's public methods (which are the ones outside threads can
 * call) are synchronized methods.  Private methods are just helpers, so they
 * need not be synchronized.
 * 
 * 2) All of its instance variables are private and final.  Furthermore,
 * the mutable "grid" variable cannot be accessed via public methods.  That
 * means a Board is only mutable within its public methods, which are synchronized.
 *
 */
public class Board {
	private static final double DEFAULT_BOMB_PROB = 0.25;
	private static final double CERTAIN_BOMB = 1;
	private static final double CERTAIN_NO_BOMB = 0;
	
	private final GridLocation[][] grid;
	private final int size;

	/**
	 * Creates a square size X size board, where each spot has a one
	 * fourth chance of having a bomb and all probabilities are independent
	 * @param size - side length of board
	 *
	 */
	public Board(int size) {
		this.size = size;
		grid = new GridLocation[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				grid[i][j] = new GridLocation(DEFAULT_BOMB_PROB);
			}
		}
	}

	/**
	 * Creates a Board matching the description in file. It enforces
	 * that the board is square and the input file is valid, as per the specification
	 * in the assigment, throwing a RuntimeException otherwise
	 * @param file - File containing a board description, as specified
	 * in the assignment
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Board(File file) throws FileNotFoundException, IOException {
		Scanner sc = new Scanner(new FileReader(file));
		String line;
		int row_count = 0;
		if (sc.hasNextLine()) {
			line = sc.nextLine(); //read the first line
			row_count++;
			String[] digits = line.split(" ");
			this.size = digits.length;
			grid = new GridLocation[digits.length][digits.length];
			grid[0] = buildGridRow(digits, size);
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				row_count++;
				digits = line.split(" ");
				grid[row_count - 1] = buildGridRow(digits, size);
			}
			//check to make sure we've got a square board
			if (row_count != size)
				throw new RuntimeException("Problem with file: input must be a square");
		} else
			throw new RuntimeException("File can't be empty");
	}

	/**
	 * Helper method for building a row of the grid
	 * @param digits - an array of strings representing whether or not a location has a bomb
	 * @param the size of the board that is being built
	 * @return an array representing the single row of a grid that
	 * is specified by digits.
	 * @throws RuntimeException if digits.length doesn't match size or one of the
	 * elements in digits is not "0" or "1"
	 */
	private GridLocation[] buildGridRow(String[] digits, int size) {
		if (digits.length != size) {
			throw new RuntimeException("Length of row is incorrect");
		}
		GridLocation[] gridRow = new GridLocation[digits.length];
		for (int i = 0; i < digits.length; i++) {
			if (digits[i].equals("0"))
				gridRow[i] = new GridLocation(CERTAIN_NO_BOMB);
			else if (digits[i].equals("1"))
				gridRow[i] = new GridLocation(CERTAIN_BOMB);
			else
				throw new RuntimeException("Input file must contain only combination of '0', '1', or whitespace");
		}
		return gridRow;
	}

	/**
	 * Flags or deflags a location
	 * @param x
	 * @param y
	 * @param value - whether or not the location (x,y) should have a bomb after this call
	 * 
	 * @modifies grid so that the location is flagged if value = true and untouched if value = false
	 * if the location (x,y) is dug, this method does nothing.
	 */
	public synchronized void setFlaggedValue(int x, int y, boolean value) {
		if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length || grid[x][y].getStatus() == GridLocation.DUG) {
			//do nothing
		}
		else if (value && grid[x][y].getStatus() == GridLocation.UNTOUCHED) //flag the spot
			grid[x][y].setStatus(GridLocation.FLAGGED);
		else if (!value && grid[x][y].getStatus() == GridLocation.FLAGGED) //unflag the spot
			grid[x][y].setStatus(GridLocation.UNTOUCHED);
	}

	/**
	 * Digs at a location
	 * @param x
	 * @param y
	 * @return true iff there was a bomb at location (x,y)
	 * @modifies grid so that the location (x,y) is dug, and performs recursive digging
	 * as long as the currently dug spot has no bomb neighbors.
	 * If (x,y) is flagged, this does nothing.
	 */
	public synchronized boolean dig(int x, int y) {
		boolean isBoom = false;
		if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length || 
				grid[x][y].getStatus() != GridLocation.UNTOUCHED) {
			//do nothing
		} else {
			isBoom = grid[x][y].hasBomb();
			if (isBoom)
				grid[x][y].setHasBomb(false); //"blow up" the bomb
			recursiveDig(x,y);
		}
		return isBoom;
	}
	
	private void recursiveDig(int x, int y) {
		if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length || 
				grid[x][y].getStatus() != GridLocation.UNTOUCHED)
			return;
		grid[x][y].setStatus(GridLocation.DUG);
		if (numBombsAround(x,y) == 0) { //run recursive dig on all the neighbors!
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (i != 0 || j != 0) //we're at a neighbor, not the location itself
						recursiveDig(x+i,y+j);
				}
			}
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return true iff (x,y) is not out of bounds and that location contains a bomb
	 */
	public synchronized boolean hasBombAtLocation(int x, int y) {
		if (x < 0 || y < 0 || x >= size || y >= size)
			return false;
		else
			return grid[x][y].hasBomb();
	}

	//does not include the point (x,y)
	private int numBombsAround(int x, int y) {
		int count = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (hasBombAtLocation(x+i,y+j) && (i != 0 || j != 0))
					count++;
			}
		}
		return count;
	}

	private String convertLocationToString(int x, int y) {
		GridLocation g = grid[x][y];
		switch(g.getStatus()) {
		case GridLocation.UNTOUCHED:
			return "-";
		case GridLocation.FLAGGED:
			return "F";
		case GridLocation.DUG:
			int numAround = numBombsAround(x,y);
			if (numAround == 0)
				return " ";
			else
				return ("" + numAround);
		}
		return null;
	}

	/**
	 * @return the Board as a player would see it
	 */
	@Override
	public synchronized String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size - 1; j++) {
				sb.append(convertLocationToString(i,j));
				sb.append(" ");
			}
			sb.append(convertLocationToString(i, size-1));
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public synchronized int getSize() {
		return size;
	}

}
