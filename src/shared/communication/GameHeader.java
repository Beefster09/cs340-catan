package shared.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONObject;

import client.data.GameInfo;
import client.data.PlayerInfo;
import shared.exceptions.SchemaMismatchException;

public class GameHeader {
	private String title;
	private UUID uuid;
	private List<PlayerHeader> players;
	
	@SuppressWarnings("unchecked")
	public GameHeader(JSONObject json) throws SchemaMismatchException {
		try {
			title = (String) json.get("title");
			uuid = UUID.fromString( (String) json.get("id"));
			players = new ArrayList<>();
			for (JSONObject obj : (List<JSONObject>) json.get("players")) {
				if(obj.isEmpty()){
					players.add(null);
					continue;
				}
				players.add(new PlayerHeader(obj));
			}
		}
		catch (ClassCastException | IllegalArgumentException e) {
			//e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for a GameHeader:\n" + json.toJSONString());
		}
	}
	
	public GameHeader(String title, UUID id, List<PlayerHeader> players){
		this.title = title;
		this.uuid = id;
		this.players = players;
	}

	public GameHeader(GameInfo info) {
		title = info.getTitle();
		uuid = info.getUUID();
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
		return uuid.hashCode();
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	/**
	 * @return the players
	 */
	public List<PlayerHeader> getPlayers() {
		return players;
	}

	
}
