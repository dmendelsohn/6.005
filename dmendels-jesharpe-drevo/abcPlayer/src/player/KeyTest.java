package player;
import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

public class KeyTest {
	/**
	 * Tests a couple of key sigatures and ensures that the
	 * basenote semitone transpositions are correct for that key.
	 */
	@Test
	public void testKeySignature() {
		Key k = new Key("C#m");
		HashMap<String, Integer> csm = k.getKeyMap();

		assertTrue(csm.get("A") == 0);
		assertTrue(csm.get("B") == 0);
		assertTrue(csm.get("C") == 1);
		assertTrue(csm.get("D") == 1);
		assertTrue(csm.get("E") == 0);
		assertTrue(csm.get("F") == 1);
		assertTrue(csm.get("G") == 1);
		
		Key k2 = new Key("C");
		HashMap<String, Integer> c = k2.getKeyMap();
		
		assertTrue(c.get("A") == 0);
		assertTrue(c.get("B") == 0);
		assertTrue(c.get("C") == 0);
		assertTrue(c.get("D") == 0);
		assertTrue(c.get("E") == 0);
		assertTrue(c.get("F") == 0);
		assertTrue(c.get("G") == 0);
	}
}
