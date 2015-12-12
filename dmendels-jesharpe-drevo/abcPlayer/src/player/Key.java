package player;

import java.util.HashMap;
import java.util.Map;

public class Key {
	private Map<String, Integer> keyMap;
	private String keyString;

	/**
	 * KEY SIGNATURE MAPPING
	 * 
	 * What follows isn't pretty, but it is static, and so is only loaded once when the class is
	 * loaded. We weren't the most musically inclined, and this was a foolproof way to ensure
	 * we go the key signatures right. 
	 * 
	 * As a bonus, the resulting HashMap is also used when processing accidentals, and is reset
	 * every measure to clear the state. The mapping of 
	 * 
	 * Int (pitch class, ie 'A' or 'B' any octave) -> Int (semitone adjustment)
	 * 
	 * makes this process work well with semitone adjustments (integer +/- of normal note values). 
	 */
	private static HashMap<String, HashMap<String, Integer>> KEY_SIG = new HashMap<String, HashMap<String, Integer>>();
	static HashMap<String, Integer> blankMap;
	static {
		blankMap = new HashMap<String, Integer>();
		blankMap.put("C", 0); blankMap.put("F", 0); blankMap.put("B", 0);
		blankMap.put("D", 0); blankMap.put("G", 0);
		blankMap.put("E", 0); blankMap.put("A", 0);

		// MAJOR //
		HashMap<String, Integer> MAJOR_C_NATURAL = new HashMap<String, Integer>(blankMap);

		// sharps
		HashMap<String, Integer> MAJOR_G_NATURAL = new HashMap<String, Integer>(MAJOR_C_NATURAL);
		MAJOR_G_NATURAL.put("F", 1);

		HashMap<String, Integer> MAJOR_D_NATURAL = new HashMap<String, Integer>(MAJOR_G_NATURAL);
		MAJOR_D_NATURAL.put("C", 1);

		HashMap<String, Integer> MAJOR_A_NATURAL = new HashMap<String, Integer>(MAJOR_G_NATURAL);
		MAJOR_A_NATURAL.put("G", 1);

		HashMap<String, Integer> MAJOR_E_NATURAL = new HashMap<String, Integer>(MAJOR_A_NATURAL);
		MAJOR_E_NATURAL.put("D'", 1);

		HashMap<String, Integer> MAJOR_B_NATURAL = new HashMap<String, Integer>(MAJOR_E_NATURAL);
		MAJOR_B_NATURAL.put("A", 1);

		HashMap<String, Integer> MAJOR_F_SHARP = new HashMap<String, Integer>(MAJOR_B_NATURAL);
		MAJOR_F_SHARP.put("E", 1);

		// flats
		HashMap<String, Integer> MAJOR_F_NATURAL = new HashMap<String, Integer>(MAJOR_C_NATURAL);
		MAJOR_F_NATURAL.put("B", -1);

		HashMap<String, Integer> MAJOR_B_FLAT = new HashMap<String, Integer>(MAJOR_F_NATURAL);
		MAJOR_B_FLAT.put("E", -1);

		HashMap<String, Integer> MAJOR_E_FLAT = new HashMap<String, Integer>(MAJOR_B_FLAT);
		MAJOR_E_FLAT.put("A", -1);

		HashMap<String, Integer> MAJOR_A_FLAT = new HashMap<String, Integer>(MAJOR_E_FLAT);
		MAJOR_A_FLAT.put("D", -1);

		HashMap<String, Integer> MAJOR_D_FLAT = new HashMap<String, Integer>(MAJOR_A_FLAT);
		MAJOR_D_FLAT.put("G", -1);
		
		// MINOR //
		HashMap<String, Integer> MINOR_A_NATURAL = new HashMap<String, Integer>(blankMap);
		
		HashMap<String, Integer> MINOR_E_NATURAL = new HashMap<String, Integer>(MINOR_A_NATURAL);
		MINOR_E_NATURAL.put("F", 1);
		
		HashMap<String, Integer> MINOR_B_NATURAL = new HashMap<String, Integer>(MINOR_E_NATURAL);
		MINOR_B_NATURAL.put("C", 1);
		
		HashMap<String, Integer> MINOR_F_SHARP   = new HashMap<String, Integer>(MINOR_B_NATURAL);
		MINOR_F_SHARP.put("G", 1);
		
		HashMap<String, Integer> MINOR_C_SHARP   = new HashMap<String, Integer>(MINOR_F_SHARP);
		MINOR_C_SHARP.put("D", 1);
		
		HashMap<String, Integer> MINOR_G_SHARP   = new HashMap<String, Integer>(MINOR_C_SHARP);
		MINOR_G_SHARP.put("A", 1);
		
		HashMap<String, Integer> MINOR_D_SHARP   = new HashMap<String, Integer>(MINOR_G_SHARP);
		MINOR_D_SHARP.put("E", 1);
		
		HashMap<String, Integer> MINOR_D_NATURAL = new HashMap<String, Integer>(blankMap);
		MINOR_D_NATURAL.put("B", -1);
		
		HashMap<String, Integer> MINOR_G_NATURAL = new HashMap<String, Integer>(MINOR_D_NATURAL);
		MINOR_D_NATURAL.put("E", -1);
		
		HashMap<String, Integer> MINOR_C_NATURAL = new HashMap<String, Integer>(MINOR_G_NATURAL);
		MINOR_C_NATURAL.put("A", -1);
		
		HashMap<String, Integer> MINOR_F_NATURAL = new HashMap<String, Integer>(MINOR_C_NATURAL);
		MINOR_F_NATURAL.put("D", -1);
		
		HashMap<String, Integer> MINOR_B_FLAT    = new HashMap<String, Integer>(MINOR_F_NATURAL);
		MINOR_B_FLAT.put("G", -1);
		
		//============================================
		
		// Major
		KEY_SIG.put("C", MAJOR_C_NATURAL);
		KEY_SIG.put("Db", MAJOR_D_FLAT);
		KEY_SIG.put("D", MAJOR_D_NATURAL);
		KEY_SIG.put("Eb", MAJOR_E_FLAT);
		KEY_SIG.put("E", MAJOR_E_NATURAL);
		KEY_SIG.put("F", MAJOR_F_NATURAL);
		KEY_SIG.put("F#", MAJOR_F_SHARP);
		KEY_SIG.put("G", MAJOR_G_NATURAL);
		KEY_SIG.put("Ab", MAJOR_A_FLAT);
		KEY_SIG.put("A", MAJOR_A_NATURAL);
		KEY_SIG.put("Bb", MAJOR_B_FLAT);
		KEY_SIG.put("B", MAJOR_B_NATURAL);
		
		// Minor
		KEY_SIG.put("Cm", MINOR_C_NATURAL);
		KEY_SIG.put("C#m", MINOR_C_SHARP);
		KEY_SIG.put("Dm", MINOR_D_NATURAL);
		KEY_SIG.put("D#m", MINOR_D_SHARP);
		KEY_SIG.put("Em", MINOR_E_NATURAL);
		KEY_SIG.put("Fm", MINOR_F_NATURAL);
		KEY_SIG.put("F#m", MINOR_F_NATURAL);
		KEY_SIG.put("Gm", MINOR_F_NATURAL);
		KEY_SIG.put("G#m", MINOR_G_SHARP);
		KEY_SIG.put("Am", MINOR_A_NATURAL);
		KEY_SIG.put("A#m", MINOR_B_FLAT);
		KEY_SIG.put("Bm", MINOR_B_NATURAL);
	}
	
	/**
	 * Creates the key mapping given the key signature string
	 * 
	 * @param keyString
	 */
	public Key(String keyString) {
		this.keyString = keyString.replaceAll(" ", "").trim();
		keyMap = KEY_SIG.get(this.keyString);
	}
	
	/**
	 * Accessor method for the key signature mapping
	 * 
	 * @return mapping of String notes to their key signature semitone transposition
	 */
	public HashMap<String, Integer> getKeyMap() {
		return new HashMap<String, Integer>(keyMap);
	}
	
}
