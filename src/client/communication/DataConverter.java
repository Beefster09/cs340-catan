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
		return new GameInfo(header);
	}
	public static GameHeader convertInfoToHeader(GameInfo info) {
		 return new GameHeader(info);
	}
}
