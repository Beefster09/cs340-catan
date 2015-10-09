package shared.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import shared.definitions.MunicipalityType;
import shared.exceptions.DuplicateKeyException;
import shared.exceptions.GameInitializationException;
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
					throws DuplicateKeyException, GameInitializationException {
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
	
	public Board(JSONObject json)
			throws SchemaMismatchException, GameInitializationException {
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
		} catch (GameInitializationException e) {
			throw new SchemaMismatchException(e.getMessage() + ":\n"
					+ json.toJSONString());
		}
	}
	
	private void initializePortsFromList(List<Port> portData)
			throws DuplicateKeyException, GameInitializationException {
		ports = new HashMap<>();
		for (Port port : portData) {
			// Make sure the port is on the edge of the board
			EdgeLocation location = port.getLocation();
			/*
			 * This function doesn't work for the moment.
			if (location.getDistanceFromCenter() != radius || location.isSpoke()) {
				throw new IndexOutOfBoundsException();
			}
			*/
			// Ensure ports are not too close together
			for (EdgeLocation neighbor : location.getNeighbors()) {
				if (ports.containsKey(neighbor)) {
					throw new GameInitializationException();
				}
			}
			// Ensure there aren't two ports at the same location
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
			throws DuplicateKeyException, GameInitializationException {
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
					throw new GameInitializationException("Distance Rule violation!");
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
	 * @throws GameInitializationException if there are not enough hexes to fill the board
	 */
	private void initializeHexesFromList(List<Hex> hexList)
			throws DuplicateKeyException, GameInitializationException {
		hexes = new HashMap<>();
		for (Hex hex : hexList) {
			HexLocation location = hex.getLocation();
			int hash = location.hashCode();
			if (location.getDistanceFromCenter() > radius) {
				throw new IndexOutOfBoundsException();
			}
			if (hexes.containsKey(location)) {
				throw new DuplicateKeyException();
			}
			hexes.put(location, hex);
		}
		
		for (HexLocation location : HexLocation.locationsWithinRadius(radius)) {
			if (!hexes.containsKey(location)) {
				throw new GameInitializationException("Some hexes are missing.");
			}
		}
	}
	
	/**
	 * @return a Collection of the all the hexes on the board (in no particular order)
	 */
	public Collection<Hex> getHexes() {
		return hexes.values();
	}

	/** Gets the Hex at the given location
	 * @return the hex at the given location
	 * @pre the location is actually on the board
	 * @post none
	 * @throws IndexOutOfBoundsException if the location is outside the board
	 */
	public Hex getHexAt(HexLocation location) {
		if (location.getDistanceFromCenter() > radius) {
			throw new IndexOutOfBoundsException();
		}
		if (hexes.containsKey(location)) {
			return hexes.get(location);
		}
		else {
			throw new IndexOutOfBoundsException();
		}
	}
	
	/** Gives a collection of all Hexes with the given number
	 * @param number the number to search for
	 * @return A Collection containing the hexes with the given number
	 */
	public Collection<Hex> getHexesByNumber(int number) {
		List<Hex> result = new ArrayList<>();
		for (Hex hex : hexes.values()) {
			if (hex.getNumber() == number) {
				result.add(hex);
			}
		}
		return result;
	}

	/** Gives the location of the desert tile
	 * 
	 * @return location of the desert tile
	 */
	public HexLocation getDesertLocation() {
		
		
		for(Hex hex : hexes.values()) {
			if(hex.getResource() == null)
				return hex.getLocation();
		}
		
		return null;
	}
	
	/**
	 * @return a Collection of all the ports on the board (in no particular order)
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
		if (location.getDistanceFromCenter() > radius) {
			throw new IndexOutOfBoundsException();
		}
		if (ports.containsKey(location)) {
			return ports.get(location);
		}
		else return null;
	}
	

	/** Returns a port (if there is one) at the given location
	 * @param location the VertexLocation to check
	 * @return the port if there is a port at the given edge
	 * @return null otherwise
	 */
	public Port getPortAt(VertexLocation location) {
		if (location.getDistanceFromCenter() > radius) {
			throw new IndexOutOfBoundsException();
		}
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
	
	/** Gets the owner of a particular port on the board
	 * @param port
	 * @return
	 */
	public PlayerReference getPortOwner(Port port) {
		for (VertexLocation location : port.getLocation().getVertices()) {
			Municipality town = getMunicipalityAt(location);
			if (town != null) return town.getOwner();
		}
		return null;
	}
	
	/** Gets the owner of a port at a specific location on the board. (Useful?)
	 * @param edge
	 * @return
	 */
	public PlayerReference getOwnerOfPortAt(EdgeLocation edge) {
		Port port = getPortAt(edge);
		if (port == null) {
			throw new IllegalArgumentException("There is no Port at that location!");
		}
		else return getPortOwner(port);
	}

	/**
	 * @return a Collection of all the roads on the board (in no particular order)
	 */
	public Collection<Road> getRoads() {
		return roads.values();
	}
	
	/**
	 * @param location
	 * @return the road at the specified location
	 * @return null if there is no road there
	 * @pre the location is on the board
	 * @throws IndexOutOfBoundsException if the road outside the boundaries of the board
	 */
	public Road getRoadAt(EdgeLocation location) {
		if (location.getDistanceFromCenter() > radius) {
			throw new IndexOutOfBoundsException();
		}
		if (roads.containsKey(location)) {
			return roads.get(location);
		}
		else return null;
	}
	
	/** Tells you if the given location is a valid place for the given player to build a road.
	 * This does NOT check resource requirements!
	 * @param player
	 * @param location
	 * @return true if the location is a legal place for the player to build a road
	 * @return false otherwise
	 * @pre none
	 * @post none
	 */
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
	 * @return a Collection of all the municipalities on the board (in no particular order)
	 */
	public Collection<Municipality> getMunicipalities() {
		return municipalities.values();
	}
	
	public Municipality getMunicipalityAt(VertexLocation location) {
		if (location.getDistanceFromCenter() > radius) {
			throw new IndexOutOfBoundsException();
		}
		if (municipalities.containsKey(location)) {
			return municipalities.get(location);
		}
		else return null;
	}
	
	/** Tells you if the given location is a valid place for the given player to build a settlement.
	 * This does NOT check resource requirements!
	 * @param player
	 * @param location
	 * @return true if the given location is a valid place for the player to build a settlement
	 */
	public boolean canBuildSettlement(PlayerReference player, VertexLocation location) {
		if (location.getDistanceFromCenter() > radius) {
			return false;
		}
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
	
	/** Tells you if the given location is a valid place for the given player to build a city.
	 * This does NOT check resource requirements!
	 * @param player
	 * @param location
	 * @return true if settlement belonging to the player is at the given location
	 * @return false otherwise.
	 */
	public boolean canBuildCity(PlayerReference player, VertexLocation location) {
		if (location.getDistanceFromCenter() > radius) return false;
		Municipality town = getMunicipalityAt(location);
		if (town == null) return false; // no settlement at that location
		return (town.getType() == MunicipalityType.SETTLEMENT
				&& player.equals(town.getOwner()));
	}
	
	/** Tells if the location is a valid location for a starting settlement/road
	 * @param settlement
	 * @param road
	 * @return
	 */
	public boolean canPlaceStartingPieces(VertexLocation settlement, EdgeLocation road) {
		// The road must be next to the settlement
		if (!road.getVertices().contains(settlement)) return false;
		// Needs to be on the board
		if (road.getDistanceFromCenter() > radius) return false;
		if (settlement.getDistanceFromCenter() > radius) return false;
		// There must not be a road at the location
		if (getRoadAt(road) != null) return false;
		
		if (getMunicipalityAt(settlement) == null) { // spot is open
			// Apply Distance Rule
			for (VertexLocation neighbor : settlement.getNeighbors()) {
				Municipality town = getMunicipalityAt(neighbor);
				if (town != null) return false;
			}
			return true;
		}
		return false;
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
		return new HashMap<>(ports);
	}
	
	public Map<VertexLocation, Municipality> getMunicipalityMap() {
		return new HashMap<>(municipalities);
	}
	
	public Map<EdgeLocation, Road> getRoadMap() {
		return new HashMap<>(roads);
	}
	
	private void initializeNumbers(boolean hasRandomNumbers) {
		
	}
	
	private void intializeHexes(boolean hasRandomHexes) {
		
	}

	public void setRoads(Map<EdgeLocation, Road> roads) {
		this.roads = roads;
	}

	public void setMunicipalities(Map<VertexLocation, Municipality> municipalities) {
		this.municipalities = municipalities;
	}



}
