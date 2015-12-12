package differentiator;

public class Utils {
	public static String smartAdd(String s1, String s2) {
	    	if (s1.equals("0"))
	    		return s2;
	    	else if (s2.equals("0"))
	    		return s1;
	    	else
	    		return "("+s1+"+"+s2+")";
	}
	
    public static String smartMul(String s1, String s2) {
		if (s1.equals("0") || s2.equals("0")) {
			return "0";
		} else if (s1.equals("1")) {
			return s2;
		} else if (s2.equals("1")) {
			return s1;
		} else {
			return "("+s1+"*"+s2+")";
		}
    }
}
