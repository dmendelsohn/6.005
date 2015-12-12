package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseTranslatorTest {
    @Test
    public void basicBaseTranslatorTest() {
        // Expect that .01 in base-2 is .25 in base-10
        // (0 * 1/2^1 + 1 * 1/2^2 = .25)
        int[] input = {0, 1};
        int[] expectedOutput = {2, 5};
        assertArrayEquals(expectedOutput,
                          BaseTranslator.convertBase(input, 2, 10, 2));
    }

    @Test
    public void convertBaseTest() {
    	int[] input;
    	int[] expectedOutput;
    	//test varying levels of precision
    	input = new int[]{6,4,5};
    	expectedOutput = new int[]{3,3,1,0};
    	assertArrayEquals(expectedOutput, BaseTranslator.convertBase(input, 7, 4, 4));
    	expectedOutput = new int[]{3, 3};
    	assertArrayEquals(expectedOutput, BaseTranslator.convertBase(input, 7, 4, 2));
    	expectedOutput = new int[]{3,3,1,0,0,0,3,2};
    	assertArrayEquals(expectedOutput, BaseTranslator.convertBase(input, 7, 4, 8));
    	
    	//test that the input array is not mutated
    	int[] unmodifiedInput = {6,4,5};
    	assertArrayEquals(unmodifiedInput, input);
    	
    	//tests where baseA >> baseB or baseA << baseB
    	input = new int[]{11, 31, 25};
    	expectedOutput = new int[]{1,0,0,0,1,1,2,2, 2, 1};
    	assertArrayEquals(expectedOutput, BaseTranslator.convertBase(input, 35, 3, 10));
    	input = new int[]{1,0,0,0,1,1,2,2,2,1};
    	expectedOutput = new int[]{11,31,24}; //different from previous input due to rounding
    	assertArrayEquals(expectedOutput, BaseTranslator.convertBase(input, 3, 35, 3));
    	
    	//test that illegal argument exception is working
    	boolean threwException = false;
    	input = new int[]{2, 3, 10};
    	try {
    		BaseTranslator.convertBase(input, 8, 3, 3);
    	} catch (IllegalArgumentException e) {
    		threwException = true;
    	}
    	assertEquals(true, threwException);
    	threwException = false;
    	input = new int[]{2, -1, 0};
    	try {
    		BaseTranslator.convertBase(input, 8, 3, 3);
    	} catch (IllegalArgumentException e) {
    		threwException = true;
    	}
    	assertEquals(true, threwException);
    }
}
