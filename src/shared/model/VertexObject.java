package shared.model;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import shared.locations.VertexLocation;

/**
 * Represents a city or a settlement that has been placed on a vertex between hexes.
 * Contains reference to the vertex and the player that owns it.
 * @author Jordan
 *
 */
public class VertexObject {
	private PlayerReference owner;
	private VertexLocation location;

	public VertexObject(JSONObject json) {
		try {
			if (json.containsKey("roads")) {
				List<Hex> hexData = new ArrayList<>();
				for (Object obj : (List) json.get("settlement")) {
					//hexData.add(new Hex((JSONObject) obj));
					int playerOwner = (int) (long) ((JSONObject)obj).get("owner");
					location = new VertexLocation((JSONObject)obj);
				}
			}
			else throw new SchemaMismatchException("Board data is missing from the JSON:" +
					json.toJSONString());
		} catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for an EdgeObject:\n" + json.toJSONString());
		}
	}

	/**
	 * @return the owner
	 */
	public PlayerReference getOwner() {
		return owner;
	}

	/**
	 * @return the location
	 */
	public EdgeLocation getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(EdgeLocation location) {
		this.location = location;
	}

}