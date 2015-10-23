package client.turntracker;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.json.simple.JSONObject;

import shared.definitions.CatanColor;

import client.base.*;
import client.misc.ClientManager;


/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController {

	public TurnTrackerController(ITurnTrackerView view) {
		
		super(view);
		
		initFromModel();
	}
	
	@Override
	public ITurnTrackerView getView() {
		
		return (ITurnTrackerView)super.getView();
	}

	@Override
	public void endTurn() {
		SwingWorker<JSONObject, Object> worker = new SwingWorker<JSONObject, Object> () {

			@Override
			protected JSONObject doInBackground() throws Exception {
				return ClientManager.getServer().finishTurn(ClientManager.getLocalPlayer());
			}

			@Override
			protected void done() {
				try {
					ClientManager.getModel().updateFromJSON(get());
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		worker.execute();
	}
	
	private void initFromModel() {
		if (ClientManager.getLocalPlayer() != null && ClientManager.getLocalPlayer().getPlayer() != null) {
			getView().setLocalPlayerColor(ClientManager.getLocalPlayer().getPlayer().getColor());
		}
		else {
			getView().setLocalPlayerColor(CatanColor.WHITE);
		}
	}

}

