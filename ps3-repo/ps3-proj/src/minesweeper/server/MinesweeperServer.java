package minesweeper.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author danielmendelsohn
 *
 *The server is thread-safe because the threads have no reference to
 *the server, and the server has no reference to the threads once they start.
 *
 *All thread-specific objects are held within the threads.  This is a good example
 *of confinement
 *
 *The threads only hold references to the board, which they can modify at will.
 *The Board class, as described in Board.java, is thread-safe.  Therefore this server
 *is thread-safe.
 *
 */
public class MinesweeperServer {
	private static final int DEFAULT_BOARD_SIZE = 10;
	private static final String HELP_MESSAGE = "Type 'look' to see the current state of the board\n" +
					"Type 'dig x y' to dig at position (x,y)\n" +
					"Type 'flag x y' to flag position (x,y)\n" +
					"Type 'deflag x y' to deflag the position (x,y)\n" +
					"Type 'bye' to disconnect\n";
	private static final String BOOM = "BOOM!\n";
	private static final String HELLO = "Welcome to Minesweeper. %d people are playing including you. Type 'help' for help.\n";
	private static final String BYE = "bye";
	
    private final ServerSocket serverSocket;
    /** True if the server should _not_ disconnect a client after a BOOM message. */
    private final boolean debug;
    private final Board board;
    private int numClients;

    /**
     * Make a MinesweeperServer that listens for connections on port.
     * @param port port number, requires 0 <= port <= 65535.
     */
    public MinesweeperServer(int port, boolean debug, Board board) throws IOException {
        serverSocket = new ServerSocket(port);
        this.debug = debug;
        this.board = board;
        numClients = 0;
    }

