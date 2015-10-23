package client.join;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import org.json.simple.JSONObject;

import server.ai.AIType;
import shared.communication.IServer;
import shared.definitions.*;
import shared.exceptions.*;
import shared.model.ModelFacade;
import shared.model.Player;
import shared.model.TurnTracker;
import client.base.*;
import client.misc.ClientManager;
import client.communication.ServerProxy;
import client.data.PlayerInfo;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController {

	//private ModelFacade modelFacade = ModelFacade.getInstance();
	private ModelFacade modelFacade = ClientManager.getModel();
	//private IServer serverProxy = ServerProxy.getInstance();
	private IServer serverProxy = ClientManager.getServer();
	
	public PlayerWaitingController(IPlayerWaitingView view) {

		super(view);
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}
	
	public void setFacade(ModelFacade facade) {
		this.modelFacade = facade;
	}

	
	/**
	 * make a list of players from the current game
	 * getView().setPlayers(that list)
	 * getView().setAIChoices(if you happen to have AI)
	 * showModal
	 */
	@Override
	public void start() {
		assert modelFacade ==  ClientManager.getModel();
		
		List<PlayerInfo> players = null;
		if (modelFacade.getCatanModel().getGameInfo() != null)
			players = modelFacade.getCatanModel().getGameInfo().getPlayers();
		else {
			players = new ArrayList<PlayerInfo>();
		}
		if (players.size() >= 4) {
			return;
		}
		List<String> AIChoiceList;
		try {
			AIChoiceList = ClientManager.getServer().getAITypes();
			
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
		} catch (ServerException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}

	
	/**
	 * Do whatever you need to do to generate an AI and and it to the player list;
	 */
	@Override
	public void addAI() {
		
		List<PlayerInfo> players = modelFacade.getCatanModel().getGameInfo().getPlayers();

		String AITypeName = getView().getSelectedAI();
		
		final AIType aitype = AIType.getTypeFromString(AITypeName);
		
		new SwingWorker<Object, Object> () {

			@Override
			protected Object doInBackground() throws Exception {
				serverProxy.addAIPlayer(aitype);
				return null;
			}
			
		}.execute();
	
	}

	@Override
	public void playersChanged(List<Player> players) {
		if (players.size() >= 4) {
			if(getView().isModalShowing()) {
				getView().closeModal();
			}
			return;
		}
		PlayerInfo[] playerList = new PlayerInfo[players.size()];
		for(int i = 0; i < players.size(); i++)
			playerList[i] = new PlayerInfo(players.get(i));
		
		getView().closeModal();
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