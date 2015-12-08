package server.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import server.communication.Server;
import shared.communication.IServer;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.*;

/** An AI that does only the bare minimum and does so randomly
 * Will not make trades and never does anything on its turn.
 * @author Justin Snyder
 *
 */
public class IdleAI extends AIPlayer {

	Random rand = new Random();
	IServer server = Server.getSingleton();
	
	public IdleAI(ModelFacade game, Player player) {
		super(game, player);
	}

	@Override
	public void firstRound() {
		ModelFacade game = getGame();
		VertexLocation settlement;
		
		do {
			List<HexLocation> allHexes = new ArrayList<>();
			for (HexLocation hex : HexLocation.locationsWithinRadius(2)) {
				allHexes.add(hex);
			}
			HexLocation hex = allHexes.get(rand.nextInt(allHexes.size()));
			settlement = new VertexLocation(hex, VertexDirection.values()[rand.nextInt(6)]);
		} while (!game.canBuildStartingSettlement(settlement));
		
		List<EdgeLocation> possRoads = new ArrayList<>();
		for (EdgeLocation edge : settlement.getEdges()) {
			if (edge.getDistanceFromCenter() <= 2) {
				possRoads.add(edge);
			}
		}
		EdgeLocation road = possRoads.get(rand.nextInt(possRoads.size()));
		
		try {
			server.buildStartingPieces(getPlayerID(), getGameID(), settlement, road);
		} catch (ServerException | UserException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void secondRound() {
		firstRound();
	}

	@Override
	public void discard() {
		ResourceList hand = getPlayer().getResources();
		ResourceList dummyHand = new ResourceList(hand);
		ResourceList discardChoices = new ResourceList(0);
		int toDiscard = hand.count() / 2;
		
		for (int i=0; i<toDiscard; ++i) {
			dummyHand.transferRandomCard(discardChoices);
		}
		
		try {
			server.discardCards(getPlayerID(), getGameID(), discardChoices);
		} catch (ServerException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void robber() {
		List<HexLocation> allHexes = new ArrayList<>();
		for (HexLocation hex : HexLocation.locationsWithinRadius(2)) {
			allHexes.add(hex);
		}
		HexLocation robberLoc;
		do {
			robberLoc = allHexes.get(rand.nextInt(allHexes.size()));
		} while (!getGame().canMoveRobberTo(robberLoc));
		
		try {
			server.robPlayer(getPlayerID(), getGameID(), robberLoc, null);
		} catch (ServerException | UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
