package server.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import client.misc.ClientManager;

import com.google.gson.Gson;

import server.DAOs.DatabaseException;
import server.DAOs.ICommandDAO;
import server.Factories.IDAOFactory;
import server.Factories.MockDAOFactory;
import server.ai.AIManager;
import server.ai.AIPlayer;
import server.ai.AIType;
import server.commands.CatanCommand;
import server.commands.ICatanCommand;
import server.logging.LogLevel;
import server.model.User;
import server.plugins.PluginRegistry;
import shared.communication.Command;
import shared.communication.GameHeader;
import shared.communication.IServer;
import shared.communication.PlayerHeader;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.exceptions.GameInitializationException;
import shared.exceptions.GamePersistenceException;
import shared.exceptions.InvalidActionException;
import shared.exceptions.JoinGameException;
import shared.exceptions.NameAlreadyInUseException;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.CatanModel;
import shared.model.ModelFacade;
import shared.model.Player;
import shared.model.PlayerReference;
import shared.model.ResourceList;
import shared.model.ResourceTradeList;
import shared.model.TradeOffer;
import shared.model.TurnTracker;

public class Server implements IServer {
	
	private static Logger logger = Logger.getLogger("Server");
	
	private static final int NUMPLAYERS = 4;
<<<<<<< HEAD

	private static int COMMAND_FLUSH_FREQUENCY = 10;
=======
	private static final int COMMAND_FLUSH_FREQUENCY = 10;
>>>>>>> 6b59da070a7e2dfb0de1b68d41de793dc6f9938c
	
	private static final String[] AI_NAMES = new String[] {
		"R2-D2", "C-3PO", "Wall-E", "Astro Boy", "Marvin", "Data",
		"HAL", "Optimus Prime", "Baymax", "Quote", "Curly Brace",
		"Bender", "Prometheus", "Atropos", "R.O.B.", "GLaDOS"
	};
	
	private static IServer instance = null;
	public static IServer getSingleton() {
		if (instance == null) {
			synchronized (ClientManager.class) {
				if (instance == null) {
					instance = new Server();
				}
			}
		}
		return instance;
	}
	
	private static IDAOFactory factory;
	
	public static void setPersistenceType(String pluginName) {
		logger.info("Setting persistence type to " + pluginName);
		PluginRegistry registry = PluginRegistry.getSingleton();
		
		try {
			factory = registry.getDAOFactory(pluginName);
		} catch (InstantiationException e) {
			e.printStackTrace();
			factory = new MockDAOFactory();
		}
	}
	
	public static void setFlushFrequency(int n) {
		COMMAND_FLUSH_FREQUENCY = n;
	}
	
	private Map<UUID, ModelFacade> activeGames = new HashMap<>();
	private Map<UUID, GameHeader> knownGames = new HashMap<>();
	private Map<UUID, AIManager> aiGames = new HashMap<>();
	
