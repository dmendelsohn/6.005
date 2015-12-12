package piwords;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class WordFinderTest {
    @Test
    public void basicFindWordsTest() {
        String haystack = "abcde";
        String[] needles = {"ab", "abc", "de", "fg"};

        Map<String, Integer> expectedOutput = new HashMap<String, Integer>();
        expectedOutput.put("ab", 0);
        expectedOutput.put("abc", 0);
        expectedOutput.put("de", 3);

        assertEquals(expectedOutput, WordFinder.findWords(haystack,
                                                              needles));
    }

    @Test
    public void findWordsTest() {
    	String haystack;
    	String[] needles;
    	Map<String, Integer> expectedOutput = new HashMap<String, Integer>();
    	
    	//case with no found needles
    	haystack = "no found needles";
    	needles = new String[]{"hi","cat","dog"};
    	assertEquals(expectedOutput, WordFinder.findWords(haystack, needles));
    	
    	//case with a needle found multiple times, also test to see needles is unchanged
    	haystack = "banana";
    	needles = new String[] {"this","ba","na"};
    	expectedOutput.put("ba", 0);
    	expectedOutput.put("na", 2);
    	assertEquals(expectedOutput, WordFinder.findWords(haystack, needles));
    	assertArrayEquals(new String[]{"this","ba","na"}, needles);
    	
    	//case where the haystack is a substring of a needle
    	haystack = "laughter";
    	needles = new String[] {"dog","augh","slaughter"};
    	expectedOutput.clear();
    	expectedOutput.put("augh", 1);
    	assertEquals(expectedOutput, WordFinder.findWords(haystack, needles));
    	
    	//case with empty haystack
    	haystack = "";
    	needles = new String[] {"Cat","Dog"};
    	expectedOutput.clear();
    	assertEquals(expectedOutput, WordFinder.findWords(haystack, needles));
    	
    	//case with empty needles input array
    	haystack = "no longer empty!";
    	needles = new String[] {};
    	assertEquals(expectedOutput, WordFinder.findWords(haystack, needles));
    }
}
