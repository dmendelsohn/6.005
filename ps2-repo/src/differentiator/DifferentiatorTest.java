package differentiator;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DifferentiatorTest {
	@Test
	public void evaluateTest() {
		//once we've built our parse tree, there aren't really any more corner cases.  we'll use
		//examples from the assignment
		Differentiator d = new Differentiator();
		assertEquals("0", d.evaluate("(3 + 2.4)", "x"));
		assertEquals("3", d.evaluate("(3 * (x + 2.4))", "x"));
		assertEquals("((3+4)*(x+x))", d.evaluate("((3 + 4) * (x * x))", "x"));
		assertEquals("0", d.evaluate("((foo + bar) + baz)", "x"));
		assertEquals("1", d.evaluate("((foo + bar) + baz)", "foo"));
		assertEquals("(2+y)", d.evaluate("((2*x    )+     (   y*x     ))", "x"));
		assertEquals("(3+(((2*x)+(2*x))+((x*x)+((1*x)*(x+x)))))",
				d.evaluate("((4 + (3 * x)) + (((2 * x) * x) + ((1 * x) * (x * x))))", "x"));
	}
}
