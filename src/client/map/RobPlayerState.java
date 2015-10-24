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
	private MoveRobberState previous;

	public RobPlayerState(MapController controller) {
		super(controller);
		assert false;
	}
	
	public RobPlayerState(MoveRobberState previous, HexLocation hex) {
		super(previous.getController());
		
		newRobberLoc = hex;
		this.previous = previous;
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

	@Override
	public MapControllerState cancelMove() throws InvalidActionException {
		return previous;
	}
	
	

}