	private Server() {
		
		loadGames();
		loadUsers();

		User sam = new User("Sam", "sam");
		User brooke = new User("Brooke", "brooke");
		User pete = new User("Pete", "pete");
		User mark = new User("Mark", "mark");
		User aaa = new User("a", "a");
		
		try {
			User.register(sam);
			User.register(brooke);
			User.register(pete);
			User.register(mark);
			User.register(aaa);
		} catch (NameAlreadyInUseException e1) {
			e1.printStackTrace();
		}
		
		try {
			factory.getUserDAO().addUser(sam);
			factory.getUserDAO().addUser(brooke	);
			factory.getUserDAO().addUser(pete);
			factory.getUserDAO().addUser(mark);
		} catch (DatabaseException e1) {
			logger.info("Users probably already exist in database, skipping register");
			
		}
		
		if (knownGames.isEmpty()) {
			logger.info("There are no known games. Creating the default game.");
			try {
				ModelFacade model = new ModelFacade();
				UUID gameUUID = model.getGameHeader().getUUID();
				model.getCatanModel().setHeader(new GameHeader("<DEFAULT>", gameUUID, null));
				model.addPlayer("Sam", CatanColor.RED);
				model.addPlayer("Brooke", CatanColor.ORANGE);
				model.addPlayer("Pete", CatanColor.YELLOW);
				model.addPlayer("Mark", CatanColor.GREEN);
				activeGames.put(gameUUID, model);
				knownGames.put(gameUUID, model.getGameHeader());
				
				model = new ModelFacade();
				gameUUID = model.getGameHeader().getUUID();
				model.getCatanModel().setHeader(new GameHeader("Join Test", gameUUID, null));
				model.addPlayer("a", CatanColor.BLUE);
				activeGames.put(gameUUID, model);
				knownGames.put(gameUUID, model.getGameHeader());
				
				try {
					factory.getGameDAO().addGame(gameUUID, model);
				} catch (DatabaseException e) {
					e.printStackTrace();
				}
				
			} catch (GameInitializationException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadGames() {
		logger.info("Getting games");
		try {
			List<GameHeader> gameList = factory.getGameDAO().getGameList();
			if (gameList == null) {
				return;
			}
			for (GameHeader game : gameList) {
				knownGames.put(game.getUUID(), game);
				logger.info(game.toString());
			}
		} catch (DatabaseException e) {
			logger.warning(e.getMessage());
			e.printStackTrace();
		}
	}

	private void loadUsers() {
		try {
			for (User user : factory.getUserDAO().getAllUsers()) {
				User.register(user);
			}
		} catch (DatabaseException | NameAlreadyInUseException e) {
			logger.warning(e.getMessage());
		}
	}
	
	private ModelFacade getGame(UUID gameid) {
		if (activeGames.containsKey(gameid)) {
			return activeGames.get(gameid);
		}
		else if (knownGames.containsKey(gameid)) {
			try {
				ModelFacade game = factory.getGameDAO().getGame(gameid);
				for (Player player : game.getCatanModel().getPlayers()) {
					Player.registerPlayer(player);
				}
				game.getCatanModel().getTurnTracker().setPlayerList(game.getCatanModel().getPlayers());
				logger.info("Game version: " + game.getVersion());
				List<ICatanCommand> commands = factory.getCommandDAO().getAll(gameid);
				for (ICatanCommand command : commands) {
					command.execute(game);
				}
				activeGames.put(gameid, game);
				return game;
			} catch (DatabaseException | InvalidActionException e) {
				e.printStackTrace();
				return null;
			}
		}
		else return null;
	}

	private void execCommand(ICatanCommand command, ModelFacade game)
		throws InvalidActionException {
		command.execute(game);
		try {
			ICommandDAO cmdDAO = factory.getCommandDAO();
			cmdDAO.addCommand(game.getUUID(), command);
			if (cmdDAO.getAll(game.getUUID()).size() >= COMMAND_FLUSH_FREQUENCY) {
				logger.fine("Flushing commands to game " + game.getUUID());
				factory.getGameDAO().updateGamebyUUID(game.getUUID(), game);
				cmdDAO.clearCommands(game.getUUID());
			}
		} catch (DatabaseException e) {
			logger.warning(e.getMessage());
		}
		game.processEvents();
	}

	@Override
	public Session login(String username, String password) throws UserException, ServerException {
		User user = User.login(username, password);
		Session newSession = new Session(user.getUsername(), user.getPassword(), null);
		return newSession;
	}

	@Override
	public Session register(String username, String password) throws UserException, ServerException {
		User user = User.register(username, password);
		try {
			factory.getUserDAO().addUser(user);
		} catch (DatabaseException e) {
			logger.warning("Failed to register the user in the persistent store");
		}
		Session newSession = new Session(user.getUsername(), user.getPassword(), null);
		return newSession;
	}

	@Override
	public List<GameHeader> getGameList() throws ServerException, UserException {
		List<GameHeader> gamesList = new ArrayList<GameHeader>();
		for(GameHeader game : knownGames.values()){
			if (activeGames.containsKey(game.getUUID())) {
				gamesList.add(getGame(game.getUUID()).getGameHeader());
			}
			else {
				gamesList.add(game);
			}
		}
		return gamesList;
	}

	@Override
	public GameHeader createGame(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts)
			throws GameInitializationException, UserException, ServerException {
		UUID gameUUID = UUID.randomUUID();
		GameHeader header = new GameHeader(name, gameUUID, null);
		ModelFacade newGame = new ModelFacade(
				new CatanModel(randomTiles, randomNumbers, randomPorts));
		newGame.getCatanModel().setHeader(header);
		newGame.getCatanModel().setVersion(0);
		try{
			factory.getGameDAO().addGame(gameUUID, newGame);
		}
		catch(Exception e){
			throw new GameInitializationException();
		}
		activeGames.put(gameUUID, newGame);
		knownGames.put(gameUUID, header);
		return header;
	}

	@Override
	public Session joinGame(Session player, UUID gameID, CatanColor color) throws JoinGameException, ServerException {
		ModelFacade game = getGame(gameID);
		
		if (game == null) {
			throw new JoinGameException();
		}
		
		List<Player> players = game.getCatanModel().getPlayers();
		for (Player currentPlayer : players) {
			if (currentPlayer.getName().equals(player.getUsername())) {
				return new Session(player.getUsername(),player.getPassword(),currentPlayer.getUUID());
			}
		}
//		Player newPlayer = new Player(0, player, color);
//		if(game.getPlayers().contains(newPlayer)){
//			return true;
//		}
		Player addedPlayer;
		try {
			addedPlayer = game.addPlayer(player, color);
		} catch (GameInitializationException e) {
			e.printStackTrace();
			return null;
		}
		if (game.getCatanModel().getPlayers().size() == NUMPLAYERS) {
			this.beginGame(game.getCatanModel());
		}
		return new Session(player.getUsername(),player.getPassword(),addedPlayer.getUUID());
	}
	
	private void beginGame(CatanModel game) throws ServerException {
		if (game.getPlayers().size() == 0) {
			//Never should occur, but in case
			throw new ServerException();
		}
		//Might want to randomize this .
		game.setTurnTracker(new TurnTracker(game.getPlayers()));
		int i = 0;
		for (Player curPlay : game.getPlayers()) {
			curPlay.setPlayerIndex(i);
			i++;
		}
		game.incrementVersion();
	}

	@Override
	public void saveGame(UUID gameID, String filename) throws GamePersistenceException, UserException, ServerException {
		// NOT NEEDED IN PHASE 3
		
	}

	@Override
	public void loadGame(String filename) throws ServerException, UserException {
		// NOT NEEDED IN PHASE 3
		
	}

	@Override
	public String getModel(UUID gameID, int version) throws ServerException, UserException {
		ModelFacade game = getGame(gameID);
		if (game == null) {
			throw new ServerException();
		}
		CatanModel model = game.getCatanModel();
		//This is currently causing the client to never update, we need to find
		//a way to fix this.
		if (version == model.getVersion() && model.hasStarted()) {
			//return null;
		}
		
		Gson gson = new Gson();
		String result = gson.toJson(model);
		logger.fine(result);
		return result;
	}

	@Override
	public String resetGame(UUID gameID) throws ServerException, UserException {
		// NOT NEEDED IN PHASE 3
		return null;
	}

	@Override
	public List<Command> getCommands(UUID gameID) throws ServerException, UserException {
		// NOT NEEDED IN PHASE 3
		return null;
	}

	@Override
	public String executeCommands(UUID gameID, List<Command> commands) throws ServerException, UserException {
		// NOT NEEDED IN PHASE 3
		return null;
	}

	@Override
	public void addAIPlayer(UUID gameID, AIType type) throws ServerException, UserException {
		ModelFacade game = getGame(gameID);

		if (game.getCatanModel().getPlayers().size() >= NUMPLAYERS) {
			throw new UserException("You may not add more players to this game");
		}
		
		if (!aiGames.containsKey(gameID)) {
			AIManager aiManager = new AIManager(game);
			aiGames.put(gameID, aiManager);
			game.registerListener(aiManager);
		}
		
		GameHeader header = game.getGameHeader();
		List<String> usedNames = new ArrayList<>();
		List<CatanColor> usedColors = new ArrayList<>();
		for (PlayerHeader ph : header.getPlayers()) {
			usedNames.add(ph.getName());
			usedColors.add(ph.getColor());
		}
		
		Random rand = new Random();
		String aiName;
		CatanColor color;
		do {
			aiName = AI_NAMES[rand.nextInt(AI_NAMES.length)];
		} while (usedNames.contains(aiName));
		
		do {
			color = CatanColor.values()[rand.nextInt(9)];
		} while (usedColors.contains(color));
		
		Player player;
		try {
			player = game.addPlayer(aiName, color);
			AIPlayer ai = type.newInstance(game, player);
			aiGames.get(gameID).addAIPlayer(ai);
			if (game.getCatanModel().getPlayers().size() == NUMPLAYERS) {
				this.beginGame(game.getCatanModel());
				aiGames.get(gameID).turnTrackerChanged(activeGames.get(gameID).getCatanModel().getTurnTracker());
			}
		} catch (GameInitializationException e) {
			throw new ServerException("Could not add an AI", e);
		}
		
	}

	@Override
	public List<String> getAITypes() throws ServerException, UserException {
		List<String> types = new ArrayList<String>();
		for (AIType aiType : AIType.values()) {
			types.add(aiType.toString());
		}
		return types;
	}

	@Override
	public String sendChat(UUID user, UUID gameID, String message) throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("sendChat",new PlayerReference(user),message);
			ModelFacade tempModel;
			tempModel = getGame(gameID);
			if (tempModel == null)
				throw new ServerException();
			
			execCommand(command, tempModel);
			
			return this.getModel(gameID, -1);
		} catch (NoSuchMethodException | SecurityException | InvalidActionException e) {
			e.printStackTrace();
		}
		return this.getModel(gameID, -1);
	}

	@Override
	public String rollDice(UUID user, UUID gameID, int num) throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("rollDice", new PlayerReference(user), num);
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

//		ModelFacade tempModel = games.get(gameID);
//		if (tempModel == null)
//			throw new ServerException();
//		try {
//			tempModel.rollDice(new PlayerReference(user), num);
//		} catch (InvalidActionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return this.getModel(gameID, -1);
	}

