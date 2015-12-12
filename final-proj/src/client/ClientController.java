package client;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.text.BadLocationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import server.ChangeRequestJsonHelper;
import shared.Change;
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
import com.google.gson.reflect.TypeToken;
/**
 * This class handles the connection to the server.  This entails
 * sending requests to the server as triggered by ClientDataModel,
 * and retrieving updates from the server (which are used to update
 * the VerifiedClientDataModel)
 * 
 * HTTP protocol is used.
 * 
 * @author dmendels
 *
 */
public class ClientController {
	private static final String DEFAULT_HOST = "127.0.0.1";
	private static final int DEFAULT_PORT = 4444;
	private static final int POLL_FREQ = 100;
	private static final int FORCE_SYNC_FREQ = 20000;
	private String host;
	private int port;
	private Gson gson;

	private VerifiedClientDataModel model;
	private int clientID;
	private int version;

	private URL getURL;
	private URL postURL;
	private URL connectURL;

	/**
	 * Four overloaded constructors for ClientController:
	 * They each take in a VerifiedClientDataModel, 
	 * and optionally allow host and port arguments.
	 * 
	 * After setting the appropriate fields, init() is called.
	 * 
	 * @param model - the server-verified data model which the client is tied to.
	 * @param h - an optional host designation (String)
	 * @param p - an optional port designation (int)
	 */
	ClientController(VerifiedClientDataModel model, String h, int p) {
	    host = h;
	    port = p;
	    init(model);
	}
	
	ClientController(VerifiedClientDataModel model, int p) {
	    host = DEFAULT_HOST;
	    port = p;
	    init(model);
	}
	
	ClientController(VerifiedClientDataModel model, String h) {
	    host = h;
	    port = DEFAULT_PORT;
	    init(model);
	}

	ClientController(VerifiedClientDataModel model) {
	    host = DEFAULT_HOST;
	    port = DEFAULT_PORT;
	    init(model);
	}

