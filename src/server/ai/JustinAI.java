package server.ai;

import java.util.UUID;

import server.communication.Server;
import shared.communication.IServer;
import shared.model.ModelFacade;
import shared.model.Player;

public class JustinAI extends AIPlayer {
	
	IServer server = Server.getSingleton();

	public JustinAI(ModelFacade game, Player player) {
		super(game, player);
	}

	@Override
	public void firstRound() {
		// TODO
	}

	@Override
	public void secondRound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void discard() {
		// TODO Auto-generated method stub

	}

	@Override
	public void robber() {
		// TODO Auto-generated method stub

	}

}
