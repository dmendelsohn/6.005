package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class PiGeneratorTest {
    @Test
    public void basicPowerModTest() {
        // 5^7 mod 23 = 17
        assertEquals(17, PiGenerator.powerMod(5, 7, 23));
    }
    
    @Test
    public void powerModTest() {
        assertEquals(1, PiGenerator.powerMod(200, 0, 3)); //test n^0
        assertEquals(0, PiGenerator.powerMod(5, 0, 1)); //test modulus 1
        assertEquals(0, PiGenerator.powerMod(0, 1000, 3)); //test 0^n
        assertEquals(1, PiGenerator.powerMod(1, 1000, 3)); //test 1^n
        assertEquals(0, PiGenerator.powerMod(3, 5, 9)); //test where a^b is a positive multiple of m
        assertEquals(18, PiGenerator.powerMod(31, 13, 23)); //test where a^b is bigger than type long can hold
    }
    
    @Test
    public void computePiInHexTest() {
    	assertEquals(1,1);
    	int[] emptyPi = PiGenerator.computePiInHex(0);
    	assertEquals(0, emptyPi.length); //test for correct number of digits (0)
    	int[] imprecisePi = PiGenerator.computePiInHex(1);
    	assertEquals(1, imprecisePi.length); //test for correct number of digits (1)
    	assertEquals(PiGenerator.piDigit(1), imprecisePi[0]); //test that the lone digit is correct
    	int[] precisePi = PiGenerator.computePiInHex(1000);
    	assertEquals(1000, precisePi.length); //test for correct number of digits
    	assertEquals(PiGenerator.piDigit(1), precisePi[0]); //test first digit for fairly precise pi
    	assertEquals(PiGenerator.piDigit(11), precisePi[10]); //test middle digit for fairly precise pi
    	assertEquals(PiGenerator.piDigit(1000), precisePi[999]); //test last digit for fairly precise pi
    }
}
