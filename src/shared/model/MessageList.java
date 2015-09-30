package shared.model;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import shared.exceptions.SchemaMismatchException;
import shared.locations.EdgeLocation;

/** An immutable representation of a chat message
 * @author beefster
 *
 */
public class MessageList {
	private List<String> message;
	private List<String> source;

	public MessageList() {
		
	}
	
	public MessageList(JSONObject json) throws SchemaMismatchException {
		//JSONObject chat = (JSONObject) json.get("chat");
		try {
			if (json.containsKey("lines")) {
				message = new ArrayList<String>();
				source = new ArrayList<String>();
				for (Object obj : (List) json.get("lines")) {
					message.add((String) ((JSONObject)obj).get("message"));
					source.add((String) ((JSONObject)obj).get("source"));
				}
			}
		} catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for an EdgeObject:\n" + json.toJSONString());
		}
	}

	/**
	 * @return the message
	 */
	public List<String> getMessage() {
		return message;
	}

	/**
	 * @return the source
	 */
	public List<String> getSource() {
		return source;
	}

	
}
