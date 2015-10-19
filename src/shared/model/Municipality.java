package shared.model;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import shared.definitions.MunicipalityType;
import shared.exceptions.InvalidActionException;
import shared.exceptions.SchemaMismatchException;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

/**
 * Represents a city or a settlement that has been placed on a vertex between hexes.
 * Contains reference to the vertex and the player that owns it.
 * @author Jordan
 *
 */
public class Municipality {
	private PlayerReference owner;
	private VertexLocation location;
	private MunicipalityType type;

	public Municipality(List<Player> players, JSONObject json, MunicipalityType type) throws SchemaMismatchException {
		try {
			/*
			if (json.containsKey("roads")) {
				List<Hex> hexData = new ArrayList<>();
				for (Object obj : (List) json.get("settlement")) {
					//hexData.add(new Hex((JSONObject) obj));
					int playerOwner = (int) (long) ((JSONObject)obj).get("owner");
					location = new VertexLocation((JSONObject)obj);
				}
			}
			else throw new SchemaMismatchException("Board data is missing from the JSON:" +
					json.toJSONString());
			*/
			int playerOwner = (int) (long) (json.get("owner"));
			
			location = new VertexLocation((JSONObject)(json.get("location")));
			this.type = type;
		} catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for an EdgeObject:\n" + json.toJSONString());
		}
	}

	/**
	 * @param owner
	 * @param location
	 * @param type
	 */
	Municipality(VertexLocation location, MunicipalityType type, PlayerReference owner) {
		super();
		this.owner = owner;
		this.location = location;
		this.type = type;
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
	public VertexLocation getLocation() {
		return location;
	}
	
	public MunicipalityType getType() {
		return type;
	}
	
	public int getPointValue() {
		return type.getPointValue();
	}
	
	/** Upgrades a settlement to a city
	 * @pre This municipality is a settlement
	 * @post This municipality will be a city
	 * @throws InvalidActionException if this is already a city
	 */
	public void upgrade() throws InvalidActionException {
		if (type != MunicipalityType.SETTLEMENT) {
			throw new InvalidActionException("Attempt to upgrade an already upgraded city.");
		}
		type = MunicipalityType.CITY;
	}

}