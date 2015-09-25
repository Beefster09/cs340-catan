package shared.communication;

import java.util.List;

import server.ai.AIType;
import server.logging.LogLevel;
import shared.definitions.*;
import shared.exceptions.GameInitializationException;
import shared.exceptions.GamePersistenceException;
import shared.exceptions.InvalidActionException;
import shared.exceptions.JoinGameException;
import shared.exceptions.NotYourTurnException;
import shared.exceptions.ServerException;
import shared.exceptions.TradeException;
import shared.exceptions.UserException;
import shared.locations.*;
import shared.model.*;

public interface IServer {

	/**
	 * @pre user with username and corresponding password already exists
	 * @post player is signed in and can play.
	 * @author-Grant
	 */
	public Session login(String username, String password)
			throws UserException, ServerException;
	/**
	 * @pre username must be 3-7 characters long.
	 *   Password is at least 5 characters long.  Alphanumerics, underscores, and hyphens not allowed
	 * @post a new user is created and his username and password are stored
	 * @author-Grant
	 */
	public Session register(String username, String password)
			throws UserException, ServerException;
	
	/**
	 * @pre there must be at least one game active
	 * @post a list is generated of game headers
	 * @author-Grant
	 */
	public List<GameHeader> getGameList()
			throws ServerException;
	/**
	 * @pre user is valid.
	 * game is given a name that is not an empty string
	 * @post a game is created
	 * @author-Grant
	 */
	public GameHeader createGame(Session user, String name,
			boolean randomTiles, boolean randomNumbers, boolean randomPorts)
					throws GameInitializationException, ServerException;
	/**
	 * @pre must be a game to join with already existing gameID
	 * color cannot be taken by another player
	 * @post player joins the game
	 * @author-Grant
	 */
	public Session joinGame(Session user, int gameID, CatanColor color)
			throws JoinGameException, ServerException;
	/**
	 * @pre game filename must not be empty string
	 * @post a game is saved with a corresponding filename
	 * @author-Grant
	 */
	public void saveGame(int gameID, String filename)
			throws GamePersistenceException, ServerException;
	/**
	 * @pre a game must exist with a corresponding filename to the filename parameter
	 * @post game is loaded
	 * @author-Grant
	 */
	public void loadGame(String filename)
			throws GamePersistenceException, ServerException;
	/**
	 * @pre version must be valid
	 * @post corresponding model is returned
	 * @author-Grant
	 */
	public CatanModel getModel(Session user, int version)
			throws ServerException;
	/**
	 * @pre
	 * @post game is reset
	 * @author-Grant
	 */
	public CatanModel resetGame(Session user)
			throws ServerException, GameInitializationException;
	/**
	 * @pre
	 * @post a list of commands is returned
	 * @author-Grant
	 */
	public List<Command> getCommands(Session user)
			throws ServerException;
	/**
	 * @pre list of commands must not be empty
	 * @post commands will be executed
	 * @author-Grant
	 */
	public CatanModel executeCommands(Session user, List<Command> commands)
			throws ServerException, InvalidActionException;
	/**
	 * @pre AI type must be specified and valid
	 * @post an AIPlayer must be added
	 * @author-Grant
	 */
	public void addAIPlayer(Session user, AIType type)
			throws ServerException;
	/**
	 * @pre
	 * @post the list of AITypes are returned
	 * @author-Grant
	 */
	public List<AIType> getAITypes(Session user)
			throws ServerException;
	/**
	 * @pre message string must not be empty
	 * @post message is sent
	 * @author-Grant
	 */
	public CatanModel sendChat(Session user, String message)
			throws ServerException;
	/**
	 * @pre
	 * @post dice is rolled
	 * @author-Grant
	 */
	public CatanModel rollDice(Session user, int number)
			throws ServerException, InvalidActionException;
	/**
	 * @pre a robber cannot be placed on desert tile.  There must be players on chosen HexLocation.
	 * victim must have at least one card to steal from him.  Otherwise, no card is stolen
	 * @post robber is on placed tile and victim loses a card
	 * @author-Grant
	 */
	public CatanModel robPlayer(Session user,
			HexLocation newRobberLocation, PlayerReference victim)
					throws ServerException, InvalidActionException;
	/**
	 * @pre
	 * @post Development card is bought
	 * @author-Grant
	 */
	public CatanModel buyDevCard(Session user)
			throws ServerException, InvalidActionException;
	/**
	 * @pre two types of valid resources are specified
	 * player must have yearOfPlenty card
	 * @post player receives two specified resources
	 * @author-Grant
	 */
	public CatanModel yearOfPlenty(Session user, ResourceType type1, ResourceType type2)
			throws ServerException, InvalidActionException;
	/**
	 * @pre edge locations must be connected to exisiting road that is owned by player
	 * player must have roadbuilding card
	 * @post two roads are built at specified edge locations
	 * @author-Grant
	 */
	public CatanModel roadBuilding(Session user, EdgeLocation road1, EdgeLocation road2)
			throws ServerException, InvalidActionException;
	/**
	 * @pre a robber cannot be placed on desert tile.  There must be players on chosen HexLocation.
	 * victim must have at least one card to steal from him.  Otherwise, no card is stolen
	 * player must have soldier card
	 * @post robber is on placed tile and victim loses a card
	 * @author-Grant
	 */
	public CatanModel soldier(Session user, 
			HexLocation newRobberLocation, PlayerReference victim)
					throws ServerException, InvalidActionException;
	/**
	 * @pre one type of valid resource is specified
	 * player must have monopoly card
	 * @post player receives all cards of specified resource type from all other players
	 * @author-Grant
	 */
	public CatanModel monopoly(Session user, ResourceType type)
			throws ServerException, InvalidActionException;
	/**
	 * @pre edge location is specified and must be connected to existing road, settlement, or city owned by player
	 * @post road is built at specified location
	 * @author-Grant
	 */
	public CatanModel buildRoad(Session user, EdgeLocation location, boolean free)
			throws ServerException, InvalidActionException;
	/**
	 * @pre vertex location is specified and must be connected to road owned by player.  Must be at least two
	 * edges away from other existing settlements and cities
	 * @post settlement is built at existing location
	 * @author-Grant
	 */
	public CatanModel buildSettlement(Session user, VertexLocation location, boolean free)
			throws ServerException, InvalidActionException;
	/**
	 * @pre vertex location is specified and settlement that is owned by the player already exists at vertex location
	 * @post city replaces settlement
	 * @author-Grant
	 */
	public CatanModel buildCity(Session user, VertexLocation location)
			throws ServerException, InvalidActionException;
	/**
	 * @pre offer is specified and player must have at least as many resources in his hand as his offer does
	 * @post offer is made to another player
	 * @author-Grant
	 */
	public CatanModel offerTrade(Session user, ResourceList offer)
			throws ServerException, NotYourTurnException;
	/**
	 * @pre user chooses to accept or decline trade
	 * @post trade is declined or accepted according to user's decision
	 * @author-Grant
	 */
	public CatanModel respondToTrade(Session user, boolean accept)
			throws ServerException, TradeException;
	/**
	 * @pre user must have correct ratio for trade that he wants to do
	 * ratios are obtained when you have settlements or cities on sea ports at the edge
	 * of the board.  
	 * @post user trades in specified resources for specified resource
	 * @author-Grant
	 */
	public CatanModel maritimeTrade(Session user,
			ResourceType inResource, ResourceType outResource, int ratio)
					throws ServerException, InvalidActionException;
	/**
	 * @pre seven must be rolled and player must have more than 7 cards
	 * @post player loses half of his cards, rounding down
	 * @author-Grant
	 */
	public CatanModel discardCards(Session user, ResourceList cards)
			throws ServerException, InvalidActionException;
	/**
	 * @pre player must have rolled
	 * @post player ends turn
	 * @author-Grant
	 */
	public CatanModel finishTurn(Session user)
			throws ServerException,
			InvalidActionException;
	/**
	 * @pre
	 * @post
	 * 
	 */
	public void changeLogLevel(LogLevel level)
			throws ServerException;
}
