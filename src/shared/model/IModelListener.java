package shared.model;

public interface IModelListener {

	void mapChanged(Board newMap);
	void turnChanged(TurnTracker turnTracker);

}
