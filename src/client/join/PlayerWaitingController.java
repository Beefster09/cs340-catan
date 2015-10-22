package client.join;

import java.util.ArrayList;
import java.util.List;

import server.ai.AIType;
import shared.communication.IServer;
import shared.communication.PlayerHeader;
import shared.definitions.CatanColor;
import shared.exceptions.InvalidActionException;
import shared.exceptions.ServerException;
import shared.model.ModelFacade;
import shared.model.Player;
import client.base.*;
import client.communication.ServerProxy;
import client.data.GameInfo;
import client.data.PlayerInfo;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController {

	private ModelFacade modelFacade = ModelFacade.getInstance();
	private IServer serverProxy = ServerProxy.getInstance();
	
	public PlayerWaitingController(IPlayerWaitingView view) {

		super(view);
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}

	
	/**
	 * make a list of players from the current game
	 * getView().setPlayers(that list)
	 * getView().setAIChoices(if you happen to hve AI)
	 * showModal
	 */
	@Override
	public void start() {
		
		List<PlayerInfo> players = modelFacade.getCatanModel().getGameInfo().getPlayers();
		List<String> AIChoiceList;
		try {
			AIChoiceList = ServerProxy.getInstance().getAITypes();
			
			PlayerInfo[] playerList = new PlayerInfo[players.size()];
			for(int i = 0; i < players.size(); i++)
				playerList[i] = players.get(i);
			
			String[] AIChoices = new String[AIChoiceList.size()];
			for(int i = 0; i < AIChoiceList.size(); i++)
				AIChoices[i] = AIChoiceList.get(i);
			
			getView().setPlayers(playerList);
			
			getView().setAIChoices(AIChoices);
			
			getView().showModal();
			
			if(playerList.length > 3)
				getView().closeModal();
		} catch (ServerException | InvalidActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

	
	/**
	 * Do whatever you need to do to generate an AI and and it to the player list;
	 */
	@Override
	public void addAI() {
		
		try {
			
			List<PlayerInfo> players = modelFacade.getCatanModel().getGameInfo().getPlayers();

			//CatanColor unusedColor = getUnusedColor();
			
			//String AIName = getUnusedName();
			
			//PlayerHeader AIHeader = new PlayerHeader(unusedColor, AIName, players.size()+1);
			
			//PlayerInfo AIInfo = new PlayerInfo(AIHeader);
			
			//players.add(AIInfo);
			
			
			
			String AITypeName = getView().getSelectedAI();
			
			AIType aitype = AIType.getTypeFromString(AITypeName);			
			
			serverProxy.addAIPlayer(aitype);
			
			
			PlayerInfo[] playerList = new PlayerInfo[players.size()];
			for(int i = 0; i < players.size(); i++)
				playerList[i] = players.get(i);
			
			getView().setPlayers(playerList);
				
		} catch (ServerException | InvalidActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void playersChanged(List<Player> players) {

		System.out.println("New Players:");
		for (Player player : players)
			System.out.println(player);
		
		PlayerInfo[] playerList = new PlayerInfo[players.size()];
		for(int i = 0; i < players.size(); i++)
			playerList[i] = new PlayerInfo(players.get(i));
		
		getView().setPlayers(playerList);
		getView().showModal();
		
		
		if(players.size() > 3)
			getView().closeModal();
	}
	
	private PlayerInfo[] listToPlayerArray(List<PlayerInfo> players) {
		
		PlayerInfo[] playerArray = new PlayerInfo[4];
		
		for(int i = 0; i < players.size(); i++)
			playerArray[i] = players.get(i);
		
		return playerArray;
	}

	private String getUnusedName() {
		
		List<String> possibleAINames = new ArrayList<>();
		possibleAINames.add("Jordan");
		possibleAINames.add("Grant");
		possibleAINames.add("Justin");
		possibleAINames.add("Steve");
		
		List<String> usedNames = getUsedNames();
		
		for(String possibleAIName : possibleAINames) {
			
			if(!usedNames.contains(possibleAIName))
				return possibleAIName;
		}
		
		return null;
	}

	private List<String> getUsedNames() {
		
		List<PlayerInfo> players = modelFacade.getCatanModel().getGameInfo().getPlayers();
		
		List<String> usedNames = new ArrayList<>();
		
		for(PlayerInfo p : players)
			usedNames.add(p.getName());
		
		return usedNames;
	}

	private CatanColor getUnusedColor() {
		
		List<CatanColor> usedColors = getUsedColors();
			
		for(CatanColor c : CatanColor.values())
			if(!usedColors.contains(c))
				return c;
			
		return null;
	}

	private List<CatanColor> getUsedColors() {
		
		List<PlayerInfo> players = modelFacade.getCatanModel().getGameInfo().getPlayers();
		
		List<CatanColor> usedColors = new ArrayList<>();
		
		for(PlayerInfo p : players)
			usedColors.add(p.getColor());
		
		return usedColors;
	}
	

}

