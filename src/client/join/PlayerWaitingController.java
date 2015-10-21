package client.join;

import java.util.List;

import shared.communication.IServer;
import shared.exceptions.InvalidActionException;
import shared.exceptions.ServerException;
import shared.model.ModelFacade;
import client.base.*;
import client.communication.ServerProxy;
import client.data.GameInfo;
import client.data.PlayerInfo;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController {

	private ModelFacade modelFacade = ModelFacade.getInstance();
	
	
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
			
//			PlayerInfo joe = new PlayerInfo();
//			
//			playerList[0] = joe;
			
			getView().setPlayers(playerList);
			
			getView().setAIChoices(AIChoices);
			
			getView().showModal();
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

		// TEMPORARY
		getView().closeModal();
	}

}

