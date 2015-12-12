package minesweeper.server;

// yeah, this doesn't compile in ps6-njoliat, because my package 
// structure's wrong there.
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import minesweeper.server.MinesweeperServer;

public class TestUtil {
  /** Return the absolute path of the specified file resource on the current Java classpath. Throw
  an exception if a valid path to an existing file cannot be produced for any reason. */
	public static String getResourcePathName(String fileName) throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (classLoader == null)
      throw new IOException("Failed to get classloader");
    URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
    if (url == null)
      throw new IOException("Failed to locate resource " + fileName);
    File file;
    try {
      file = new File(url.toURI());
    } catch (URISyntaxException e) {
      throw new IOException("Invalid resource URI type: " + e);
    }
    String ret = file.getAbsolutePath();
    if (!new File(ret).exists())
      throw new IOException("File " + ret + " does not exist");
    return ret;
  }

  public static void startServer() {
    startServer(new String[] {});
  }

  public static void startServer(boolean debug) {
    startServer(new String[] { debug ? "true" : "false" });
  }

  public static void startServer(boolean debug, String boardFileResource) {
    String resourcePathName;
    try {
      resourcePathName = getResourcePathName("minesweeper/server/" + boardFileResource);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    startServer(new String[] { debug ? "true" : "false", "-f", resourcePathName });
  }

  public static void startServer(boolean debug, int size) {
    startServer(new String[] { debug ? "true" : "false", "-s", Integer.toString(size) });
  }

	private static void startServer(String args[]) {
		final String myArgs[] = args;
		new Thread(new Runnable() {
			public void run() {
				try {
					MinesweeperServer.main(myArgs);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		}).start();
	}
	
	public static boolean eqNoSpace(String s1, String s2) {
		System.out.println("ex "+s1);
		System.out.println("ac "+s2);
		return s1.replaceAll("\\s+", "").equals(s2.replaceAll("\\s+", ""));
	}
}