package client.roll;

import java.util.Random;

import shared.definitions.TurnStatus;
import shared.exceptions.ServerException;
import shared.exceptions.UserException;
import shared.model.TurnTracker;
import client.base.*;
import client.misc.ClientManager;

/**
 * Implementation for the roll controller
 */
public class RollController extends Controller implements IRollController {

	private IRollResultView resultView;

	/**
	 * RollController constructor
	 * 
	 * @param view Roll view
	 * @param resultView Roll result view
	 */
	public RollController(IRollView view, IRollResultView resultView) {

		super(view);
		
		setResultView(resultView);
	}
	
	public IRollResultView getResultView() {
		return resultView;
	}
	public void setResultView(IRollResultView resultView) {
		this.resultView = resultView;
	}

	public IRollView getRollView() {
		return (IRollView)getView();
	}
	
	@Override
	public void rollDice() {
		Random rand = new Random();
		int result = rand.nextInt(6) + rand.nextInt(6) + 2;
		getResultView().setRollValue(result);
		try {
			ClientManager.getServer().rollDice(ClientManager.getLocalPlayer(), result);
			getRollView().closeModal();
			getResultView().showModal();
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (UserException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void turnTrackerChanged(TurnTracker turnTracker) {
		if (turnTracker.getStatus().equals(TurnStatus.Rolling) && 
			turnTracker.getCurrentPlayer().equals(ClientManager.getLocalPlayer())) {
			getRollView().showModal();
		}
	}

}

