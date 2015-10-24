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
	public MapControllerState placeRoad(EdgeLocation edge)
			throws InvalidActionException {
		SwingWorker<JSONObject, Object> worker = new SwingWorker<JSONObject, Object>() {

			@Override
			protected JSONObject doInBackground() throws Exception {
				MapController controller = getController();
				return controller.getServer().soldier(controller.getYourself(), null, null);
			}

			@Override
			protected void done() {
				try {
					getController().getModel().updateFromJSON(get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
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
