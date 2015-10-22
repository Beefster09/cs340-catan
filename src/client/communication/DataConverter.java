package client.communication;

import java.util.List;

import shared.communication.GameHeader;
import shared.communication.PlayerHeader;
import client.data.GameInfo;
import client.data.PlayerInfo;

public class DataConverter {
	public static GameInfo[] convertGameHeaderToGameInfo(List<GameHeader> headers) {
		GameInfo[] games = new GameInfo[headers.size()];
		
		int i = 0;
		for (GameHeader currentHead : headers) {
			games[i] = convertHeaderToInfo(currentHead);
			i++;
		}
		return games;
	}
	public static GameInfo convertHeaderToInfo(GameHeader header) {
		int id = header.getId();
		String title = header.getTitle();
		
		GameInfo newGame = new GameInfo();
		newGame.setId(id);
		newGame.setTitle(title);
		//games[i] = newGame;
		
		List<PlayerHeader> players = header.getPlayers();
		
		int index = 0;
		for (PlayerHeader player : players) {
			if (player == null)
				continue;
			PlayerInfo newPlayer = new PlayerInfo();
			newPlayer.setColor(player.getColor());
			newPlayer.setId(player.getId());
			newPlayer.setName(player.getName());
			newPlayer.setPlayerIndex(index);
			newGame.addPlayer(newPlayer);
			//games[i].addPlayer(newPlayer);
			
			index++;
		}
		return newGame;
	}
	public static GameHeader convertInfoToHeader(GameInfo info) {
		 return new GameHeader(info);
	}
}
