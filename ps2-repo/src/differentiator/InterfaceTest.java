package differentiator;

import org.junit.Test;

/**
 * <p>The purpose of this class is to provide a test method which verifies that
 * you're implementation does not break the interface of
 * {@code Differentiator#evaluate(String, String)}.  I.e., we're checking here,
 * that your implementation's primary method has remained the same as when you
 * cloned it.</p>
 *
 * <p>In short, if this test passes, then you'll get at least 15% credit on
 * autograder runs&mdash;make sure this test passes ^_^</p>
 * @author jamoozy a.k.a. Andrew (from recitation)
 */
public class InterfaceTest {
    @Test
    public void testPublic() {
        Differentiator d = new Differentiator();
        d.evaluate("(Hi * there)", "foo");
    }
}
