package server.ai;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import shared.communication.IServer;
import shared.model.Player;

public enum AIType {
	Justin;
	
	private String stringRepr;
	private Class<? extends AIPlayer> aiClass;
	
	static {
		Justin.stringRepr = "Justin";
		Justin.aiClass = JustinAI.class;
	}
	
	static public AIType fromString(String input) throws IllegalArgumentException{
		switch (input.toLowerCase()) {
		case "justin":
			return Justin;
		default:
			throw new IllegalArgumentException();
		}
	}

	public String toString() {
		return stringRepr;
	}
	
	public AIPlayer newInstance (UUID gameid, Player player) {
		try {
			Constructor<? extends AIPlayer> ctor = (Constructor<? extends AIPlayer>)
					aiClass.getConstructor(UUID.class, Player.class);
			return ctor.newInstance(gameid, player);
		} catch (NoSuchMethodException | SecurityException |
				InstantiationException | IllegalAccessException |
				IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