	/**
	 * The init() method is called by all ClientController constructors.
	 * 
	 * Three URLs are generated: get, post, and connect as contexts for the
	 *     three different message types to send over HTTP.
	 * Then, a connect request is sent to the server. When it returns, the client
	 *     is able to store it's own clientID that the server assigns to it.
	 * After the connect, the client constantly polls the server for changes.
	 * The frequency of these polls is indicated using the POLL_FREQ static field.
	 * 
	 * @param model - the model to tie the client to.
	 */
	private void init(VerifiedClientDataModel model) {
	    this.model = model;
	    this.version = 0;
	    this.gson = new Gson();

	    try{
	        getURL = new URL("http", host, port, "/get");

	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    try{
	        postURL = new URL("http", host, port, "/post");

	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    try{
	        connectURL = new URL("http", host, port, "/connect");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }


	    getClientIDFromServer();
	    startPollForUpdates();
	    startSyncChecker();
	}

	/**
	 * Called upon initialization, sends ID request to server and receives the ID which
	 * the server assigns to this client instance.
	 */
	private void getClientIDFromServer() {
	    try {
            HttpClient pc = new DefaultHttpClient();
            HttpPost post = new HttpPost(connectURL.toExternalForm());
            
            post.setEntity(new StringEntity("ID request"));
            
            HttpResponse response = pc.execute(post);
            
            Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
            while ( s.hasNext() ) {
                String message = s.next();
                parseConnectResponse(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * Called as last part of client initialization.
	 * Starts a thread which loops until client is killed.
	 * This thread polls for updates from the server every [POLL_FREQ] milliseconds.
	 * If updates are returned from the server, it parses the updates
	 *     to correctly update the client's VerifiedClientDataModel.
	 */
	private void startPollForUpdates() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while(true) {
				    try {
			            HttpClient pc = new DefaultHttpClient();
			            HttpPost post = new HttpPost(getURL.toExternalForm());
			            
			            UpdateRequest request = new UpdateRequest(null, version);
			            post.setEntity(new StringEntity(gson.toJson(request)));
			            
			            HttpResponse response = pc.execute(post);
			            
			            Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
			            while ( s.hasNext() ) {
			                String message = s.next();
			                parseGetResponse(message);
			            }
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
					
					try {
						Thread.sleep(POLL_FREQ);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}

	private void startSyncChecker() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while(true) {
					try {
						Thread.sleep(FORCE_SYNC_FREQ);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					model.pushAllDocs();
				}
			}
		});
		t.start();
	}
	
	/**
	 * Handles a response from the server (acknowledgment) when 
	 *     a data modification ('post' message) is signaled by the client.
	 *     
	 * @param message
	 */
	private void parsePostResponse(String message) {
		PostResponse pr = gson.fromJson(message, PostResponse.class);
		if (pr.getMessage().equals("failure")) {
			System.out.println("Post failed");
		}
	}
	
	/**
	 * Handles a response from the server containing a clientID when
	 *     the client connects to the server over the 'connect' context.
	 *     
	 * @param message
	 */
	private void parseConnectResponse(String message) {
		ConnectResponse response = gson.fromJson(message, ConnectResponse.class);
		clientID = response.getClientID();
		model.setClientID(clientID);
	}

	/**
	 * Parses a message from the server, and updates the VerifiedClientDataModel appropriately
	 * @param line - the message from the server (see protocol)
	 */
	private void parseGetResponse(String message) {
		Type listType = new TypeToken<List<ChangeRequestJsonHelper>>(){}.getType();
		ArrayList<ChangeRequestJsonHelper> requests = gson.fromJson(message, listType);
		for (ChangeRequestJsonHelper helper : requests) {
			ChangeRequest request = Utils.convertHelperToChangeRequest(helper);
			if (isValidRequest(request)) {
				version = request.getVersionID();
				switch (request.getOperationType()) {
				case NEWDOC:
					NewDoc newDocChange = (NewDoc)request.getChange();
					model.newDoc(request.getDocID(), newDocChange.getTitle(), request.getClientID());
					break;
				case INSERT:
					Insert insertChange = (Insert)request.getChange();
					try {
						model.insert(request.getDocID(), insertChange.getPosition(), insertChange.getText(), request.getClientID(),
										insertChange.isBold(), insertChange.isItalic(), insertChange.isUnderline());
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
					break;
				case DELETE:
					Delete deleteChange = (Delete)request.getChange();
					try {
						model.delete(request.getDocID(), deleteChange.getPosition(), deleteChange.getNumChars(), request.getClientID());
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
					break;
				case STYLECHANGE:
					StyleChange styleChange = (StyleChange)request.getChange();
					try {
						model.insert(request.getDocID(), styleChange.getPosition(), "", request.getClientID(),
								false, false, false);		
						} catch (BadLocationException e) {
						e.printStackTrace();
					}
				default:
					break;
				}
			}
		}
	}
	
	/**
	 * Checks the change request to see if it is a valid request.
	 * 
	 * @param request - ChangeRequest object to be checked
	 * @return boolean (true if valid request)
	 */
    private boolean isValidRequest(ChangeRequest request) {
    	if (request.getOperationType() == null || request.getVersionID() == null || request.getClientID() == null || request.getChange() == null)
    		return false;

    	switch(request.getOperationType()) {
    	case NEWDOC:
    		if (request.getDocID() == null || !(request.getChange() instanceof NewDoc))
    			return false;
    		else {
    			NewDoc nd = (NewDoc)request.getChange();
    			if (nd.getTitle() == null)
    				return false;
    			else
    				return true;
    		}
    	case INSERT:
    		if (request.getDocID() == null || !(request.getChange() instanceof Insert))
    			return false;
    		else {
    			Insert i = (Insert)request.getChange();
    			if (i.getPosition() == null || i.getText() == null)
    				return false;
    			else 
    				return true;
    		}
    	case DELETE:
    		if (request.getDocID() == null || !(request.getChange() instanceof Delete))
    			return false;
    		else {
    			Delete d = (Delete)request.getChange();
    			if (d.getPosition() == null || d.getNumChars() == null)
    				return false;
    			else 
    				return true;
    		}
    	case STYLECHANGE:
    		if (request.getDocID() == null || !(request.getChange() instanceof StyleChange))
    			return false;
    		else {
    			StyleChange sc = (StyleChange)request.getChange();
    			if (sc.getStyleType() == null || sc.getPosition() == null || sc.getNumChars() == null)
    				return false;
    			else 
    				return true;
    		}
    	default:
    		return false;
    	}
    }

	/**
	 * Sends an insert request to the server
	 * @param docID - the docID of the document being edited
	 * @param position - the position within that document at which the insertion occurs
	 * @param text - the text being inserted
	 * @param isBold - whether or not that text is bold
	 * @param isItalic - whether or not that text is italic
	 * @param isUnderline - whether or not that text is underlined
	 */
	public void sendInsertRequest(Integer docID, Integer position, String text, boolean isBold, boolean isItalic, boolean isUnderline) {
		Change change = new Insert(position, text, isBold, isItalic, isUnderline);
	    ChangeRequest request = new ChangeRequest(ChangeRequest.OperationType.INSERT, docID, clientID, version, change);
	    sendPost(request);
	}
	
	/**
	 * Sends a delete request to the server
	 * @param docID - the docID of the document being edited
	 * @param position - the position within that document at which the deletion starts
	 * @param numChars - the number of chararacters deleted
	 */
	public void sendDeleteRequest(Integer docID, Integer position, Integer numChars) {
		Change change = new Delete(position, numChars);
		ChangeRequest request = new ChangeRequest(ChangeRequest.OperationType.DELETE, docID, clientID, version, change);
		sendPost(request);
	}
	
	/**
	 * Sends a style change request to the server
	 * @param docID - the docID of the document being edited
	 * @param styleType - the StyleType of the style change
	 * @param isEnabling - true indicates that style is being enabled, false indicates it is being disabled
	 * @param position - the position at which this change begins
	 * @param numChars - the number of characters to be changed
	 */
	public void sendStyleChangeRequest(Integer docID, StyleChange.StyleType styleType, boolean isEnabling, Integer position, Integer numChars) {
		Change change = new StyleChange(styleType, isEnabling, position, numChars);
		ChangeRequest request = new ChangeRequest(ChangeRequest.OperationType.STYLECHANGE, docID, clientID, version, change);
		sendPost(request);
	}

	/**
	 * Sends a new document request to the server
	 * @param title - the title of that new document
	 */
	public void sendNewDocRequest(String title) {
		Change change = new NewDoc(title);
	    ChangeRequest request = new ChangeRequest(ChangeRequest.OperationType.NEWDOC, null, clientID, version, change);
		sendPost(request);
	}
	
	/**
	 * Helper method to send a generic post request to the server
	 * @param request - an instance of ChangeRequest that is converted into JSON and sent to the server
	 */
	public void sendPost(ChangeRequest request) {
		final ChangeRequest x = request;
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					ChangeRequestJsonHelper requestHelper = Utils.convertChangeRequestToHelper(x);
					
					HttpClient pc = new DefaultHttpClient();
					HttpPost post = new HttpPost(postURL.toExternalForm());
					String message = gson.toJson(requestHelper);
					post.setEntity(new StringEntity(message));
					
					HttpResponse response = pc.execute(post);
					
					Scanner s = new Scanner(response.getEntity().getContent()).useDelimiter("\\A");
					while ( s.hasNext() ) {
						String responseText = s.next();
						parsePostResponse(responseText);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}
}
