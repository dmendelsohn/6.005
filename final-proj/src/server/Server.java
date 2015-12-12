package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.text.BadLocationException;

import shared.ChangeRequest;
import shared.ConnectResponse;
import shared.Delete;
import shared.Insert;
import shared.NewDoc;
import shared.PostResponse;
import shared.StyleChange;
import shared.UpdateRequest;
import shared.Utils;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * Server class is the main server for the Collaborative Editor.
 * 
 * Server trades messages with all clients using an HTTP protocol.
 * 
 * Documents are represented in an instance of ServerDataModel, 
 *   and this model persists on the server until the server is terminated.
 * 
 * An instance of server is hosted in the following way:
 *  ::The DEFAULT_HOST is localhost.
 *  ::Change the Server's field if this server will not be running on localhost.
 *  ::The DEFAULT_PORT is 4444.
 *  ::A Server's port can be changed by passing in an optional argument:
 *  :: ... -p [integer]
 *  ::This will set the server's port to [integer].
 *  
 * Thread safety is ensured by locking the data model's change log when it is being 
 *   iterated through to search for new edits to send back to the client.
 *  
 * We use Google's 'Gson' in this implementation to easily convert JSON objects into String representation and vice versa.
 * 
 * We use Sun's HTTP server utilities for handling HTTP connection messages.
 * 
 * @author - keanu
 */

public class Server {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 4444;
    private static int port = DEFAULT_PORT;
    private ServerDataModel model;
    private Gson gson;
    private int clientCounter;
    private int docCounter;
    
    /**
     * Constructor for Server: creates the server data model
     * and  sets initial docCounter and clientCounter to 0.
     */
    public Server() {
        model = new ServerDataModel();
        gson = new Gson();
        clientCounter = 0;
        docCounter = 0;
    }
    
