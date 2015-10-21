package shared.communication;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import client.data.GameInfo;
import client.data.PlayerInfo;

import shared.exceptions.SchemaMismatchException;

public class GameHeader {
	private String title;
	private int id;
	private List<PlayerHeader> players;
	
	public GameHeader(JSONObject json) throws SchemaMismatchException {
		try {
			title = (String) json.get("title");
			id = (int) (long) json.get("id");
			players = new ArrayList<>();
			for (Object obj : (List) json.get("players")) {
				players.add(new PlayerHeader((JSONObject) obj));
			}
		}
		catch (ClassCastException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for a GameHeader:\n" + json.toJSONString());
		}
	}
	
	public GameHeader(String title, int id, List<PlayerHeader> players){
		this.title = title;
		this.id = id;
		this.players = players;
	}

	public GameHeader(GameInfo info) {
		title = info.getTitle();
		id = info.getId();
		players = new ArrayList<>();
		for (PlayerInfo player : info.getPlayers()) {
			players.add(new PlayerHeader(player));
		}
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the players
	 */
	public List<PlayerHeader> getPlayers() {
		return players;
	}

	
}
