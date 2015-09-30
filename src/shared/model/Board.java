package shared.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import shared.definitions.MunicipalityType;
import shared.exceptions.DuplicateKeyException;
import shared.exceptions.InvalidActionException;
import shared.exceptions.SchemaMismatchException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

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
	
	private Map<EdgeLocation, Port> ports;
	private Map<EdgeLocation, Road> roads;
	private Map<VertexLocation, Municipality> municipalities;
	
	private HexLocation robber;

	public Board() {
		this(true, true, true);
	}
	
	public Board(boolean hasRandomNumbers, boolean hasRandomHexes, boolean hasRandomPorts) {
		// TODO: when implementing server
	}
	
	public Board(int boardRadius, List<Hex> hexList, List<Port> ports,
			List<Road> roads, List<Municipality> towns,	HexLocation robberLocation)
					throws DuplicateKeyException, InvalidActionException {
		radius = boardRadius;
		initializeHexesFromList(hexList);
		initializePortsFromList(ports);
		initializeRoadsFromList(roads);
		initializeMunicipalitiesFromList(towns);
		if (robberLocation.getDistanceFromCenter() > boardRadius) {
			throw new IndexOutOfBoundsException();
		}
		robber = robberLocation;
	}
	
	public Board(JSONObject json) throws SchemaMismatchException {
		try {
			radius = (int) (long) json.get("radius") - 1; // Remove center from radius
			if (json.containsKey("hexes")) {
				List<Hex> hexData = new ArrayList<>();
				for (Object obj : (List) json.get("hexes")) {
					hexData.add(new Hex((JSONObject) obj));
				}
				initializeHexesFromList(hexData);
			}
			else throw new SchemaMismatchException("Board data is missing from the JSON:" +
					json.toJSONString());
			
			robber = new HexLocation((JSONObject) json.get("robber"));
			
			List<Port> portData = new ArrayList<>();
			for (Object obj : (List) json.get("ports")) {
				portData.add(new Port((JSONObject) obj));
			}
			initializePortsFromList(portData);
			
			List<Road> roadData = new ArrayList<>();
			for (Object obj : (List) json.get("roads")) {
				roadData.add(new Road((JSONObject) obj));
			}
			initializeRoadsFromList(roadData);
			
			List<Municipality> towns = new ArrayList<>();
			for (Object obj : (List) json.get("settlements")) {
				towns.add(new Municipality(null, (JSONObject) obj, MunicipalityType.SETTLEMENT));
			}
			List<Municipality> cities = new ArrayList<>();
			for (Object obj : (List) json.get("cities")) {
				towns.add(new Municipality(null, (JSONObject) obj, MunicipalityType.CITY));
			}
			initializeMunicipalitiesFromList(towns);
		}
		catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for a Board:\n" + json.toJSONString());
		}
		catch (DuplicateKeyException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("Two (or more) objects share the same location:\n"
					+ json.toJSONString());
		} catch (InvalidActionException e) {
			throw new SchemaMismatchException(e.getMessage() + ":\n"
					+ json.toJSONString());
		}
	}
	
	private void initializePortsFromList(List<Port> portData) throws DuplicateKeyException {
		ports = new HashMap<>();
		for (Port port : portData) {
			// Make sure the port is ron the edge of the board
			EdgeLocation location = port.getLocation();
			int temp = location.getDistanceFromCenter();
			if (location.getDistanceFromCenter() != radius || location.isSpoke()) {
				System.out.println("AHHHHHHHHHHHHHH\nApparently this port is in an invalid spot.");
				//throw new IndexOutOfBoundsException();
			}
			// TODO: make sure the port is not next to another port
			if (ports.containsKey(location)) {
				throw new DuplicateKeyException();
			}
			ports.put(location, port);
		}
	}

	private void initializeRoadsFromList(List<Road> roadData) throws DuplicateKeyException {
		roads = new HashMap<>();
		for (Road road : roadData) {
			// Make sure the road is on the board
			EdgeLocation location = road.getLocation();
			if (location.getDistanceFromCenter() > radius) {
				throw new IndexOutOfBoundsException();
			}
			if (roads.containsKey(location)) {
				throw new DuplicateKeyException();
			}
			roads.put(location, road);
		}
	}

	private void initializeMunicipalitiesFromList(List<Municipality> towns)
			throws DuplicateKeyException, InvalidActionException {
		municipalities = new HashMap<>();
		for (Municipality town : towns) {
			// make sure the city is on the board.
			VertexLocation location = town.getLocation();
			if (location.getDistanceFromCenter() > radius) {
				throw new IndexOutOfBoundsException();
			}
			// enforce Distance Rule
			for (VertexLocation neighbor : location.getNeighbors()) {
				if (municipalities.containsKey(neighbor)) {
					throw new InvalidActionException("Distance Rule violation!");
				}
			}
			if (municipalities.containsKey(location)) {
				throw new DuplicateKeyException();
			}
			municipalities.put(location, town);
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
			HexLocation location = hex.getLocation();
			if (location.getDistanceFromCenter() > radius) {
				throw new IndexOutOfBoundsException();
			}
			if (hexes.containsKey(location)) {
				throw new DuplicateKeyException();
			}
			hexes.put(location, hex);
		}
	}
	
	public Collection<Hex> getHexes() {
		return hexes.values();
	}

	/**
	 * @return the hex at the given location
	 */
	public Hex getHexAt(HexLocation location) {
		if (hexes.containsKey(location)) {
			return hexes.get(location);
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	public Collection<Hex> getHexesByNumber(int number) {
		List<Hex> result = new ArrayList<>();
		for (Hex hex : hexes.values()) {
			if (hex.getNumber() == number) {
				result.add(hex);
			}
		}
		return result;
	}

	/**
	 * @return the ports
	 */
	public Collection<Port> getPorts() {
		return ports.values();
	}
	
	/** Returns a port (if there is one) at the given location
	 * @param location the EdgeLocation to check
	 * @return the port if there is a port at the given edge
	 * @return null otherwise
	 */
	public Port getPortAt(EdgeLocation location) {
		if (ports.containsKey(location)) {
			return ports.get(location);
		}
		else return null;
	}
	
	public Port getPortAt(VertexLocation location) {
		Port port = null;
		for (EdgeLocation loc: location.getEdges()) {
			Port p = getPortAt(loc);
			if (p != null) {
				if (port == null) {
					port = p;
				}
				else assert false; // This should not be allowed to happen
				// ports should never be allowed to be on adjacent edges
			}
		}
		return port;
	}
	
	public PlayerReference getPortOwner(Port port) {
		for (VertexLocation location : port.getLocation().getVertices()) {
			Municipality town = getMunicipalityAt(location);
			if (town != null) return town.getOwner();
		}
		return null;
	}
	
	public PlayerReference getOwnerOfPortAt(EdgeLocation edge) {
		Port port = getPortAt(edge);
		if (port == null) {
			throw new IllegalArgumentException("There is no Port at that location!");
		}
		else return getPortOwner(port);
	}

	/**
	 * @return the roads
	 */
	public Collection<Road> getRoads() {
		return roads.values();
	}
	
	public Road getRoadAt(EdgeLocation location) {
		if (roads.containsKey(location)) {
			return roads.get(location);
		}
		else return null;
	}
	
	public boolean canBuildRoadAt(PlayerReference player, EdgeLocation location) {
		if (location.getDistanceFromCenter() > radius) return false;
		if (getRoadAt(location) == null) {
			// Adjacent road
			for (EdgeLocation neighbor : location.getNeighbors()) {
				Road road = getRoadAt(neighbor);
				if (road != null && road.getOwner() == player) {
					// check if blocked by opponent's municipality
					VertexLocation townLoc = neighbor.getVertexBetween(location);
					Municipality town = getMunicipalityAt(townLoc);
					if (town != null && !player.equals(town.getOwner())) {
						continue;
					}
					return true;
				}
			}
			// Adjacent municipality
			for (VertexLocation vertex : location.getVertices()) {
				Municipality town = getMunicipalityAt(vertex);
				if (town != null && player.equals(town.getOwner())) return true;
			}
		}
		return false;
	}

	/**
	 * @return the settlements
	 */
	public Collection<Municipality> getMunicipalities() {
		return municipalities.values();
	}
	
	public Municipality getMunicipalityAt(VertexLocation location) {
		if (municipalities.containsKey(location)) {
			return municipalities.get(location);
		}
		else return null;
	}
	
	/**
	 * @param player
	 * @param location
	 * @return
	 */
	public boolean canBuildSettlement(PlayerReference player, VertexLocation location) {
		if (getMunicipalityAt(location) == null) { // spot is open
			// Apply Distance Rule
			for (VertexLocation neighbor : location.getNeighbors()) {
				Municipality town = getMunicipalityAt(neighbor);
				if (town != null) return false;
			}
			// There must be one of your roads next to the vertex
			for (EdgeLocation edge : location.getEdges()) {
				Road road = getRoadAt(edge);
				if (road != null && road.getOwner() == player) return true;
			}
		}
		return false;
	}
	
	public boolean canBuildCity(PlayerReference player, VertexLocation location) {
		Municipality town = getMunicipalityAt(location);
		if (town == null) return false; // no settlement at that location
		return (town.getType() == MunicipalityType.SETTLEMENT
				&& player.equals(town.getOwner()));
	}

	/** This gives the radius that is needed by the HexGrid constructor.
	 * @return the radius, including the center hex and water hexes. 
	 */
	public int getDrawableRadius() {
		return radius + 2;
	}

	/**
	 * @return the location of the robber
	 */
	public HexLocation getRobberLocation() {
		return robber;
	}
	
	public boolean canMoveRobberTo(HexLocation location) {
		if (location.equals(robber)) {
			return false;
		}
		if (location.getDistanceFromCenter() > radius) { // Robber must stay on the board.
			return false;
		}
		if (getHexAt(location).getResource() == null) { // Can't move to the desert
			return false;
		}
		return true;
	}
	
	public void moveRobber(HexLocation location) throws InvalidActionException {
		if (!canMoveRobberTo(location))  {
			throw new InvalidActionException("The robber cannot be moved there.");
		}
		robber = location;
	}
	
	public Map<EdgeLocation, Port> getPortMap() {
		return ports;
	}
	
	public Map<VertexLocation, Municipality> getMunicipalityMap() {
		return municipalities;
	}
	
	public Map<EdgeLocation, Road> getRoadMap() {
		return roads;
	}
	
	private void initializeNumbers(boolean hasRandomNumbers) {
		
	}
	
	private void intializeHexes(boolean hasRandomHexes) {
		
	}



}
