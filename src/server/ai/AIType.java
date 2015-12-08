package server.ai;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import shared.model.ModelFacade;
import shared.model.Player;

public enum AIType {
	Random,
	Justin;
	
	private Class<? extends AIPlayer> aiClass;
	
	static {
		Justin.aiClass = JustinAI.class;
		Random.aiClass = RandomAI.class;
	}
	
	static public AIType fromString(String input) throws IllegalArgumentException{
		String lowerInput = input.toLowerCase();
		for (AIType type : AIType.values()) {
			if (type.toString().toLowerCase().equals(lowerInput)) {
				return type;
			}
		}
		throw new IllegalArgumentException();
	}
	
	public AIPlayer newInstance (ModelFacade game, Player player) {
		try {
			Constructor<? extends AIPlayer> ctor = (Constructor<? extends AIPlayer>)
					aiClass.getConstructor(ModelFacade.class, Player.class);
			return ctor.newInstance(game, player);
		} catch (NoSuchMethodException | SecurityException |
				InstantiationException | IllegalAccessException |
				IllegalArgumentException | InvocationTargetException e) {
			System.out.println();
			e.printStackTrace();
			System.out.println("Returning null from AIType." + toString() + ".newInstance(...)");
			return null;
		}
	}
	
}
