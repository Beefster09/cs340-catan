package shared.model;

import java.util.List;

public interface IModelListener {

	void mapChanged(Board newMap);
	void turnChanged(TurnTracker turnTracker);
	void playersChanged(List<Player> players);
	void bankChanged(Bank otherBank);
	void turnTrackerChanged(TurnTracker otherTurnTracker);
	void largestArmyChanged(PlayerReference otherPlayer);
	void longestRoadChanged(PlayerReference otherPlayer);
	void tradeOfferChanged(TradeOffer otherOffer);
	void chatChanged(MessageList otherChat);
	void winnerChanged(PlayerReference otherPlayer);

}
