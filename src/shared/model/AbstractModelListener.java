package shared.model;

import java.util.List;

public abstract class AbstractModelListener implements IModelListener {

	public void mapInitialized() {}
	@Override
	public void mapChanged(Board newMap) {}
	@Override
	public void playersChanged(List<Player> players) {}
	public void bankChanged(Bank otherBank) {}
	public void turnTrackerChanged(TurnTracker otherTurnTracker) {}
	public void largestArmyChanged(PlayerReference otherPlayer) {}
	public void longestRoadChanged(PlayerReference otherPlayer) {}
	public void tradeOfferChanged(TradeOffer otherOffer) {}
	public void chatChanged(MessageList otherChat) {}
	public void winnerChanged(int winner) {}
	public void logChanged(MessageList otherLog) {}
	
	
}
