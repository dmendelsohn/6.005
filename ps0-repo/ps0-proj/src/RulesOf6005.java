import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * RulesOf6005 represents some of the rules of 6.005 as described by the 
 * general information on Stellar. This class also contains some unrelated
 * methods to be implemented as warm-up tasks in Problem Set 0.
 * 
 * The course elements are described by the hasFeature method. 
 * 
 * The grade details are described by the computeGrade method.
 * 
 * The extension policy (slack days) is described by the extendDeadline method.
 * 
 * The collaboration policy is described by the mayUseCodeInAssignment method.
 * 
 */
public class RulesOf6005 {
	
	//this array holds the features with the exact capitalization used on the website
	public final static String[] FEATURES = new String[] {
		"Lectures",
		"Recitations",
		"Laptops required",
		"Text",
		"Problem sets",
		"Code review",
		"Beta and Final Submission",
		"Projects",
		"Team meetings",
		"Quizzes"};
	
	//this array holds the fraction of the grade due to Quizzes, Problem sets, Projects, and Code review (in that order).
	public final static float[] GRADING_RUBRIC = new float[] {.20f, .45f, .30f, .05f};
	
	//this variable holds the total number of slack days allowed for the course
	public final static int TOTAL_SLACK_DAYS_ALLOWED = 8;
	
    /**
     * Tests if the string is one of the items in the Course Elements section. 
     *  
     * @param name - the element to be tested
     * @return true if <name> appears in bold in Course Elements section.
     *         Ignores case (capitalization). 
     * Example: "Lectures" and "lectures" will both return true.
     */
    public static boolean hasFeature(String name){
    	for (int i = 0; i < FEATURES.length; i++) {
    		if (FEATURES[i].equalsIgnoreCase(name))
    			return true;
    	}
        return false;
    }
   
    /**
     * Takes in the quiz, pset, project, and code review grades as values out of
     * a hundred and returns the grade based on the course information also as a
     * value out of a hundred, rounded to the nearest integer. 
     * 
     * Behavior is unspecified if the values are out of range.
     * 
     * @param quiz
     * @param pset
     * @param project
     * @param codeReview
     * @return the resulting grade out of a hundred
     */
    public static int computeGrade(
            int quiz, int pset, int project, int codeReview)
    {
    	return Math.round((quiz * GRADING_RUBRIC[0] + pset * GRADING_RUBRIC[1] + project * GRADING_RUBRIC[2] + codeReview * GRADING_RUBRIC[3]));
    }
  
    /**
     * Based on the slack day policy, returns a date of when the assignment
     * would be due, making sure not to exceed the budget. In the case of the
     * request being more than what's allowed, the latest possible due date is
     * returned. 
     * 
     * Hint: Take a look at
     * http://download.oracle.com/javase/6/docs/api/java/util/GregorianCalendar.html
     * 
     * Behavior is unspecified if request is negative, if alreadyUsed is
     * negative, or duedate is null. 
     * 
     * @see java.util.GregorianCalendar
     * @param request - the requested number of slack days to use
     * @param alreadyUsed - the number of slack days already used earlier in the
     *        semester
     * @param duedate - the original due date of the assignment. Should not be
     *        modified by this method.
     * @return a new instance of a Calendar with the date and time set to when
     *         the assignment will be due
     */
    public static Calendar extendDeadline(
            int request, int alreadyUsed, Calendar duedate)
    {
    	int numSlackDaysGranted = request; //numSlackDaysGranted is equal to request by default, and is adjusted below
        if (request > TOTAL_SLACK_DAYS_ALLOWED - alreadyUsed) //not enought slack days, set numSlackDaysGranted to num days remaining, enforcing non-negativity
        	numSlackDaysGranted = Math.max(0, TOTAL_SLACK_DAYS_ALLOWED - alreadyUsed);
        if (numSlackDaysGranted > 2) //requested (and had available) more than 2 slack days, numSlackDaysGranted pulled down to 2
        	numSlackDaysGranted = 2;
        Calendar newDeadline = (Calendar)duedate.clone();
        newDeadline.add(Calendar.DATE, numSlackDaysGranted);
        return newDeadline;
    }
  
