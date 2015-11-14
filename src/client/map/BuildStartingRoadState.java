package client.map;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.json.simple.JSONObject;

import client.misc.ClientManager;
import shared.definitions.PieceType;
import shared.exceptions.InvalidActionException;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

public class BuildStartingRoadState extends MapControllerState {
	
	VertexLocation settlement;

	public BuildStartingRoadState(MapController controller) {
		super(controller);
	}
	
	public BuildStartingRoadState(MapController controller, VertexLocation vertex) {
		super(controller);
		settlement = vertex;
	}

	@Override
	public MapControllerState placeRoad(final EdgeLocation edge)
			throws InvalidActionException {
		
		getView().placeRoad(edge, getYourColor());
		
		new SwingWorker<String, Object> () {

			@Override
			protected String doInBackground() throws Exception {
				int gameID = ClientManager.getModel().getGameHeader().getId();
				getServer().buildSettlement(getYourself().getIndex(), gameID, settlement, true);
				getServer().buildRoad(getYourself().getIndex(), gameID, edge, true);
				return getServer().finishTurn(getYourself().getIndex(), gameID);
			}

			@Override
			protected void done() {
				try {
					getModel().updateFromJSON(get());
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.execute();
		
		return new NullState(getController());
	}

	@Override
	public MapControllerState cancelMove() throws InvalidActionException {
		getController().refreshPieces();
		getView().startDrop(PieceType.SETTLEMENT, getYourColor(), false);
		return new BuildStartingSettlementState(getController());
	}

	@Override
	public boolean canPlaceRoad(EdgeLocation loc) {
		try {
			return getModel().canBuildStartingPieces(settlement, loc);
		} catch (Exception e) {
			return false;
		}
	}

}
