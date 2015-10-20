package client.map;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.json.simple.JSONObject;

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
		
		SwingWorker<JSONObject, Object> worker = new SwingWorker<JSONObject, Object> () {

			@Override
			protected JSONObject doInBackground() throws Exception {
				getServer().buildSettlement(getYourself(), settlement, true);
				return getServer().buildRoad(getYourself(), edge, true);
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
		};
		worker.execute();
		
		return super.placeRoad(edge);
	}

	@Override
	public MapControllerState cancelMove() throws InvalidActionException {
		return new BuildStartingSettlementState(getController());
	}

	@Override
	public boolean canPlaceRoad(EdgeLocation loc) {
		return getModel().canBuildStartingPieces(settlement, loc);
	}

}
