package shared.model;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import shared.definitions.TurnStatus;
import shared.exceptions.InvalidActionException;
import shared.exceptions.SchemaMismatchException;

/*Keeps track of whose turn it is, as well what part of their turn it is.*/
/**
 * Keeps track of whose turn it is, as well as what part of their turn it is.
 * @author Jordan
 *
 */
public class TurnTracker {
	private List<Player> players;
	
	private PlayerReference currentPlayer;
	private TurnStatus status;

	public TurnTracker(List<Player> players) {
		players = new ArrayList<>(players);
	}
	
	public TurnTracker(List<Player> players, JSONObject json) throws SchemaMismatchException {
		players = new ArrayList<>(players);
		try {
			currentPlayer = new PlayerReference((String) json.get("currentTurn"));
			status = TurnStatus.fromString((String) json.get("status"));
		}
		catch (ClassCastException | IllegalArgumentException | NullPointerException e) {
			e.printStackTrace();
			throw new SchemaMismatchException("The JSON does not follow the expected schema " +
					"for a TurnTracker:\n" + json.toJSONString());
		}
	}

	/**
	 * @return the status
	 */
	public TurnStatus getStatus() {
		return status;
	}
	
	/**
	 * @param status the status to set
	 */
	void setStatus(TurnStatus status) {
		this.status = status;
	}

	/**
	 * @return the currentPlayer
	 */
	public PlayerReference getCurrentPlayer() {
		return currentPlayer;
	}
	
	/** Passes the turn to the next player
	 * @throws InvalidActionException if the current player cannot pass their turn
	 * @pre The current player has finished all mandatory actions
	 * @post Control is passed onto the next player
	 */
	public void passTurn() throws InvalidActionException {
		assert currentPlayer.getPlayer().hasRolled();
		assert currentPlayer.getPlayer().hasDiscarded();
		
		int currentPlayerIndex = currentPlayer.getIndex();
		switch(status) {
		case FirstRound:
			if (currentPlayerIndex == 3) {
				status = TurnStatus.SecondRound;
			}
			else {
				currentPlayer = players.get(currentPlayerIndex + 1).getReference();
			}
			break;
		case SecondRound:
			if (currentPlayerIndex == 0) {
				status = TurnStatus.Rolling;
			}
			else {
				currentPlayer = players.get(currentPlayerIndex - 1).getReference();
			}
			break;
		case Playing:
			currentPlayer = players.get(currentPlayerIndex + 1).getReference();
			status = TurnStatus.Rolling;
			currentPlayer.getPlayer().setHasRolled(false);
			break;
		default:
			throw new InvalidActionException();
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TurnTracker [currentPlayer=" + currentPlayer + ", status="
				+ status + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentPlayer == null) ? 0 : currentPlayer.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TurnTracker other = (TurnTracker) obj;
		if (currentPlayer == null) {
			if (other.currentPlayer != null)
				return false;
		} else if (!currentPlayer.equals(other.currentPlayer))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	void roll(int roll) {
		if (roll == 7) {
			boolean discardNeeded = false;
			for (Player player : players) {
				if (player.getResources().count() >= 8) {
					discardNeeded = true;
					player.setHasDiscarded(false);
					break;
				}
			}
			if (discardNeeded) {
				setStatus(TurnStatus.Discarding);
			}
			else {
				setStatus(TurnStatus.Robbing);
			}
		}
		else {
			setStatus(TurnStatus.Playing);
		}
	}	
	
}

