package client.map;

import shared.exceptions.InvalidActionException;
import shared.locations.VertexLocation;

public class BuildSettlementState extends MapControllerState {

	public BuildSettlementState(MapController controller) {
		super(controller);
	}

	@Override
	public MapControllerState placeSettlement(VertexLocation vertex)
			throws InvalidActionException {
		// TODO Auto-generated method stub
		return super.placeSettlement(vertex);
	}

	@Override
	public MapControllerState cancelMove() throws InvalidActionException {
		// TODO Auto-generated method stub
		return super.cancelMove();
	}
	
	

}
