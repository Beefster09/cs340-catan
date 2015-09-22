package shared.model;

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