    /**
     * Run the server, listening for client connections and handling them.  
     * Never returns unless an exception is thrown.
     * @throws IOException if the main server socket is broken
     * (IOExceptions from individual clients do *not* terminate serve()).
     */
    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            final Socket socket = serverSocket.accept();
            Thread client = new Thread(new Runnable() {
            	@Override
            	public void run() {
                    synchronized (MinesweeperServer.this) {
                    	numClients++;
                    }
            		try {
            			handleConnection(socket);
            		} catch (IOException e) {  //BAD BAD BAD
            			e.printStackTrace(); 
            		} finally {
            			synchronized (MinesweeperServer.this) {
            				numClients--;
            			}
            			try {
            				socket.close();
            			} catch (IOException e) {
            				e.printStackTrace();
            			}
            		}
            	}
            });
            client.start();
        }
    }
    
    /**
     * Handles a single connection, this is run in a separate thread from the main() thread.
     * @param socket
     * @throws IOException
     */
    private void handleConnection(Socket socket) throws IOException {
    	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
    	out.print(String.format(HELLO, numClients));
    	out.flush();
    	String line;
    	while ((line = in.readLine()) != null) {
    		String output = handleRequest(line);
    		if (output.equals(BYE)) //cut the connection without printing
    			break;
    		out.print(output);
    		out.flush();
    		if (output.equals(BOOM) && !debug) //cut the connection after printing
    			break;
    	}
    	out.close();
    	in.close();
    }

    /**
     * handler for client input
     * 
     * make requested mutations on game state if applicable, then return 
     * appropriate message to the user.
     * 
     * @param input
     * @return if input is a valid user input string according to the problem specification
     * the String that will be sent to the user or "bye" if the input is "bye".
     * Otherwise return null
     */
    private synchronized String handleRequest(String input) {
        String regex = "(look)|(dig \\d+ \\d+)|(flag \\d+ \\d+)|" +
                "(deflag \\d+ \\d+)|(help)|(bye)";
        if(!input.matches(regex)) {
            //invalid input
            return null;
        }
        String[] tokens = input.split(" ");
        if (tokens[0].equals("look")) {
            //no modifications needed, so do nothing and we'll return the board at the bottom of this method
        } else if (tokens[0].equals("help")) {
            return HELP_MESSAGE;
        } else if (tokens[0].equals("bye")) {
            return BYE; //if thread sees this return value, it will end itself
        } else {
            int x = Integer.parseInt(tokens[1]);
            int y = Integer.parseInt(tokens[2]);
            if (tokens[0].equals("dig")) {
                boolean isBoom = board.dig(x, y);
                if (isBoom)
                	return BOOM; //if thread sees this return value, it will cut the connection iff debug is false
            } else if (tokens[0].equals("flag")) {
                board.setFlaggedValue(x, y, true);
            } else if (tokens[0].equals("deflag")) {
                board.setFlaggedValue(x, y, false);
            }
        }
        //if we get here, then we want to return a BOARD message
        return board.toString();
    }

    /**
     * Start a MinesweeperServer running on the default port (4444).
     * 
     * Usage: MinesweeperServer [DEBUG [(-s SIZE | -f FILE)]]
     * 
     * The DEBUG argument should be either 'true' or 'false'. The server should disconnect a client
     * after a BOOM message if and only if the DEBUG flag is set to 'false'.
     * 
     * SIZE is an optional integer argument specifying that a random board of size SIZE*SIZE should
     * be generated. E.g. "MinesweeperServer false -s 15" starts the server initialized with a
     * random board of size 15*15.
     * 
     * FILE is an optional argument specifying a file pathname where a board has been stored. If
     * this argument is given, the stored board should be loaded as the starting board. E.g.
     * "MinesweeperServer false -f boardfile.txt" starts the server initialized with the board
     * stored in boardfile.txt, however large it happens to be (but the board may be assumed to be
     * square).
     * 
     * The board file format, for use by the "-f" option, is specified by the following grammar:
     * 
     * FILE :== LINE+
     * LINE :== (VAL SPACE)* VAL NEWLINE
     * VAL :== 0 | 1
     * SPACE :== " "
     * NEWLINE :== "\n" 
     * 
     * If neither FILE nor SIZE is given, generate a random board of size 10x10. If no arguments are
     * specified, do the same and additionally assume DEBUG is 'false'. FILE and SIZE may not be
     * specified simultaneously, and if one is specified, DEBUG must also be specified.
     * 
     * The system property minesweeper.customport may be used to specify a listening port other than
     * the default (used by the autograder only).
     */
    public static void main(String[] args) {
        // We parse the command-line arguments for you. Do not change this method.
        boolean debug = false;
        File file = null;
        Integer size = 10; // Default size.
        try {
            if (args.length != 0 && args.length != 1 && args.length != 3)
              throw new IllegalArgumentException();
            if (args.length >= 1) {
                if (args[0].equals("true")) {
                    debug = true;
                } else if (args[0].equals("false")) {
                    debug = false;
                } else {
                    throw new IllegalArgumentException();
                }
            }
            if (args.length == 3) {
                if (args[1].equals("-s")) {
                    try {
                        size = Integer.parseInt(args[2]);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException();
                    }
                    if (size < 0)
                        throw new IllegalArgumentException();
                } else if (args[1].equals("-f")) {
                    file = new File(args[2]);
                    if (!file.isFile()) {
                        System.err.println("file not found: \"" + file + "\"");
                        return;
                    }
                    size = null;
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("usage: MinesweeperServer DEBUG [(-s SIZE | -f FILE)]");
            return;
        }
        // Allow the autograder to change the port number programmatically.
        final int port;
        String portProp = System.getProperty("minesweeper.customport");
        if (portProp == null) {
            port = 4444; // Default port; do not change.
        } else {
            port = Integer.parseInt(portProp);
        }
        try {
            runMinesweeperServer(debug, file, size, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start a MinesweeperServer running on the specified port, with either a random new board or a
     * board loaded from a file. Either the file or the size argument must be null, but not both.
     * 
     * @param debug The server should disconnect a client after a BOOM message if and only if this
     *        argument is false.
     * @param size If this argument is not null, start with a random board of size size * size.
     * @param file If this argument is not null, start with a board loaded from the specified file,
     *        according to the input file format defined in the JavaDoc for main().
     * @param port The network port on which the server should listen.
     */
    public static void runMinesweeperServer(boolean debug, File file, Integer size, int port)
            throws IOException
    {
        //build a board
    	Board board;
        if (file != null)
        	board = new Board(file);
        else if (size != null)
        	board = new Board(size);
        else
        	board = new Board(DEFAULT_BOARD_SIZE);
        MinesweeperServer server = new MinesweeperServer(port, debug, board);
        server.serve();
    }
}
