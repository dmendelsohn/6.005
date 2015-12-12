package minesweeper.server;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.junit.Before;
import org.junit.Test;

/** Based on BoomTest. Ensures that students got the sense of X,Y directions right. */
public class PublishedTest {
	@Before
	public void setUp() {
		TestUtil.startServer(true, "board_file_5");
	}

	@Test(timeout=10000)
	public void publishedTest() throws IOException, InterruptedException {
		// avoid race where we try to connect to server too early
		Thread.sleep(100);
		Socket sock;

		try {
			sock = new Socket("127.0.0.1",4444);
			sock.setSoTimeout(3000);
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			PrintWriter out = new PrintWriter(sock.getOutputStream(),true);
			
			assertEquals(true, nextNonEmptyLine(in).startsWith("Welcome"));
      // This particular test ignores extraneous newlines; other tests may not.
      out.println("look");
      assertEquals("- - - - - - -", nextNonEmptyLine(in));
      assertEquals("- - - - - - -", nextNonEmptyLine(in));
      assertEquals("- - - - - - -", nextNonEmptyLine(in));
      assertEquals("- - - - - - -", nextNonEmptyLine(in));
      assertEquals("- - - - - - -", nextNonEmptyLine(in));
      assertEquals("- - - - - - -", nextNonEmptyLine(in));
      assertEquals("- - - - - - -", nextNonEmptyLine(in));
			out.println("dig 3 1");
      assertEquals("- - - - - - -", nextNonEmptyLine(in));
      assertEquals("- - - - - - -", nextNonEmptyLine(in));
      assertEquals("- - - - - - -", nextNonEmptyLine(in));
      assertEquals("- 1 - - - - -", nextNonEmptyLine(in));
      assertEquals("- - - - - - -", nextNonEmptyLine(in));
      assertEquals("- - - - - - -", nextNonEmptyLine(in));
      assertEquals("- - - - - - -", nextNonEmptyLine(in));
			out.println("dig 4 1");
			assertEquals("BOOM!", nextNonEmptyLine(in));
      out.println("look"); // Debug is true.
      assertEquals("          1 -", nextNonEmptyLine(in));
      assertEquals("          1 1", nextNonEmptyLine(in));
      assertEquals("             ", nextNonEmptyLine(in));
      assertEquals("             ", nextNonEmptyLine(in));
      assertEquals("             ", nextNonEmptyLine(in));
      assertEquals("             ", nextNonEmptyLine(in));
      assertEquals("             ", nextNonEmptyLine(in));
      out.println("bye");
			sock.close();
		} catch (SocketTimeoutException e) {
      throw new RuntimeException(e);
		}
	}

  private String nextNonEmptyLine(BufferedReader in) throws IOException {
    while (true) {
      String ret = in.readLine();
      if (ret == null || !ret.equals("")) {
        return ret;
  		}
    }
  }
}