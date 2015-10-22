package client.listener;

public abstract class IModelListener {

	public void notifyRoadBuilt() {}
	public void notifySettlementBuilt() {}
	public void notifyCityBuilt() {}
	public void notifyRobberMoved() {}
	public void notifyturnChanged() {}
	public void notifyPlayersChanged() {}
	public void notifyBankChanged() {}
}
