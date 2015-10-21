package client.map;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.json.simple.JSONObject;

import shared.exceptions.InvalidActionException;
import shared.locations.EdgeLocation;

public class RoadBuildingCardState extends MapControllerState {
	
	private EdgeLocation firstRoad = null;

	public RoadBuildingCardState(MapController controller) {
		super(controller);
	}

	@Override
	public MapControllerState placeRoad(final EdgeLocation edge)
			throws InvalidActionException {
		if (firstRoad == null) {
			firstRoad = edge;
			getView().placeRoad(edge, getYourColor());
			return this;
		}
		else {
			getView().placeRoad(edge, getYourColor());
			
			SwingWorker<JSONObject, Object> worker = new SwingWorker<JSONObject, Object> () {

				@Override
				protected JSONObject doInBackground() throws Exception {
					return getServer().roadBuilding(getYourself(), firstRoad, edge);
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
			
			return new YourTurnState(getController());
		}
	}

	@Override
	public MapControllerState cancelMove() throws InvalidActionException {
		if (firstRoad == null) {
			return new YourTurnState(getController());
		}
		else {
			firstRoad = null;
			// Refresh to previous state to "delete" piece
			getController().refreshPieces();
			return this;
		}
	}

	@Override
	public boolean canPlaceRoad(EdgeLocation loc) {
		if (firstRoad == null) {
			return getModel().canBuildRoad(loc);
		}
		else {
			return getModel().canBuild2Roads(firstRoad, loc);
		}
	}

}
