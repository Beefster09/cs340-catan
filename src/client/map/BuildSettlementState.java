package client.map;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import client.misc.ClientManager;

import shared.exceptions.InvalidActionException;
import shared.locations.VertexLocation;

public class BuildSettlementState extends MapControllerState {

	public BuildSettlementState(MapController controller) {
		super(controller);
	}

	@Override
	public boolean canPlaceSettlement(VertexLocation loc) {
		return getModel().canBuildSettlement(loc);
	}

	@Override
	public MapControllerState placeSettlement(final VertexLocation vertex)
			throws InvalidActionException {
		getView().placeSettlement(vertex, getYourColor());

		new SwingWorker<String, Object>() {

			@Override
			protected String doInBackground() throws Exception {
				int gameID = ClientManager.getModel().getGameHeader().getId();
				return ClientManager.getServer().buildSettlement(getYourself().getIndex(), gameID,
						vertex, false);
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
		return new YourTurnState(getController());
	}

}
