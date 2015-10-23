package client.roll;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import org.json.simple.JSONObject;

import shared.definitions.TurnStatus;
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
	
	
	@Override
	public void turnTrackerChanged(TurnTracker turnTracker) {
		if (turnTracker.getCurrentPlayer().equals(ClientManager.getLocalPlayer())
				&& turnTracker.getStatus() == TurnStatus.Rolling) {
			System.out.println("Showing Roll View...");
			if (getRollView().isModalShowing()) getRollView().closeModal();
			
			getRollView().showModal();
		}
		else {
			getRollView().closeModal();
		}
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
		int roll1 = rand.nextInt(6) + 1;
		int roll2 = rand.nextInt(6) + 1;
		final int rollTotal = roll1 + roll2;
		getResultView().setRollValue(rollTotal);
		getResultView().showModal();
		
		SwingWorker<JSONObject, Object> worker = new SwingWorker<JSONObject, Object> () {

			@Override
			protected JSONObject doInBackground() throws Exception {
				return ClientManager.getServer().rollDice(ClientManager.getLocalPlayer(), rollTotal);
			}
			
			@Override
			protected void done() {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						try {
							ClientManager.getModel().updateFromJSON(get());
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					}
					
				});
			}
			
		};
		worker.execute();
	}

}

