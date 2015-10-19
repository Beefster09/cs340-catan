package client.map;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.json.simple.JSONObject;

import shared.exceptions.InvalidActionException;
import shared.locations.HexLocation;
import shared.model.PlayerReference;
import client.data.RobPlayerInfo;

public class RobPlayerState extends MapControllerState {
	
	private HexLocation newRobberLoc;

	public RobPlayerState(MapController controller) {
		super(controller);
	}
	
	public RobPlayerState(MapController controller, HexLocation hex) {
		super(controller);
		
		newRobberLoc = hex;
	}

	/* (non-Javadoc)
	 * @see client.map.MapControllerState#robPlayer(client.data.RobPlayerInfo)
	 */
	@Override
	public MapControllerState robPlayer(final RobPlayerInfo victim)
			throws InvalidActionException {
		SwingWorker<JSONObject, Object> worker = new SwingWorker<JSONObject, Object>() {

			@Override
			protected JSONObject doInBackground() throws Exception {
				MapController controller = getController();
				return controller.getServer().robPlayer(controller.getYourself(), newRobberLoc,
						PlayerReference.getDummyPlayerReference(victim.getPlayerIndex()));
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
		
		return super.robPlayer(victim);
	}
	
	

}
