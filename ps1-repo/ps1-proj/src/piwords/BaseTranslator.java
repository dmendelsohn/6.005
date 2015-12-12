package piwords;


public class BaseTranslator {
    /**
     * Converts a fractional digit array (0.abcd...) from one base to another.
     * 
     * The input array, digits, represents a number between 0 and 1 in baseA,
     * where the ith digit after the decimal point corresponds to (1 / baseA)^(i + 1) digits[i].
     * 
     * The returned array, output, represents a fractional number between 0 and 1 in baseB,
     * truncated to precisionB digits long, where the ith digit corresponds to (1 / baseB)^(i + 1) * output[i].
     *
     * For example, if baseA is 10 (decimal) and digits is {2,5}  (corresponding to the decimal number 0.25), 
     * and baseB is 2 (binary) and precisionB is 3 (3 binary digits after the decimal point, 
     * then convertBase() returns {0,1,0} (corresponding to the binary number 0.010).
     * 
     * @param digits The input array to translate. This array is not mutated.
     * @param baseA The base that the input array is expressed in. Requires baseA >=  2.
     * @param baseB The base that the input array is translated into. Requires baseB >= 2.
     * @param precisionB The number of digits of precision the output should
     *                   have.  Requires precisionB >= 0.
     * @return An array of size precisionB expressing digits in baseB.
     * @throws IllegalArgumentException if any value in digits is not a baseA digit, i.e.
     *            digits[i] < 0 or digits[i] >= baseA.
     */
    public static int[] convertBase(int[] digits, int baseA,
                                    int baseB, int precisionB) throws IllegalArgumentException{
        for (int i = 0; i < digits.length; i++) {
        	if (digits[i] < 0 || digits[i] >= baseA) {
        		throw new IllegalArgumentException("All elements of the digits array must be valid baseA digits.");
        	}
        }
        int[] result = new int[precisionB];
        int[] digitsCopy = digits.clone();
        for (int i = 0; i < precisionB; i++) {
        	int carry = 0;
        	for (int j = digits.length -1; j >= 0; j--) {
        		int x = digitsCopy[j]*baseB + carry;
        		digitsCopy[j] = x % baseA;
        		carry = x / baseA;
        	}
        	result[i] = carry;
        }
        return result;
    }
}
