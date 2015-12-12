import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit tests for RulesOf6005. Goes through the methods
 * hasFeature, testComputeGrade, and testExtendedDueDate
 *
 */
public class RulesOf6005Test {

    /**
     * Tests the hasFeature method of RulesOf6005. 
     */
	@Test
	public void testHasFeature() {
		assertTrue(RulesOf6005.hasFeature("QUIZZES"));
		assertFalse(RulesOf6005.hasFeature("quuuuuiiizes"));
		assertFalse(RulesOf6005.hasFeature("Laptops not required"));
		assertTrue(RulesOf6005.hasFeature("code review"));
	}
	
	
	/**
	 * Tests the computeGrade method of RulesOf6005.
	 */
	@Test
	public void testComputeGrade(){
		assertEquals(100, RulesOf6005.computeGrade(100, 100, 100, 100));
		assertEquals(0, RulesOf6005.computeGrade(0, 0, 0, 0));
		assertEquals(47, RulesOf6005.computeGrade(60, 40, 50, 37));
	}
	
	
	/**
	 * Tests the extendDeadline method of RulesOf6005.
	 */
	@Test
	public void testExtendDeadline(){
        Calendar duedate = new GregorianCalendar();
        duedate.clear();
        duedate.set(2011, 8, 9, 23, 59, 59);
        Calendar twoDaysAfter = new GregorianCalendar();
        twoDaysAfter.clear();
        twoDaysAfter.set(2011, 8, 11, 23, 59, 59);
        Calendar extended = RulesOf6005.extendDeadline(4, 1, duedate);
        assertEquals(extended.get(Calendar.YEAR), twoDaysAfter.get((Calendar.YEAR)));
        assertEquals(extended.get(Calendar.MONTH), twoDaysAfter.get((Calendar.MONTH)));
        assertEquals(extended.get(Calendar.DATE), twoDaysAfter.get((Calendar.DATE)));
        assertEquals(extended.get(Calendar.HOUR), twoDaysAfter.get((Calendar.HOUR)));
        assertEquals(extended.get(Calendar.MINUTE), twoDaysAfter.get((Calendar.MINUTE)));
        assertEquals(extended.get(Calendar.SECOND), twoDaysAfter.get((Calendar.SECOND)));

        duedate = new GregorianCalendar();
        duedate.clear();
        duedate.set(2011, 8, 9, 23, 59, 59);
        Calendar threeDaysAfter = new GregorianCalendar();
        threeDaysAfter.clear();
        threeDaysAfter.set(2011, 8, 11, 23, 59, 59);
        extended = RulesOf6005.extendDeadline(4, 5, duedate);
        assertEquals(extended.get(Calendar.YEAR), threeDaysAfter.get((Calendar.YEAR)));
        assertEquals(extended.get(Calendar.MONTH), threeDaysAfter.get((Calendar.MONTH)));
        assertEquals(extended.get(Calendar.DATE), threeDaysAfter.get((Calendar.DATE)));
        assertEquals(extended.get(Calendar.HOUR), threeDaysAfter.get((Calendar.HOUR)));
        assertEquals(extended.get(Calendar.MINUTE), threeDaysAfter.get((Calendar.MINUTE)));
        assertEquals(extended.get(Calendar.SECOND), threeDaysAfter.get((Calendar.SECOND)));

	}

	/**
	 * Tests the putToMultiMap and getFromMultiMap methods.
	 */
	@Test
	public void testMultimap(){
    Map<String,List<String>> multiMap = new LinkedHashMap<String,List<String>>();
    RulesOf6005.putToMultiMap(multiMap, "key1", "value1-1");
    RulesOf6005.putToMultiMap(multiMap, "key1", "value1-2");
    RulesOf6005.putToMultiMap(multiMap, "key1", "value1-2");
    RulesOf6005.putToMultiMap(multiMap, "key2", "value2-1");
    assertEquals(RulesOf6005.getFromMultiMap(multiMap, "key1"), Arrays.asList(
        new String[] {"value1-1", "value1-2", "value1-2"}));
    assertEquals(RulesOf6005.getFromMultiMap(multiMap, "key2"), Arrays.asList(
        new String[] {"value2-1"}));
    assertEquals(RulesOf6005.getFromMultiMap(multiMap, "key3"), Arrays.asList(
        new String[] {}));
  }

	/**
	 * Tests the mayUseCodeInAssignment method.
	 */
	@Test
	public void testMayUseCodeInAssignment() {
    assertEquals(false, RulesOf6005.mayUseCodeInAssignment(false, true, false, false, false));
    assertEquals(true, RulesOf6005.mayUseCodeInAssignment(true, false, true, true, true));
  }

  /**
   * Tests the getMysteryString method.
   */
  @Test
  public void testGetMysteryString() {
    final MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA1");
    } catch (NoSuchAlgorithmException e) {
      throw new UnsupportedOperationException(e);
    }
    String toTest = RulesOf6005.getMysteryString();
    try {
      md.update(toTest.getBytes("UTF8"));
    } catch (UnsupportedEncodingException e) {
      throw new UnsupportedOperationException(e);
    }
    byte actualDigest[] = md.digest();
    byte correctDigest[] =
        {78, -48, -106, 31, -42, 31, 38, 26, -86, -94, -19, -16, 110, -78, 65, 94, 2, -58, 92, -59};
    assertArrayEquals(correctDigest, actualDigest);
  }
}

