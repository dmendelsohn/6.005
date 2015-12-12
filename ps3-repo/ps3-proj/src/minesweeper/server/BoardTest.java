package minesweeper.server;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;


public class BoardTest {
	private static final String TEST_1 = "src/minesweeper/server/board_file_5";
	private static final String TEST_2 = "src/minesweeper/server/small_board";
	
	@Test
	public void constructorWithFileTest() throws IOException {
		File file = new File(TEST_1);
		assertTrue(file.isFile());
		
		Board board = new Board(file);
		assertEquals(board.getSize(), 7);
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if ((i==0 && j==6) || (i==4 && j==1))
					assertTrue(board.hasBombAtLocation(i,j));
				else
					assertFalse(board.hasBombAtLocation(i,j));
			}
		}
	}
	
	@Test
	public void constructorWithSizeTest() {
		Board board = new Board(4);
		assertEquals(board.getSize(), 4);
		
		String expected = "- - - -\n- - - -\n- - - -\n- - - -\n";
		assertEquals(board.toString(),expected);
	}
	
	@Test
	public void digTest() throws IOException {
		File file = new File(TEST_2);
		assertTrue(file.isFile());
		Board board = new Board(file);
		
		assertEquals(board.toString(), "- - - -\n- - - -\n- - - -\n- - - -\n");
		
		//test simple dig
		board.dig(0, 1);
		assertEquals(board.toString(), "- 1 - -\n- - - -\n- - - -\n- - - -\n");
		
		//test recursive dig
		board.dig(2, 3);
		assertEquals(board.toString(), "- 1 - -\n- - 1 1\n- - 1  \n- - 1  \n");
		
		//test BOOM
		board.dig(0, 3);
		assertEquals(board.toString(), "- 1    \n- 1    \n- - 1  \n- - 1  \n");
		
		//test no action if spot is not untouched (is flagged or dug already)
		board.dig(1, 1);
		assertEquals(board.toString(), "- 1    \n- 1    \n- - 1  \n- - 1  \n");
		board.setFlaggedValue(0, 0, true);
		board.dig(0,0);
		assertEquals(board.toString(), "F 1    \n- 1    \n- - 1  \n- - 1  \n");
	}
	
	@Test
	public void setFlaggedValueTest() throws IOException {
		File file = new File(TEST_2);
		assertTrue(file.isFile());
		Board board = new Board(file);
		assertEquals(board.toString(), "- - - -\n- - - -\n- - - -\n- - - -\n");

		//test flagging
		board.setFlaggedValue(0, 0, true);
		assertEquals(board.toString(), "F - - -\n- - - -\n- - - -\n- - - -\n");

		//test deflagging
		board.setFlaggedValue(0, 0, false);
		assertEquals(board.toString(), "- - - -\n- - - -\n- - - -\n- - - -\n");

		//test flagging dug spot
		board.dig(0, 0);
		board.setFlaggedValue(0,0,true);
		assertEquals(board.toString(), "1 - - -\n- - - -\n- - - -\n- - - -\n");

		//test flagging flagged spot
		board.setFlaggedValue(0, 1, true);
		board.setFlaggedValue(0, 1, true);
		assertEquals(board.toString(), "1 F - -\n- - - -\n- - - -\n- - - -\n");

		//test deflagging dug spot
		board.setFlaggedValue(0, 0, false);
		assertEquals(board.toString(), "1 F - -\n- - - -\n- - - -\n- - - -\n");

		//test deflagging untouched spot
		board.setFlaggedValue(0, 2, false);
		assertEquals(board.toString(), "1 F - -\n- - - -\n- - - -\n- - - -\n");
	}
	
}
