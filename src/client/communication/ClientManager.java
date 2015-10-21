package client.communication;

import shared.communication.IServer;
import shared.model.ModelFacade;
import shared.model.PlayerReference;

public class ClientManager {
	
	private static ModelFacade model;
	private static IServer server;
	private static PlayerReference localPlayer;

	public static final int SERVER_TYPE_PROXY = 0;
	public static final int SERVER_TYPE_SERVER = 1;
	public static final int SERVER_TYPE_MOCK = 2;
	
	public static final int DEFAULT_SERVER_TYPE = SERVER_TYPE_PROXY;
	
	public static ModelFacade getModel() {
		if (model == null) {
			model = new ModelFacade();
		}
		return model;
	}
	
	public static void initializeServer(int type) {
		if (server != null) {
			throw new RuntimeException();
		}
	}
	
	public static IServer getServer() {
		if (server == null) {
			initializeServer(DEFAULT_SERVER_TYPE);
		}
		return server;
	}

	public static PlayerReference getLocalPlayer() {
		return localPlayer;
	}

	public static void setLocalPlayer(PlayerReference localPlayer) {
		ClientManager.localPlayer = localPlayer;
	}
	

}
