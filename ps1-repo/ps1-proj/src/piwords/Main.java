/*
 * NOTE: You don't need to modify any part of this file to complete the
 * assignment! This file can be run to check your progress.
 */

package piwords;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Main {
    public static final int PI_PRECISION = 10000;
    public static final String FREQ_FILE= "txt_files/dict-freq.txt";
   
    //Copy of the Ubuntu dictionary found in /usr/share/dict/word 
    //with compound words and accented words filtered out.
    public static final String WORD_FILE= "txt_files/dict.txt";
    
    public static final char[] BASIC_ALPHABET =
        {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
         'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static void main(String[] args) {
        System.out.println("Problem 1: Calculating Pi...");
        int[] piHexDigits = PiGenerator.computePiInHex(PI_PRECISION);
        System.out.printf(
                "Digits of Pi in base-16: %s\n\n",
                MaybeTruncateString(Arrays.toString(piHexDigits), 50));

        System.out.println("Problem 2: Translating Pi to base-26...");
        int[] translatedPiBase26 =
                BaseTranslator.convertBase(piHexDigits, 16, 26, PI_PRECISION);
        System.out.printf(
                "Digits of Pi in base-26: %s\n\n",
                MaybeTruncateString(Arrays.toString(translatedPiBase26), 50));
        
        System.out.println("Problem 3: Converting Pi using basic alphabet");
        String basicConversion = DigitsToStringConverter.convertDigitsToString(
                translatedPiBase26, 26, BASIC_ALPHABET);
        System.out.printf(
                "Digits of Pi translated into a-z: %s\n\n",
                MaybeTruncateString(basicConversion, 50));

        System.out.println("Problem 4: Getting word matches");
        
        String[] wordList;
        try{
            wordList= readWordFile(WORD_FILE);
        }catch(IOException e){
            e.printStackTrace();
            wordList=null;
        }
        Map<String, Integer> basicSubstrings =
                WordFinder.findWords(basicConversion, wordList);
        for (Map.Entry<String, Integer> entry : basicSubstrings.entrySet()) {
            printWithContext(basicConversion, entry.getValue(),
                             entry.getKey(), 3, true);
        }
        System.out.printf("Word coverage using basic alphabet: %f\n\n",
                ((double)basicSubstrings.size()) / wordList.length);
        
        System.out.println("Problem 5: Getting word matches with base-100 and" +
        		           " frequency dictionary");
        int[] translatedPiBase100 =
                BaseTranslator.convertBase(piHexDigits, 16, 100, PI_PRECISION);
        List<Pair<Character,Integer>> charFreq;
        try{
            charFreq = readFreqFile(FREQ_FILE);
        }catch(IOException e){
            e.printStackTrace();
            charFreq = null;
        }
        char[] alphabet = AlphabetGenerator.generateFrequencyAlphabet(charFreq);
        
        System.out.printf("Frequency dictionary generated: %s\n",
                          MaybeTruncateString(Arrays.toString(alphabet), 50));
        
        String frequencyConversion =
                DigitsToStringConverter.convertDigitsToString(
                        translatedPiBase100, 100, alphabet);
        System.out.printf(
                "Digits of Pi translated into a-z: %s\n",
                MaybeTruncateString(frequencyConversion, 50));
        
        Map<String, Integer> frequencySubstrings =
                WordFinder.findWords(frequencyConversion, wordList);
        
        List<Pair<Integer, String>> foundWords = new ArrayList<Pair<Integer, String>>();
        for (Map.Entry<String, Integer> entry :
             frequencySubstrings.entrySet()) {
            printWithContext(frequencyConversion, entry.getValue(),
                             entry.getKey(), 3, true);
            foundWords.add(new Pair<Integer, String>(entry.getValue(),entry.getKey()));
        }
        System.out.printf("Word coverage using frequency alphabet: %f\n\n",
                ((double)frequencySubstrings.size()) / wordList.length);
        
        System.out.println("Problem 6: Sorting word matches by occurance in pi.");
        
        Collections.sort(foundWords);
        System.out.println("First 10 words found in Pi.");
        for(int i=0; i<Math.max(10, foundWords.size());i++){
            System.out.printf("\t "+i+". %s \n", MaybeTruncateString(foundWords.get(i).second, 50) );
        }
        
    }
    
    /**
     * Given a file path to a text file containing words separated by new line
     * characters return a String[] containing the words.
     * 
     * For example, provided a wordFilePath to txt_files/sample.txt the method should
     * return an array containing only strings {'java', 'is', 'fun'} in any order.
     * 
     * @param wordFilePath to a text file with a list of words separated
     *        by the new line character, words in the file are required to be 
     *        in lower case and only contain the standard set of letters,
     *        in other words just the letters a-z. 
     * @return String[] of all the words found in the file in any order.
     * @throws IOException when the file specified in wordFilePath cannot
     *         be read or formatted incorrectly. 
     */
    
    public static String[] readWordFile(String wordFilePath) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(wordFilePath));
        List<String> wordFile = new ArrayList<String>();
        String word = reader.readLine();
        while(word != null){
            wordFile.add(word);
            word = reader.readLine();
        }
        reader.close();
        return wordFile.toArray(new String[]{});
    }
    
    /**
     * Given a file path to a text file with the following format for each line,
     * single character followed by a space and then an integer value, the method
     * reads the lines and creates a Character,Integer pair for each line and then
     * returns the list of pairs found within the text file. 
     * 
     * For example, provided a freqFilePath to txt_files/sample-freq.txt the method should
     * return an List of Character, Integer pairs that looks similar to this.
     *      {('j',1),('a', 2),('v',1),('i',1),('s',1),('f',1),('u',1),('n',1)} 
     * in any order.
     * 
     * @param freqFilePath to a text file with a list of words separated
     *        by the new line character, words in the file are required to be 
     *        in lower case and only contain the standard set of letters,
     *        in other words just the letters a-z. 
     * @return String[] of all the words found in the file in any order.
     * @throws IOException when the file specified in freqFilePath cannot
     *         be read or is formatted incorrectly.  
     *         NumberFormatException thrown when second field in a line 
     *         is not an integer. 
     */
    public static List<Pair<Character,Integer>> readFreqFile(String freqFilePath)
            throws IOException, NumberFormatException{
        BufferedReader reader = new BufferedReader(new FileReader(freqFilePath));
        List<Pair<Character,Integer>> freqFile = new ArrayList<Pair<Character,Integer>>();
        String line = reader.readLine();
        while(line != null){
            String[] vals = line.split(" ");
            if(vals[0].length() != 1 || vals.length != 2){
                reader.close();
                throw new IOException("Freq file not formatted correctly");
            }
            freqFile.add(new Pair<Character,Integer>(vals[0].charAt(0), 
                    Integer.parseInt(vals[1])));
            line = reader.readLine();
        }
        reader.close();
        return freqFile;
    }
    
    
    /**
     * If the input is less than or equal to len letters long, return it
     * unchanged. If the input is greater than len letters long, trim it to
     * len letters, then add an ellipses to the end.
     * 
     * @param input String to maybe truncate.
     * @param len Length to truncate to.
     * @return The input, potentially truncated to len letters with a trailing
     *         ellipses.
     */
    public static String MaybeTruncateString(String input, int len) {
        return ((input.length() > len) ? input.substring(0, len) : input) +
               ((input.length() > len) ? "..." : "");
    }

    /**
     * Pretty print a substring of a string with some context information to
     * either side.
     * 
     * @param haystack String to print from.
     * @param offset Index to start the substring in haystack.
     * @param needle The substring that should be printed from haystack.
     * @param shouldOffset If true, offset output with a leading tab char.
     * @param numContext Max amount of context characters to take from either
     *                   end of the substring.
     */
    public static void printWithContext(String haystack, int offset,
                                        String needle, int numContext,
                                        boolean shouldOffset) {
        if (offset < 0 || offset + needle.length() > haystack.length() ||
            needle.length() < 0) {
            return;
        }

        int substringStart = Math.max(0, offset - numContext);
        int substringEnd = Math.min(haystack.length(),
                                    offset + needle.length() + numContext);

        System.out.printf("%sSubstring '%s' found at index %d: %s%s%s\n",
                          (shouldOffset) ? "\t" : "",
                          needle, offset,
                          (substringStart > 0) ? "..." : "",
                          haystack.substring(substringStart, substringEnd),
                          (substringEnd < haystack.length()) ? "..." : "");
    }
}
