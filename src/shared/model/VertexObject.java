package shared.model;

import shared.locations.EdgeLocation;

/**
 * Represents a city or a settlent that has been placed on a vertex between hexes.
 * Contains reference to the vertex and the player that owns it.
 * @author Jordan
 *
 */
public class VertexObject {
	private PlayerReference owner;
	private EdgeLocation location;

	public VertexObject() {
		
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