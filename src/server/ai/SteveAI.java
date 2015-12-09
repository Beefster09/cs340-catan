package server.ai;

import java.util.Random;

import client.misc.ClientManager;
import server.communication.Server;
import shared.communication.IServer;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.ModelFacade;
import shared.model.Player;
import shared.model.TradeOffer;

public class SteveAI extends AIPlayer{

	private Random random;
	private IServer server = Server.getSingleton();
	
	public SteveAI(ModelFacade game, Player player) {
		super(game, player);
		random = new Random();
	}

	@Override
	public void firstRound() {

	}

	@Override
	public void secondRound() {
		
	}

	@Override
	public void takeTurn(){
		System.out.println("Take Turn");
		
	}
	
	@Override
	public boolean tradeOffered(TradeOffer offer){
		System.out.println("Trade Offered");
		return false;
	}
	
	@Override
	public void discard() {
		// TODO Auto-generated method stub
		System.out.println("Discard");
		
	}

	@Override
	public void robber() {
		// TODO Auto-generated method stub
		System.out.println("Robber");
		
	}

}
