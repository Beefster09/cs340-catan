package shared.model;

import org.json.simple.JSONObject;

import shared.definitions.ResourceType;
import shared.exceptions.SchemaMismatchException;
import shared.locations.HexLocation;

/**
 * Represents a hex location on the map, as well as what resource it represents.
 * This class is immutable, as a hex will not be changed mid game.
 * @author Jordan
 *
 */
public class Hex {
	private HexLocation location;
	private ResourceType resource;
	private int number;

	public Hex() {
		
	}
	
	public Hex(HexLocation location, ResourceType resource, int number) {
		this.location = location;
		this.resource = resource;
		this.number = number;
	}

	public Hex(JSONObject json) throws SchemaMismatchException {
		try {
			location = new HexLocation((JSONObject) json.get("location"));
			resource = ResourceType.getTypeFromString((String) json.get("resource"));
			number = (int) (long) json.get("number");
		}
		catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for a Hex:\n" + json.toJSONString());
		}
	}

	/**
	 * @return the location
	 */
	public HexLocation getLocation() {
		return location;
	}

	/**
	 * @return the resource
	 */
	public ResourceType getResource() {
		return resource;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

}
