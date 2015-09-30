package shared.model;

import java.io.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import shared.definitions.ResourceType;
import shared.exceptions.SchemaMismatchException;
import shared.locations.*;

/**
 * Represents a port of the board; contains a location, direction, as well as what kind
 * of port it is (whether it is a 3:1 resource trading port, or a 2:1 resource trading port
 * for a specific kind of resource)
 * @author Jordan
 *
 */
public class Port {
	private ResourceType resource; // null = any resource
	private EdgeLocation location;
	private int ratio;
	
	public static void main(String args[]) throws Exception {
		JSONParser parser = new JSONParser();
		Reader r = new BufferedReader(new FileReader("port.json"));
		Object parseResult = parser.parse(r);
		Port port = new Port((JSONObject) parseResult);

		System.out.println(parseResult);
		System.out.println(port);
	}

	public Port() {
		
	}

	public Port(EdgeLocation location, ResourceType resource) {
		this.location = location;
		this.resource = resource;
		ratio = (resource == null) ? 3 : 2;
	}
	
	public Port(JSONObject json) throws SchemaMismatchException {
		try {
			if (json.containsKey("resource")) {
				resource = ResourceType.getTypeFromString((String) json.get("resource"));
			}
			else {
				resource = null;
			}
			HexLocation hexLoc = new HexLocation((JSONObject) json.get("location"));
			EdgeDirection direction = EdgeDirection.getDirectionFromString(
					(String) json.get("direction"));
			location = new EdgeLocation(hexLoc, direction);
			ratio = (int) (long) json.get("ratio");
		}
		catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for a Port:\n" + json.toJSONString());
		}
	}

	/**
	 * @return the resource
	 */
	public ResourceType getResource() {
		return resource;
	}

	/**
	 * @return the location
	 */
	public EdgeLocation getLocation() {
		return location;
	}

	/**
	 * @return the ratio
	 */
	public int getRatio() {
		return ratio;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Port [resource=" + resource + ", location=" + location
				 + ", ratio=" + ratio + "]";
	}
}