    /**
     * ConnectHandler is a runnable which listens in on the /connect context 
     * and processes connection requests from new clients.
     * It assigns a clientID to the new client upon receiving this message
     * and eventually sends the generated clientID back to the client.
     */
    class ConnectHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            class ConnectRunnable implements Runnable {
                HttpExchange exchange;
                public ConnectRunnable(HttpExchange t) {
                    exchange = t;
                }
                public void run() {
                	int responseClientID;
                	synchronized (this) {
                		 responseClientID = ++clientCounter;
                	}
                    ConnectResponse cr = new ConnectResponse(responseClientID);
                    String response = gson.toJson(cr);
                    try {
                        exchange.sendResponseHeaders(200, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Thread thread = new Thread(new ConnectRunnable(t));
            thread.start();
        }
    }
    
    /**
     * PostHandler is a runnable which listens in on the /post context 
     * and processes update requests from the client. These requests
     * are parsed and cause changes to the server's data model. These
     * changes are saved in a log so that clients' future requests for
     * updates can return all new changes.
     */
    class PostHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            class PostRunnable implements Runnable {
                HttpExchange exchange;
                public PostRunnable(HttpExchange t) {
                    exchange = t;
                }
                public void run() {
                    Scanner s = new Scanner(exchange.getRequestBody()).useDelimiter("\\A");
                    boolean isPostSuccessful = false;
                    while ( s.hasNext() ) {
                        String message = s.next();
                        synchronized(this) {
                        	isPostSuccessful = parseMessage(message); //read the entire request body using "\\A" (start request) as delimiter
                        }
                    }
                    
                    PostResponse pr;
                    if (isPostSuccessful)
                    	pr = new PostResponse("success");
                    else
                    	pr = new PostResponse("failure");
                    String response = gson.toJson(pr);
                    try {
                        exchange.sendResponseHeaders(200, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            Thread thread = new Thread(new PostRunnable(t));
            thread.start();
        }
    }
    
    /**
     * GetHandler is a runnable which listens in on the /get context 
     * and processes get requests from the client. These 'get' requests
     * are called by all clients periodically to check for server data updates.
     * The request is parsed and the server responds by giving it a list
     * of any and all new changes to the data.
     */
    class GetHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            class GetRunnable implements Runnable {
                HttpExchange exchange;
                public GetRunnable(HttpExchange t) {
                    exchange = t;
                }
                public void run() {
                    java.util.Scanner s = new java.util.Scanner(exchange.getRequestBody()).useDelimiter("\\A");
                    int versionID = -1;
                    while ( s.hasNext() ) {
                        String message = s.next();
                        UpdateRequest ur = gson.fromJson(message, UpdateRequest.class);
                        versionID = ur.getVersionID();
                    }
                    if (versionID == -1) {
                    	return; //can't proceed without a versionID
                    } else {
                    	ArrayList<ChangeRequest> newEdits; 
                    	synchronized (this) {
                    		newEdits = getEdits(versionID);
                    	}
                    	ArrayList<ChangeRequestJsonHelper> newEditsFormatted = new ArrayList<ChangeRequestJsonHelper>();
                    	for (ChangeRequest req : newEdits) {
                    		newEditsFormatted.add(Utils.convertChangeRequestToHelper(req));
                    	}
                    	String response = gson.toJson(newEditsFormatted);
                    	try {
                    		exchange.sendResponseHeaders(200, response.length());
                    		OutputStream os = exchange.getResponseBody();
                    		os.write(response.getBytes());
                    		os.close();
                    	} catch(IOException e) {
                    		e.printStackTrace();
                    	}
                    }
                }
            }
            Thread thread = new Thread(new GetRunnable(t));
            thread.start();
        }
    }
    
    /**
     * The getEdits method allows the server to send back all of the edits that
     * are 'new' to the client requesting the updates, given the client's last version.
     * 
     * @param versionID Integer representing the client's last seen version from the server.
     * @return freshEdits - an ArrayList representing the edits that the client hasn't yet seen.
     */
    private ArrayList<ChangeRequest> getEdits(Integer versionID) {
    	ArrayList<ChangeRequest> freshEdits = new ArrayList<ChangeRequest>();
    	for (ChangeRequest cr: model.getLog()) {
    		if (cr.getVersionID() > versionID)
    			freshEdits.add(cr);
    	}
        return freshEdits;
    }
    
    /**
     * This method gets all edits to a specific document that are later version that a given version
     * The return list only includes insertions and deletions, no new docs or change styles
     * Furthermore, the returned list includes only edits made by users other than the one who made this edit,
     * since that user already had those edits when it requested a position
     * This helps enable OperationalTransformation
     * @param versionID - Integer representing the client's last seen version from the server.
     * @param docID - a documentID
     * @param clientID - a clientID that represents client who made the change
     * @return an ArrayList representing the edits that the client hasn't yet seen.
     */
    private ArrayList<ChangeRequest> getOtherClientsEditsWithinDoc(Integer versionID, Integer docID, Integer clientID) {
    	ArrayList<ChangeRequest> freshEdits = new ArrayList<ChangeRequest>();
    	for (ChangeRequest cr: model.getLog()) {
    		if (cr.getVersionID() > versionID && cr.getDocID() == docID && cr.getClientID() != clientID &&
    				(cr.getOperationType() == ChangeRequest.OperationType.INSERT || cr.getOperationType() == ChangeRequest.OperationType.DELETE))
    			freshEdits.add(cr);
    	}
        return freshEdits;    }

    /**
     * parseMessage takes in a client-to-server message and parses it
     * @param line : the message to parse
     */
    private boolean parseMessage(String line) {
        ChangeRequest request = Utils.convertStringToChangeRequest(line);
    	if (isValidRequest(request)) {
    		switch (request.getOperationType()) {
    		case NEWDOC:
    			Integer newDocID = ++docCounter;
    			NewDoc newDocChange = (NewDoc)request.getChange();
    			model.newDoc(newDocID, newDocChange.getTitle(), request.getClientID());
    			break;
    		case INSERT:
    			try {
    				Insert insertChange = (Insert)request.getChange();
    				int newPosition = getPositionUsingOpTrans(insertChange.getPosition(), request.getDocID(), request.getVersionID(), request.getClientID());
    				model.insert(request.getDocID(), newPosition, insertChange.getText(), request.getClientID(),
    								insertChange.isBold(), insertChange.isItalic(), insertChange.isUnderline());
    			} catch (BadLocationException e) {
    				e.printStackTrace();
    			}
    			break;
    		case DELETE:
    			try {
    				Delete deleteChange = (Delete)request.getChange();
    				int newPosition = getPositionUsingOpTrans(deleteChange.getPosition(), request.getDocID(), request.getVersionID(), request.getClientID());
    				model.delete(request.getDocID(), newPosition, deleteChange.getNumChars(), request.getClientID());
    			} catch (BadLocationException e) {
    				e.printStackTrace();
    			}
    			break;
    		default:
    			break;
    		}
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Checks if a request is valid, in that all the necessary fields are populated
     * @param request - a ChangeRequest object
     * @return true if request has the necessary field populated; false if not
     * @precondition the type of the Change field of request corresponds with the operation type
     */
    private boolean isValidRequest(ChangeRequest request) {
    	if (request.getOperationType() == null || request.getVersionID() == null || request.getClientID() == null || request.getChange() == null)
    	    return false;

    	switch(request.getOperationType()) {
    	case NEWDOC:
    		NewDoc nd = (NewDoc)request.getChange();
    		if (nd.getTitle() == null)
    			return false;
    		else
    			return true;
    	case INSERT:
    		Insert i = (Insert)request.getChange();
    		if (request.getDocID() == null || i.getPosition() == null || i.getText() == null)
    			return false;
    		else 
    			return true;
    	case DELETE:
    		Delete d = (Delete)request.getChange();
    		if (request.getDocID() == null || d.getPosition() == null || d.getNumChars() == null)
    			return false;
    		else {
    			return true;
    		}
    	case STYLECHANGE:
    		StyleChange sc = (StyleChange)request.getChange();
    		if (request.getDocID() == null || sc.getStyleType() == null || sc.getPosition() == null || sc.getNumChars() == null)
    			return false;
    		else
    			return true;
    	default:
    		return false;
    	}
    }
    
    /**
     * Transforms the position of an edit that is being processed. The position changes
     * based on all the edits that occurred between the present time and the last edit processed by the client
     * that requested this change
     * @param position - the position requested by the client
     * @param docID - the ID of the document being edited
     * @param versionID - the versionID of edit processed by the client immediately before this request was made
     * @return the new position of the edit after transformation
     */
    private Integer getPositionUsingOpTrans(Integer position, Integer docID, Integer versionID, Integer clientID) {
    	int newPosition = position;
    	ArrayList<ChangeRequest> edits = getOtherClientsEditsWithinDoc(versionID, docID, clientID);
    	for (ChangeRequest req : edits) {
    		if (req.getOperationType() == ChangeRequest.OperationType.INSERT) {
    			Insert i = (Insert)req.getChange();
    			if (i.getPosition() <= newPosition)
    				newPosition += i.getText().length();
    		} else { //it is a delete
    			Delete d = (Delete)req.getChange();
    			if ((d.getPosition() + d.getNumChars()) < newPosition) { //entirety of deleted text is before position
    				newPosition -= d.getNumChars();
    			} else if (d.getPosition() < newPosition) { //deleted text includes newPosition
    				newPosition = d.getPosition();
    			}
    		}
    	}
    	return newPosition;
    }
    
    /**
     * Main method starts a server, parses optional arguments for specifying port,
     * creates an HttpServer instance, creates post, get, and connect contexts, 
     * and finally starts the HttpServer.
     * 
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        
        //parses port arguments
        if (args.length == 2) {
            if (args[0].equals("-p")) {
                try{
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(DEFAULT_HOST, port), -1); //-1 gives default max queue size
        httpServer.createContext("/post", server.new PostHandler());
        httpServer.createContext("/get", server.new GetHandler());
        httpServer.createContext("/connect", server.new ConnectHandler());
        httpServer.setExecutor(null);
        httpServer.start();
    }
}
