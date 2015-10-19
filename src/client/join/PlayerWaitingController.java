package client.join;

import client.base.*;
import client.data.PlayerInfo;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController {

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
		
		PlayerInfo[] playerList = new PlayerInfo[4];
		String[] AIChoices = new String[3];
		AIChoices[0] = "Easy";
		AIChoices[1] = "Medium";
		AIChoices[2] = "Hard";
		
		PlayerInfo joe = new PlayerInfo();
		
		playerList[0] = joe;
		
		//getView().setPlayers(playerList);
		
		getView().setAIChoices(AIChoices);
		
		getView().showModal();
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