	@Override
	public String robPlayer(UUID user, UUID gameID, HexLocation newRobberLocation, UUID victim)
			throws ServerException, UserException {
		try {
			PlayerReference victimReference = null;
			victimReference = new PlayerReference(victim);
			ICatanCommand command = new CatanCommand("rob", new PlayerReference(user), newRobberLocation, victimReference);
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return this.getModel(gameID, -1);
	}

	@Override
	public String buyDevCard(UUID user, UUID gameID) throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("buyDevelopmentCard", new PlayerReference(user));
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getModel(gameID, -1);
	}

	@Override
	public String yearOfPlenty(UUID user, UUID gameID, ResourceType type1, ResourceType type2)
			throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("yearOfPlenty", new PlayerReference(user), type1, type2);
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getModel(gameID, -1);
	}

	@Override
	public String roadBuilding(UUID user, UUID gameID, EdgeLocation road1, EdgeLocation road2)
			throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("roadBuildingCard", new PlayerReference(user), road1, road2);
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return this.getModel(gameID, -1);
	}

	@Override
	public String soldier(UUID user, UUID gameID, HexLocation newRobberLocation, UUID victim)
			throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("soldier", new PlayerReference(user), newRobberLocation, new PlayerReference(victim));
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return this.getModel(gameID, -1);
	}

	@Override
	public String monopoly(UUID user, UUID gameID, ResourceType type) throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("monopoly", new PlayerReference(user), type);
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return this.getModel(gameID, -1);
	}

	@Override
	public String monument(UUID user, UUID gameID) throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("monument", new PlayerReference(user));
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getModel(gameID, -1);
	}

	@Override
	public String buildRoad(UUID user, UUID gameID, EdgeLocation location)
			throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("buildRoad", new PlayerReference(user), location);
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getModel(gameID, -1);
	}

	@Override
	public String buildSettlement(UUID user, UUID gameID, VertexLocation location)
			throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("buildSettlement", new PlayerReference(user), location);
			ModelFacade game = getGame(gameID);
			if (game == null)
				throw new ServerException();
			command.execute(game);
			return this.getModel(gameID, -1);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getModel(gameID, -1);
	}
	

	@Override
	public String buildStartingPieces(UUID user, UUID gameID,
			VertexLocation settlementLoc, EdgeLocation roadLoc) throws ServerException,
			UserException {
		try {
			ICatanCommand command = new CatanCommand("buildStartingPieces", new PlayerReference(user), settlementLoc, roadLoc);
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getModel(gameID, -1);
	}

	@Override
	public String buildCity(UUID user, UUID gameID, VertexLocation location)
			throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("buildCity", new PlayerReference(user), location);
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getModel(gameID, -1);
	}

	@Override
	public String offerTrade(UUID user, UUID gameID, ResourceTradeList offer, UUID receiver)
			throws ServerException, UserException {
		try {
			TradeOffer tradeOffer = new TradeOffer(new PlayerReference(user), new PlayerReference(receiver), offer);
			ICatanCommand command = new CatanCommand("offerTrade", tradeOffer);
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getModel(gameID, -1);
	}

	@Override
	public String respondToTrade(UUID user, UUID gameID, boolean accept) throws ServerException, UserException {
		try {
			ICatanCommand command = null;
			if(accept) {
				command = new CatanCommand("acceptTrade");
			}
			else{
				command = new CatanCommand("declineTrade");
			}
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getModel(gameID, -1);
	}

	@Override
	public String maritimeTrade(UUID user, UUID gameID, ResourceType inResource, ResourceType outResource,
			int ratio) throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("maritimeTrade", new PlayerReference(user), inResource, outResource);
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getModel(gameID, -1);
	}

	@Override
	public String discardCards(UUID user, UUID gameID, ResourceList cards)
			throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("discard", new PlayerReference(user), cards.getResources());
			ModelFacade tempModel;
			try {
				
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getModel(gameID, -1);
	}

	@Override
	public String finishTurn(UUID user, UUID gameID) throws ServerException, UserException {
		try {
			ICatanCommand command = new CatanCommand("finishTurn", new PlayerReference(user));
			ModelFacade tempModel;
			try {
				tempModel = getGame(gameID);
				if (tempModel == null)
					throw new ServerException();
				execCommand(command, tempModel);
				return this.getModel(gameID, -1);
			} catch (InvalidActionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return this.getModel(gameID, -1);
			
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.getModel(gameID, -1);
	}

	
	@Override
	public void changeLogLevel(LogLevel level) throws ServerException, UserException {
		// NOT NEEDED IN PHASE 3
		
	}

	@Override
	public Session getPlayerSession() {
		// TODO Auto-generated method stub
		return null;
	}
	

}