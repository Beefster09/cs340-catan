package shared.model;

import java.util.List;

public abstract class AbstractModelListener implements IModelListener {

	@Override
	public void mapChanged(Board newMap) {
	}

	@Override
	public void turnChanged(TurnTracker turnTracker) {
	}

	@Override
	public void playersChanged(List<Player> players) {
	}
	
}
