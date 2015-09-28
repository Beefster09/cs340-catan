package shared.model;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import shared.definitions.MunicipalityType;
import shared.exceptions.SchemaMismatchException;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

/**
 * Represents a city or a settlement that has been placed on a vertex between hexes.
 * Contains reference to the vertex and the player that owns it.
 * @author Jordan
 *
 */
public class Municipality {
	private PlayerReference owner;
	private VertexLocation location;
	private MunicipalityType type;

	public Municipality(JSONObject json) throws SchemaMismatchException {
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
	public VertexLocation getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(VertexLocation location) {
		this.location = location;
	}

}