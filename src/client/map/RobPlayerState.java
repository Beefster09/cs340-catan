package client.map;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.json.simple.JSONObject;

import shared.exceptions.InvalidActionException;
import shared.locations.HexLocation;
import shared.model.PlayerReference;
import client.data.RobPlayerInfo;
import client.misc.ClientManager;

public class RobPlayerState extends MapControllerState {

	private HexLocation newRobberLoc;
	private MapControllerState previous;

	public RobPlayerState(MapController controller) {
		super(controller);
		assert false;
	}

	public RobPlayerState(MapControllerState previous, HexLocation hex) {
		super(previous.getController());

		newRobberLoc = hex;
		this.previous = previous;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.map.MapControllerState#robPlayer(client.data.RobPlayerInfo)
	 */
	@Override
	public MapControllerState robPlayer(final RobPlayerInfo victimInfo)
			throws InvalidActionException {
		new SwingWorker<JSONObject, Object>() {

			@Override
			protected JSONObject doInBackground() throws Exception {
			
				PlayerReference victim =  PlayerReference.getDummyPlayerReference(-1);
				
				if (victimInfo != null) {
					victim = new PlayerReference(ClientManager
						.getModel().getCatanModel().getHeader(),
						victimInfo.getPlayerIndex());
				}

				if (previous instanceof SoldierMoveState) {
					return ClientManager.getServer().soldier(getYourself(),
							newRobberLoc, victim);
				} else if (previous instanceof MoveRobberState) {
					return ClientManager.getServer().robPlayer(getYourself(),
							newRobberLoc, victim);
				} else return null;
			}

			@Override
			protected void done() {
				try {
					ClientManager.getModel().updateFromJSON(get());
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		}.execute();

		return new YourTurnState(getController());
	}

	@Override
	public MapControllerState cancelMove() throws InvalidActionException {
		return previous;
	}

}
