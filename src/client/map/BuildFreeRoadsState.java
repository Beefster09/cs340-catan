package client.map;

public class BuildFreeRoadsState extends MapControllerState {
	
	private int freeRoadsLeft = 2;

	public BuildFreeRoadsState(MapController controller) {
		super(controller);
	}

	public BuildFreeRoadsState(int freeRoads, MapController controller) {
		super(controller);
		freeRoadsLeft = freeRoads;
	}

}
