package shared;

import server.ChangeRequestJsonHelper;

import com.google.gson.Gson;
/**
 * This class provides shared utilities for classes in both the server and the client
 * @author dmendels
 *
 */
public class Utils {
	
	/**
	 * Converts a string into a ChangeRequest object
	 * @param s - the json string to be converted into a ChangeRequest object
	 * @return
	 */
    public static ChangeRequest convertStringToChangeRequest(String s) {
    	Gson gson = new Gson();
    	ChangeRequestJsonHelper helper = gson.fromJson(s, ChangeRequestJsonHelper.class);
    	return convertHelperToChangeRequest(helper);
    }
    
    /**
     * Converts a ChangeRequestJsonHelper object into a ChangeRequest object
     * @param helper - a ChangeRequestJsonHelper object
     * @return a ChangeRequest object that is the same as the input object, except the
     * string in the helper object that represents a Change object has been expanded into 
     * the appropriate subclass of change (e.g. Insert, NewDoc, StyleChange, etc.)
     */
    public static ChangeRequest convertHelperToChangeRequest(ChangeRequestJsonHelper helper) {
    	Gson gson = new Gson();
    	ChangeRequest.OperationType opType = helper.getOperationType();
    	ChangeRequest result;
    	switch (opType) {
    	case NEWDOC:
    		NewDoc newDocChange = gson.fromJson(helper.getJsonChangeString(), NewDoc.class);
    		result = new ChangeRequest(opType, helper.getDocID(), helper.getClientID(), helper.getVersionID(), newDocChange);
    		break;
    	case INSERT:
    		Insert insertChange = gson.fromJson(helper.getJsonChangeString(), Insert.class);
    		result = new ChangeRequest(opType, helper.getDocID(), helper.getClientID(), helper.getVersionID(), insertChange);
    		break;
    	case DELETE:
    		Delete deleteChange = gson.fromJson(helper.getJsonChangeString(), Delete.class);
    		result = new ChangeRequest(opType, helper.getDocID(), helper.getClientID(), helper.getVersionID(), deleteChange);
    		break;
    	case STYLECHANGE:
    		StyleChange styleChange = gson.fromJson(helper.getJsonChangeString(), StyleChange.class);
    		result = new ChangeRequest(opType, helper.getDocID(), helper.getClientID(), helper.getVersionID(), styleChange);
    		break;
    	default:
    		result = null;
    		break;
    	}
    	return result;
    }
    
    /**
     * Converts a ChangeRequest object into a ChangeRequestJsonHelper object
     * @param request - a ChangeRequest object
     * @return a ChangeRequestJsonHelper object that is the same as the input object,
     * except the change field of the ChangeRequest (which is of type Change) is compressed into a
     * String using json.
     */
    public static ChangeRequestJsonHelper convertChangeRequestToHelper(ChangeRequest request) {
    	Gson gson = new Gson();
    	String changeString = null;
    	if (request.getChange() != null)
    		changeString = gson.toJson(request.getChange());
    	return new ChangeRequestJsonHelper(request.getOperationType(), request.getDocID(), request.getVersionID(), request.getClientID(), changeString);
    }
}
