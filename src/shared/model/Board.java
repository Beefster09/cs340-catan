package shared.model;

import java.util.List;

import shared.locations.HexLocation;

/**
 * Contains references to the entire layout of the map at any given moment:
 * ports, roads, settlements, cities, and the robber.
 * @author Jordan
 *
 */
public class Board {
	private List<Hex> hexes;
	private List<Port> ports;
	private List<EdgeObject> roads;
	private List<VertexObject> settlements;
	private List<VertexObject> cities;
	private int radius;
	private HexLocation robber;

	public Board() {
		
	}
	
	public Board(boolean hasRandomNumbers, boolean hasRandomHexes, boolean hasRandomPorts) {
		
	}

	/**
	 * @return the hexes
	 */
	public List<Hex> getHexes() {
		return hexes;
	}

	/**
	 * @return the ports
	 */
	public List<Port> getPorts() {
		return ports;
	}

	/**
	 * @return the roads
	 */
	public List<EdgeObject> getRoads() {
		return roads;
	}

	/**
	 * @return the settlements
	 */
	public List<VertexObject> getSettlements() {
		return settlements;
	}

	/**
	 * @return the cities
	 */
	public List<VertexObject> getCities() {
		return cities;
	}

	/**
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * @return the robber
	 */
	public HexLocation getRobber() {
		return robber;
	}
	
	private void initializeNumbers(boolean hasRandomNumbers) {
		
	}
	
	private void intializeHexes(boolean hasRandomHexes) {
		
	}

}
