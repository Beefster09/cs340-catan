package shared.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import shared.exceptions.SchemaMismatchException;
import shared.locations.HexLocation;

public class Board {
	private Map<HexLocation, Hex> hexes;
	// This is an internal representation of the size of the board. It differs from the JSON
	// in that it does not include the center or water hexes.
	private int radius = 2;
	
	private List<Port> ports;
	private List<EdgeObject> roads;
	private List<VertexObject> settlements;
	private List<VertexObject> cities;
	
	private HexLocation robber;

	public Board() {
		
	}
	
	public Board(boolean hasRandomNumbers, boolean hasRandomHexes, boolean hasRandomPorts) {
		
	}
	
	public Board(List<Hex> hexList) {
		initializeHexesFromList(hexList);
	}
	
	public Board(JSONObject json) throws SchemaMismatchException {
		try {
			radius = (int) (long) json.get("radius") - 2;
			if (json.containsKey("map")) {
				List<Hex> hexData = new ArrayList<>();
				for (Object obj : (List) json.get("map")) {
					hexData.add(new Hex((JSONObject) obj));
				}
				initializeHexesFromList(hexData);
			}
			else throw new SchemaMismatchException("Board data is missing from the JSON:" +
					json.toJSONString());
		}
		catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for a Board:\n" + json.toJSONString());
		}
	}
	
	/**
	 * @param hexList
	 * @throws IndexOutOfBoundsException if any of the hex locations are outside the board's
	 * boundaries, based on radius.
	 */
	private void initializeHexesFromList(List<Hex> hexList) {
		hexes = new HashMap<>();
		for (Hex hex : hexList) {
			if (hex.getLocation().getDistanceFromCenter() > radius) {
				throw new IndexOutOfBoundsException();
			}
			hexes.put(hex.getLocation(), hex);
		}
	}

	/**
	 * @return the hexes
	 */
	public Hex getHexAt(HexLocation location) {
		if (hexes.containsKey(location)) {
			return hexes.get(location);
		}
		else {
			throw new IndexOutOfBoundsException();
		}
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

	/** This gives the radius that is needed by the HexGrid constructor.
	 * @return the radius, including the center hex and water hexes. 
	 */
	public int getDrawableRadius() {
		return radius + 2;
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
