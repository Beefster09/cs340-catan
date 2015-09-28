package shared.model;

import java.util.List;

import org.json.simple.JSONObject;

import shared.locations.EdgeLocation;

/** An immutable representation of a chat message
 * @author beefster
 *
 */
public class MessageLine {
	private String message;
	private String source;

	public MessageLine() {
		
	}
	
	public MessageLine(JSONObject json) {
		JSONObject chat = (JSONObject) json.get("chat");
		try {
			if (chat.containsKey("lines")) {
				for (Object obj : (List) json.get("lines")) {
					message = (String) ((JSONObject)obj).get("message");
					source = (String) ((JSONObject)obj).get("source");
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
	public String getMessage() {
		return message;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	
}
