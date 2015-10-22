package client.join;

import java.util.List;

import shared.communication.GameHeader;
import shared.communication.IServer;
import shared.communication.PlayerHeader;
import shared.definitions.CatanColor;
import shared.exceptions.GameInitializationException;
import shared.exceptions.InvalidActionException;
import shared.exceptions.JoinGameException;
import shared.exceptions.ServerException;
import shared.model.ModelFacade;
import client.base.*;
import client.communication.ServerPoller;
import client.communication.ServerProxy;
import client.data.*;
import client.misc.*;


/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController {

	private INewGameView newGameView;
	private ISelectColorView selectColorView;
	private IMessageView messageView;
	private IAction joinAction;
	private IServer serverProxy = ServerProxy.getInstance();
	private ModelFacade modelFacade = ModelFacade.getInstance();
	
	/**
	 * JoinGameController constructor
	 * 
	 * @param view Join game view
	 * @param newGameView New game view
	 * @param selectColorView Select color view
	 * @param messageView Message view (used to display error messages that occur while the user is joining a game)
	 */
	public JoinGameController(IJoinGameView view, INewGameView newGameView, 
								ISelectColorView selectColorView, IMessageView messageView) {

		super(view);

		setNewGameView(newGameView);
		setSelectColorView(selectColorView);
		setMessageView(messageView);
	}
	
	public IJoinGameView getJoinGameView() {
		
		return (IJoinGameView)super.getView();
	}
	
	/**
	 * Returns the action to be executed when the user joins a game
	 * 
	 * @return The action to be executed when the user joins a game
	 */
	public IAction getJoinAction() {
		
		return joinAction;
	}

	/**
	 * Sets the action to be executed when the user joins a game
	 * 
	 * @param value The action to be executed when the user joins a game
	 */
	public void setJoinAction(IAction value) {	
		
		joinAction = value;
	}
	
	public INewGameView getNewGameView() {
		
		return newGameView;
	}

	public void setNewGameView(INewGameView newGameView) {
		
		this.newGameView = newGameView;
	}
	
	public ISelectColorView getSelectColorView() {
		
		return selectColorView;
	}
	public void setSelectColorView(ISelectColorView selectColorView) {
		
		this.selectColorView = selectColorView;
	}
	
	public IMessageView getMessageView() {
		
		return messageView;
	}
	public void setMessageView(IMessageView messageView) {
		
		this.messageView = messageView;
	}

	/**
	 * get list of games from server, save them into your pre-game model
	 * JoinGameView().setGames(list of games, your player info)
	 * showModal
	 */
	@Override
	public void start() {
		try {
			List<GameHeader> headers = serverProxy.getGameList();
			
			GameInfo[] games = this.convertGameHeaderToGameInfo(headers);
			PlayerInfo localPlayer = new PlayerInfo();
			localPlayer.setId(modelFacade.getLocalPlayer().getPlayerID());
			localPlayer.setName(modelFacade.getLocalPlayer().getUsername());
			//localPlayer.setPlayerIndex(0);
			
			getJoinGameView().setGames(games, localPlayer);
			getJoinGameView().showModal();
		} catch (ServerException e) {
			messageView.setTitle("Server Error");
			messageView.setMessage("Unable to reach server at this point");
			messageView.showModal();
		} catch (InvalidActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private GameInfo[] convertGameHeaderToGameInfo(List<GameHeader> headers) {
		GameInfo[] games = new GameInfo[headers.size()];
		
		int i = 0;
		for (GameHeader currentHead : headers) {
			games[i] = convertHeaderToInfo(currentHead);
			i++;
		}
		return games;
	}
	private GameInfo convertHeaderToInfo(GameHeader header) {
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

	@Override
	public void startCreateNewGame() {
		
		getNewGameView().showModal();
	}

	@Override
	public void cancelCreateNewGame() {
		
		getNewGameView().closeModal();
	}

	
	/**
	 * Create a new Game board based on the options in the View(Random or not)
	 * Send create game request to server
	 * update Game List
	 * closeModal
	 */
	@Override
	public void createNewGame() {
		String title = getNewGameView().getTitle();
		boolean randomTiles = getNewGameView().getRandomlyPlaceHexes();
		boolean randomNumbers = getNewGameView().getRandomlyPlaceNumbers();
		boolean randomPorts = getNewGameView().getUseRandomPorts();
		try {
			serverProxy.createGame(title, randomTiles, randomNumbers, randomPorts);
			getNewGameView().closeModal();
			this.start();
		} catch (GameInitializationException e) {
			getMessageView().setTitle("Setup Error");
			getMessageView().setMessage("Could not initialize game.");
			getMessageView().showModal();
		} catch (InvalidActionException e) {
			getMessageView().setTitle("Invalid Action Error");
			getMessageView().setMessage("Invalid Action was performed.");
			getMessageView().showModal();
		} catch (ServerException e) {
			getMessageView().setTitle("Server Error");
			getMessageView().setMessage("Unable to connect to the server.");
			getMessageView().showModal();
		}
	}

	
	/**
	 * Iterate through player in GameInfo and 
	 * disable each color that has already been used in ColorSelectView
	 * check if you are already in
	 * if so,call JoinGame with the color you had already picked
	 */
	@Override
	public void startJoinGame(GameInfo game) {
		
//		boolean playerInGame= false;
//		CatanColor color = null;
		
		//Updates the facade with all the relevant game header information.
		modelFacade.setGameInfo(game);
		
		for (PlayerInfo player : game.getPlayers()) {
			if (player.getId() != modelFacade.getLocalPlayer().getPlayerID()) {
				getSelectColorView().setColorEnabled(player.getColor(), false);
			}
		}
		//Give the player a chance to select a color and join
		getJoinGameView().closeModal();
		getSelectColorView().showModal();
		//joinAction.execute();
	}

	@Override
	public void cancelJoinGame() {
	
		getJoinGameView().closeModal();
	}

	
	/**
	 * call join game on server
	 */
	@Override
	public void joinGame(CatanColor color) {
		try{
			serverProxy.joinGame(modelFacade.getGameHeader().getId(), color);
			if (modelFacade.getLocalPlayer() != null) {
				ServerPoller poller = new ServerPoller(serverProxy,modelFacade.getLocalPlayer());
				poller.start();
			}
			// If join succeeded
			getSelectColorView().closeModal();
//			getJoinGameView().closeModal();
			joinAction.execute();
		}
		catch(ServerException e){
			messageView.setTitle("Server Error");
			messageView.setMessage("Unable to reach server at this point");
			messageView.showModal();			
		}
		catch (JoinGameException e) {
			messageView.setTitle("Join Game Error");
			messageView.setMessage("Unable to join game at this point");
			messageView.showModal();
		}
	}

}

