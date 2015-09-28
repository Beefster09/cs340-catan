package shared.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import shared.exceptions.DuplicateKeyException;
import shared.exceptions.SchemaMismatchException;
import shared.locations.HexLocation;

/**
 * Contains references to the entire layout of the map at any given moment:
 * ports, roads, settlements, cities, and the robber.
 * @author Jordan
 *
 */
public class Board {
	private Map<HexLocation, Hex> hexes;
	// This is an internal representation of the size of the board. It differs from the JSON
	// in that it does not include the center or water hexes.
	private int radius = 2;
	
	private List<Port> ports;
	private List<Road> roads;
	private List<Municipality> settlements;
	private List<Municipality> cities;
	
	private HexLocation robber;

	public Board() {
		this(true, true, true);
	}
	
	public Board(boolean hasRandomNumbers, boolean hasRandomHexes, boolean hasRandomPorts) {
		
	}
	
	public Board(List<Hex> hexList) throws DuplicateKeyException {
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
			
			robber = new HexLocation((JSONObject) json.get("robber"));
			
			ports = new ArrayList<>();
			for (Object obj : (List) json.get("ports")) {
				ports.add(new Port((JSONObject) obj));
			}
			roads = new ArrayList<>();
			for (Object obj : (List) json.get("roads")) {
				roads.add(new Road((JSONObject) obj));
			}
			settlements = new ArrayList<>();
			for (Object obj : (List) json.get("settlements")) {
				settlements.add(new Municipality((JSONObject) obj));
			}
			cities = new ArrayList<>();
			for (Object obj : (List) json.get("cities")) {
				cities.add(new Municipality((JSONObject) obj));
			}
		}
		catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for a Board:\n" + json.toJSONString());
		}
		catch (DuplicateKeyException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("Two (or more) hexes share the same location.\n"
					+ json.toJSONString());
		}
	}
	
	/**
	 * @param hexList
	 * @throws DuplicateKeyException if any of the hexes are repeated.
	 * @throws IndexOutOfBoundsException if any of the hex locations are outside the board's
	 * boundaries, based on radius.
	 */
	private void initializeHexesFromList(List<Hex> hexList) throws DuplicateKeyException {
		hexes = new HashMap<>();
		for (Hex hex : hexList) {
			if (hex.getLocation().getDistanceFromCenter() > radius) {
				throw new IndexOutOfBoundsException();
			}
			if (hexes.containsKey(hex.getLocation())) {
				throw new DuplicateKeyException();
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
	public List<Road> getRoads() {
		return roads;
	}

	/**
	 * @return the settlements
	 */
	public List<Municipality> getSettlements() {
		return settlements;
	}

	/**
	 * @return the cities
	 */
	public List<Municipality> getCities() {
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
