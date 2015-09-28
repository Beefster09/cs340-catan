package shared.model;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import shared.exceptions.SchemaMismatchException;
import shared.locations.EdgeLocation;

/**
 * Represents an object (i.e. road) that exists on the edge of a hex.
 * @author Jordan
 *
 */
public class EdgeObject {
	private PlayerReference owner;
	private EdgeLocation location;

	public EdgeObject() {
		
	}
	
	public EdgeObject(JSONObject json) throws SchemaMismatchException {
		try {
			if (json.containsKey("roads")) {
				List<Hex> hexData = new ArrayList<>();
				for (Object obj : (List) json.get("roads")) {
					//hexData.add(new Hex((JSONObject) obj));
					int playerOwner = (int) (long) ((JSONObject)obj).get("owner");
					location = new EdgeLocation((JSONObject)obj);
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

	/** Gets the owner of the edge (road)
	 * @return the owner
	 */
	public PlayerReference getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(PlayerReference owner) {
		this.owner = owner;
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
