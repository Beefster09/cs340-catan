package shared.model;

import shared.definitions.ResourceType;
import shared.locations.*;

/**
 * Represents a port of the board; contains a location, direction, as well as what kind
 * of port it is (whether it is a 3:1 resource trading port, or a 2:1 resource trading port
 * for a specific kind of resource)
 * @author Jordan
 *
 */
public class Port {
	private ResourceType resource;
	private HexLocation location;
	private EdgeDirection direction;
	private int ratio;

	public Port() {
		
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
	public HexLocation getLocation() {
		return location;
	}

	/**
	 * @return the direction
	 */
	public EdgeDirection getDirection() {
		return direction;
	}

	/**
	 * @return the ratio
	 */
	public int getRatio() {
		return ratio;
	}
	
	

}
