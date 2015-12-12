package piwords;

public class DigitsToStringConverter {
    /**
     * Given a list of digits, a base, and a mapping of digits of that base to
     * chars, convert the list of digits into a character string by applying the
     * mapping to each digit in the input.
     * 
     * For example, let digits = {1,0,2,0}, base = 3, and alphabet = {'a','j','v'},
     * then covertDigitsToString returns "java".
     *
     * @param digits A list of digits to encode. This object is not mutated.
     * @param base The base the digits are encoded in.
     * @param alphabet The mapping of digits to chars. This object is not
     *                 mutated. Requires alphabet.length == base. 
     * @return A String encoding the input digits with alphabet.
     * @throws IllegalArgumentException if any value in digits is not a valid digit in the given base, i.e.
     *            digits[i] < 0 or digits[i] >= base.
     */
    public static String convertDigitsToString(int[] digits, int base,
                                               char[] alphabet) throws IllegalArgumentException {
        for (int element : digits) {
        	if (element < 0 || element >= base) {
        		throw new IllegalArgumentException(
        				"All elements of the digits array must be digits in base");
        	}
        }
        StringBuilder sb = new StringBuilder();
        for (int element : digits) {
        	sb.append(alphabet[element]);
        }
        return sb.toString();
    }
}
