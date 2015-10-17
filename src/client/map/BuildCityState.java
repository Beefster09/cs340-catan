package client.map;

import shared.exceptions.InvalidActionException;
import shared.locations.VertexLocation;

public class BuildCityState extends MapControllerState {

	public BuildCityState(MapController controller) {
		super(controller);
	}

	@Override
	public MapControllerState placeCity(VertexLocation vertex)
			throws InvalidActionException {
		// TODO Auto-generated method stub
		return super.placeCity(vertex);
	}

	@Override
	public MapControllerState cancelMove() throws InvalidActionException {
		// TODO Auto-generated method stub
		return super.cancelMove();
	}
	
	

}
