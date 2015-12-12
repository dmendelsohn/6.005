package minesweeper.server;

import java.util.Random;

public class GridLocation {
	public final static int UNTOUCHED = 0;
	public final static int FLAGGED = 1;
	public final static int DUG = 2;
	
	private boolean mHasBomb;
	private int mStatus;
	
	public GridLocation(double probabilityOfBomb) {
		boolean hasBomb = (probabilityOfBomb > new Random().nextDouble());
		this.mHasBomb = hasBomb;
		this.mStatus = UNTOUCHED;
	}
	
	public boolean hasBomb() {
		return mHasBomb;
	}
	
	public void setHasBomb(boolean value) {
		mHasBomb = value;
	}
	
	public int getStatus() {
		return mStatus;
	}
	
	public void setStatus(int value) {
		mStatus = value;
	}
}
