package piwords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlphabetGenerator {
    /**
     * 
     * Given a List of Character, Integer pairs, return a char[] 
     * with the same character frequencies in ascending lexicographic order . 
     * 
     * For example, let the list of character, integer pairs be {('b', 2), ('a',1), ('c',4) }, 
     * then the output should be {'a', 'b', 'b', 'c', 'c', 'c', 'c'}. 
     * 
     * Note the method should not return {'b', 'b', 'a', 'c', 'c', 'c', 'c'}
     * because the order is incorrect. 
     * 
     * @param frequencies; List of Character, Integer pairs that list the
     *        character count for a given file; requires the Integer of any given pair
     *        to be greater than or equal to 0.
     * @return A char[] in ascending lexicographic order with the character
     *         frequencies described in the input.
     */
    
    public static char[] generateFrequencyAlphabet(List<Pair<Character, Integer>> frequencies){
    	//first put all the characters with multiplicity into an ArrayList
        ArrayList<Character> resultList = new ArrayList<Character>();
    	for (Pair<Character,Integer> pair : frequencies) {
        	for (int i = 0; i < pair.second; i++) {
        		resultList.add(pair.first);
        	}
    	}
    	
    	//sort the ArrayList
    	Collections.sort(resultList);
    	
    	//turn ArrayList<Character> into Character[]
    	Character[] arrayOfCharacter = resultList.toArray(new Character[resultList.size()]);
    	
    	//turn Character[] into char[]
    	char[] finalResult = new char[arrayOfCharacter.length];
    	for (int i = 0; i < arrayOfCharacter.length; i++)
    		finalResult[i] = arrayOfCharacter[i];
    	
    	//return result
    	return finalResult;
    }
}
