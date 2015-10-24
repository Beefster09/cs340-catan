package client.map;

import client.misc.ClientManager;
import shared.exceptions.InvalidActionException;
import shared.locations.HexLocation;

public class MoveRobberState extends MapControllerState {
	
	private boolean isSoldier = false;

	public MoveRobberState(MapController controller) {
		super(controller);
	}

	public MoveRobberState(MapController controller, boolean isSoldier) {
		super(controller);
		this.isSoldier = isSoldier;
	}

	@Override
	public MapControllerState placeRobber(HexLocation hex)
			throws InvalidActionException {
		getController().robDialog(hex);
		return new RobPlayerState(this, hex);
	}

	@Override
	public MapControllerState cancelMove() throws InvalidActionException {
		if (isSoldier) {
			return new YourTurnState(getController());
		}
		else {
			return super.cancelMove();
		}
	}
	
	@Override
	public boolean canMoveRobber(HexLocation loc) {
		return ClientManager.getModel().canRob(loc);
	}
	
}
