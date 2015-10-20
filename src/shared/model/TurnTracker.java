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
	
	public static void main(String[] args) throws Exception {
		List<Player> players = new ArrayList<>();
		players.add(new Player(null, 0));
		players.add(new Player(null, 1));
		players.add(new Player(null, 2));
		players.add(new Player(null, 3));
		
		JSONParser parser = new JSONParser();
		Reader r = new BufferedReader(new FileReader("turn.json"));
		Object parseResult = parser.parse(r);
		TurnTracker trade = new TurnTracker(players, (JSONObject) parseResult);

		System.out.println(parseResult);
		System.out.println(trade);
	}

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
	
}