    /**
     * Judge whether a given piece of code may be used in an assignment (problem
     * set or team project) or not, according to the 6.005 collaboration policy.
     * 
     * @param writtenByYourself true if the code in question was written by
     *        yourself or, in the case of a team project, your teammates,
     *        otherwise false.
     * @param availableToOthers if not writtenByYourself, whether or not the
     *        code in question is available to all other students in the class.
     *        Otherwise ignored.
     * @param writtenAsCourseWork if not writtenByYourself, whether or not the
     *        code in question was written specifically as part of a solution to
     *        a 6.005 assignment, in the current or past semesters. Otherwise
     *        ignored.
     * @param citingYourSource if not writtenByYourself, whether or not you
     *        properly cite your source. Otherwise ignored.
     * @param implementationRequired whether the assignment specifically asks
     *        you to implement the feature in question.
     * @return Whether or not, based on the information provided in the
     *         arguments, you are likely to be allowed to use the code in
     *         question in your assignment, according to the 6.005 collaboration
     *         policy for the current semester.
     */
    public static boolean mayUseCodeInAssignment(boolean writtenByYourself,
            boolean availableToOthers, boolean writtenAsCourseWork,
            boolean citingYourSource, boolean implementationRequired) {
        return (writtenByYourself || (availableToOthers && !writtenAsCourseWork && citingYourSource && !implementationRequired));
    }
  
    /**
     * Adds a value associated with the specified key to a multimap.
     * 
     * A multimap is a Map with slightly different semantics. Putting a value
     * into the map will add the value to a List at that key. Getting a value
     * will return a List, holding all the values put to that key, in the order
     * they were put. (Adapted from Apache Commons Collections.)
     * 
     * @see java.util.Map
     * @see java.util.List
     * @param multiMap a multimap modeled as a regular map from keys to
     *        modifiable lists of values.
     * @param key the key to store against
     * @param the value to add to the list at the key
     */
    public static void putToMultiMap(
            Map<String,List<String>> multiMap, String key, String value)
    {
        if (multiMap.containsKey(key)) {
        	multiMap.get(key).add(value);
        } else {
        	List<String> newList = new ArrayList<String>();
        	newList.add(value);
        	multiMap.put(key, newList);
        }
    }
  
    /**
     * Gets the list of values associated with the specified key in a multimap.
     * The multiMap should not be modified as a result of calling this method or
     * modifying the returned list.
     * 
     * A multimap is a Map with slightly different semantics. Putting a value
     * into the map will add the value to a List at that key. Getting a value
     * will return a List, holding all the values put to that key, in the order
     * they were put. (Adapted from Apache Commons Collections.)
     * 
     * @see java.util.Map
     * @see java.util.List
     * @param multiMap a multimap modeled as a regular map from keys to lists of
     *        values.
     * @param key the key to retrieve values for
     * @return a list of values associated with the key, or an empty list if no
     *         values have been associated with the key.
     */
    public static List<String> getFromMultiMap(
            Map<String,List<String>> multiMap, String key)
    {
        if (multiMap.containsKey(key)) {
        	return multiMap.get(key);
        } else {
        	return new ArrayList<String>();
        }
    }
  
    /**
     * Return a mysterious string. Don't try to implement this method; we will
     * provide you with an updated version of RulesOf6005.java that contains the
     * correct implementation. This way we can ensure that everyone knows how to
     * fetch corrections to the assignments.
     */
    public static String getMysteryString() {
        return "Mysterious, indeed!";
    }
  
    /**
     * Main method of the class. Runs the methods hasFeature, computeGrade, 
     * extendDeadline, mayUseCodeInAssignment, putToMultiMap, and
     * getFromMultiMap. 
     * 
     * @param args
     */
    public static void main(String[] args){
        System.out.println("Has feature QUIZZES: " + hasFeature("QUIZZES"));
        System.out.println("My grade is: " + computeGrade(60, 40, 50, 37));
        Calendar duedate = new GregorianCalendar();
        duedate.set(2011, 8, 9, 23, 59, 59);
        System.out.println("Original due date: " + duedate.getTime());
        System.out.println("New due date: " +
            extendDeadline(4, 1, duedate).getTime());
        System.out.println("You may certainly use code you wrote yourself: " +
            RulesOf6005.mayUseCodeInAssignment(true, false, true, true, true));
        Map<String,List<String>> multiMap =
            new LinkedHashMap<String,List<String>>();
        RulesOf6005.putToMultiMap(multiMap, "key1", "value1-1");
        RulesOf6005.putToMultiMap(multiMap, "key1", "value1-2");
        RulesOf6005.putToMultiMap(multiMap, "key1", "value1-2");
        RulesOf6005.putToMultiMap(multiMap, "key2", "value2-1");
        System.out.println("My multimap: " + multiMap);
        System.out.println("A key in my multimap: " +
            RulesOf6005.getFromMultiMap(multiMap, "key1"));
    }
}

