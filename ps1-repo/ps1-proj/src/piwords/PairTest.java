package piwords;

import static org.junit.Assert.*;

import org.junit.Test;

public class PairTest {
    
    @Test
    public void testEquals(){
        Pair<Integer, String> pair1 = new Pair<Integer, String>(5,"hello");
        Pair<Integer, String> pair2 = new Pair<Integer, String>(5,"hello");
        Pair<Integer, String> pair3 = new Pair<Integer, String>(4,"hello");
        Pair<String, String> pair4 = new Pair<String, String>("hello","world");
        
        assertTrue(pair1.equals(pair1));
        assertTrue(pair1.equals(pair2));
        assertTrue(pair2.equals(pair1));
        
        assertFalse(pair1.equals(pair3));
        assertFalse(pair3.equals(pair1));
        assertFalse(pair3.equals(pair2));
        
        
        assertFalse(pair4.equals(pair1));
        
    }
    
    @Test 
    public void testHashCode(){
        Pair<Integer, String> pair1 = new Pair<Integer, String>(40,"foo");
        Pair<Integer, String> pair2 = new Pair<Integer, String>(40,"foo");
        assertTrue(pair1.hashCode() == pair2.hashCode());
    }
    
    @Test
    
    public void testCompareTo(){
    	//Integer differentiates
        Pair<Character, Integer> pair1 = new Pair<Character,Integer>('a',2);
        Pair<Character, Integer> pair2 = new Pair<Character,Integer>('a',3);
        assertTrue(pair1.compareTo(pair2) < 0);
        
        //Character differentiates
        pair1 = new Pair<Character,Integer>('b',2);
        pair2 = new Pair<Character,Integer>('a',3);
        assertTrue(pair1.compareTo(pair2) > 0);
        
        //equal case
        pair1 = new Pair<Character,Integer>('b',3);
        pair2 = new Pair<Character,Integer>('b',3);
        assertTrue(pair1.compareTo(pair2) == 0);
    }

    
    // TODO: write more tests for compareTo (Problem 6.a)
    

}
