package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class DigitsToStringConverterTest {
    @Test
    public void basicNumberSerializerTest() {
        // Input is a 4 digit number, 0.123 represented in base 4
        int[] input = {0, 1, 2, 3};

        // Want to map 0 -> "d", 1 -> "c", 2 -> "b", 3 -> "a"
        char[] alphabet = {'d', 'c', 'b', 'a'};

        String expectedOutput = "dcba";
        assertEquals(expectedOutput,
                     DigitsToStringConverter.convertDigitsToString(
                             input, 4, alphabet));
    }

    @Test
    public void convertDigitsToStringTest() {
    	int[] input;
    	final char[] alphabet = new char[]{'a','c','e','g'};
    	final char[] weirdAlphabet = new char[]{'a','/','%',',','3','M','+','z'};
    	
    	//test with weird alphabet with funky characters
    	input = new int[]{2,3,3,4,1,0,5,6,7};
    	assertEquals("%,,3/aM+z",DigitsToStringConverter.convertDigitsToString(input, 8, weirdAlphabet));
    	
    	//test empty input works
    	input = new int[]{};
    	assertEquals("", DigitsToStringConverter.convertDigitsToString(input, 4, alphabet));
    	
    	//test that illegal argument exception is working
    	boolean threwException = false;
    	input = new int[]{2, 3, 10};
    	try {
    		DigitsToStringConverter.convertDigitsToString(input, 4, alphabet);
    	} catch (IllegalArgumentException e) {
    		threwException = true;
    	}
    	assertEquals(true, threwException);
    	threwException = false;
    	input = new int[]{2, -1, 0};
    	try {
    		DigitsToStringConverter.convertDigitsToString(input, 4, alphabet);
    	} catch (IllegalArgumentException e) {
    		threwException = true;
    	}
    	assertEquals(true, threwException);
    }
}
