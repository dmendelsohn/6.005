package piwords;

import java.util.HashMap;
import java.util.Map;

public class WordFinder {
    /**
     * Given a String (the haystack) and an array of Strings (the needles),
     * return a Map<String, Integer>, where keys in the map correspond to
     * elements of needles that were found as substrings of haystack, and the
     * value for each key is the lowest index of haystack at which that needle
     * was found. A needle that was not found in the haystack should not be
     * returned in the output map.
     *
     * For example, let haystack = "abaaaaba" and needles = ["ab", "aaaa", "bb"]
     * then findWords would return {"ab"-> 0, "aaaa" -> 2 }; 
     * 
     * @param haystack The string to search into.
     * @param needles The array of strings to search for. This array is not
     *                mutated.
     * @return A map of needles that were found in the haystack mapped to the
     *	       lowest index of the haystack at which the needle was found.
     */
    public static Map<String, Integer> findWords(String haystack,
                                                     String[] needles) { 
    	Map<String, Integer> result = new HashMap<String, Integer>();
        for (String needle : needles) {
        	int x = haystack.indexOf(needle);
        	if (x != -1) {
        		result.put(needle, x);
        	}
        }
        return result;
    }
}
