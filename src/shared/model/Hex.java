package shared.model;

import shared.definitions.ResourceType;
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
