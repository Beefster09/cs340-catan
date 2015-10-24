package shared.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import shared.definitions.TurnStatus;
import shared.exceptions.SchemaMismatchException;

/*Keeps track of whose turn it is, as well what part of their turn it is.*/
/**
 * Keeps track of whose turn it is, as well as what part of their turn it is.
 * @author Jordan
 *
 */
public class TurnTracker {
	private PlayerReference currentPlayer;
	private TurnStatus status;
	private PlayerReference longestRoad;
	private PlayerReference largestArmy;

	public TurnTracker() {
		
	}
	
	public TurnTracker(List<Player> players, JSONObject json) throws SchemaMismatchException {
		try {
			currentPlayer = players.get((int) (long) json.get("currentTurn")).getReference();
			status = TurnStatus.getStatusFromString((String) json.get("status"));
			/*
			 * MIGHT NEED TO CHANGE THIS IMPLEMENTATION.  WHAT IF NOBODY HAS THESE, SHOULD
			 * THEY BE SET TO NULL OR INDEXED TO -1???
			 */
//			if (json.containsKey("longestRoad")) {
//				int playerWithLongestRoad = (int) (long) json.get("longestRoad");
//				if (playerWithLongestRoad != -1)
//					longestRoad = players.get((int) (long) json.get("longestRoad")).getReference();
//			}
//			if (json.containsKey("largestArmy")) {
//				int playerWithLargestArmy = (int) (long) json.get("largestArmy");
//				if (playerWithLargestArmy != -1)
//					largestArmy = players.get((int) (long) json.get("largestArmy")).getReference();
//			}
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
	public void setStatus(TurnStatus status) {
		this.status = status;
	}

	/**
	 * @return the currentPlayer
	 */
	public PlayerReference getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void setCurrentPlayer(PlayerReference player) {
		currentPlayer = player;
	}
	
	public PlayerReference getLongestRoad() {
		return longestRoad;
	}
	
	public PlayerReference getLargestArmy() {
		return largestArmy;
	}
	/** Passes the turn to the next player
	 * @pre The current player has finished all mandatory actions
	 * @post Control is passed onto the next player
	 */
	public void passTurn() {
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TurnTracker [currentPlayer=" + currentPlayer + ", status="
				+ status + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentPlayer == null) ? 0 : currentPlayer.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
	
}

