package shared.communication;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import client.data.PlayerInfo;

import shared.definitions.CatanColor;
import shared.exceptions.SchemaMismatchException;

public class PlayerHeader {
	private CatanColor color;
	private String name;
	private int id;
	
	public PlayerHeader(JSONObject json) throws SchemaMismatchException {
		try {
			color = CatanColor.getColorFromString((String) json.get("color"));
			name = (String) json.get("name");
			id = (int) (long) json.get("id");
		}
		catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for a PlayerHeader:\n" + json.toJSONString());
		}
	}
	
	public PlayerHeader(CatanColor color, String name, int id){
		this.color = color;
		this.name = name;
		this.id = id;
	}
	
	public PlayerHeader(PlayerInfo player) {
		color = player.getColor();
		name = player.getName();
		id = player.getId();
	}

	/**
	 * @return the color
	 */
	public CatanColor getColor() {
		return color;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

}
