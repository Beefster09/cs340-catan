package client.map;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.json.simple.JSONObject;

import shared.exceptions.InvalidActionException;
import shared.locations.EdgeLocation;

public class BuildRoadState extends MapControllerState {

	public BuildRoadState(MapController controller) {
		super(controller);
	}

	@Override
	public MapControllerState placeRoad(final EdgeLocation edge)
			throws InvalidActionException {
		SwingWorker<JSONObject, Object> worker = new SwingWorker<JSONObject, Object>() {

			@Override
			protected JSONObject doInBackground() throws Exception {
				MapController controller = getController();
				return controller.getServer().buildRoad(controller.getYourself(), edge, false);
			}

			@Override
			protected void done() {
				try {
					getController().getModel().updateFromJSON(get());
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		};
		worker.execute();
		
		return new YourTurnState(getController());
	}
	
	@Override
	public MapControllerState cancelMove() throws InvalidActionException {
		return new YourTurnState(getController());
	}

	@Override
	public boolean canPlaceRoad(EdgeLocation loc) {
		return getController().getModel().canBuildRoad(loc);
	}	

}
