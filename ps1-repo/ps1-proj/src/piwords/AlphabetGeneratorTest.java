package piwords;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AlphabetGeneratorTest {
    @Test
    public void generateFrequencyAlphabetTest() {
        List<Pair<Character, Integer>> testInput = 
                new ArrayList<Pair<Character,Integer>>();
        
        //standard case
        testInput.add(new Pair<Character, Integer>('a', 4));
        testInput.add(new Pair<Character, Integer>('b', 4));
        testInput.add(new Pair<Character, Integer>('c', 2));
        
        char[] expectedOutput = {'a', 'a', 'a', 'a',
                                 'b', 'b', 'b', 'b',
                                 'c', 'c'};
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(testInput));
        
        //test with a character with specifically 0 frequency
        testInput.clear();
        testInput.add(new Pair<Character, Integer>('c', 3));
        testInput.add(new Pair<Character, Integer>('d', 0));
        testInput.add(new Pair<Character, Integer>('a', 2));
        testInput.add(new Pair<Character, Integer>('b', 0));
        
        expectedOutput = new char[] {'a','a','c','c','c'};
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(testInput)); 
        
        //test with all characters of 0 frequency
        testInput.clear();
        testInput.add(new Pair<Character, Integer>('b', 0));
        testInput.add(new Pair<Character, Integer>('a', 0));
        testInput.add(new Pair<Character, Integer>('c', 0));
        
        expectedOutput = new char[] {};
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(testInput)); 
        
        //test where testInput is empty
        testInput.clear();
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(testInput)); 
        
        //test with weird characters
        testInput.add(new Pair<Character, Integer>('%', 2));
        testInput.add(new Pair<Character, Integer>('a', 1));
        testInput.add(new Pair<Character, Integer>('+', 3));
        testInput.add(new Pair<Character, Integer>('Q', 4));
        
        expectedOutput = new char[] {'%','%','+','+','+','Q','Q','Q','Q','a'};
        assertArrayEquals(expectedOutput,
                AlphabetGenerator.generateFrequencyAlphabet(testInput)); 
    }

}
