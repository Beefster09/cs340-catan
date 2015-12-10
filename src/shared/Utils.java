package shared;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.ResourceType;

public class Utils {

	public static boolean isBuiltinType(Class<?> type) {
		return type.getCanonicalName().startsWith("java");
	}

	/** Gives the number of "pips" that would be on a number token. (indicating frequency)
	 * @param number
	 * @return
	 */
	public static int numPips(int number) {
		if (number >= 2 && number < 7) {
			return number - 1;
		}
		else if (number > 7 && number <= 12) {
			return 13 - number;
		}
		else {
			return 0;
		}
	}
	
	public static Map<ResourceType, Integer> resourceMap(
			int wood, int brick, int sheep, int wheat, int ore) {
		Map<ResourceType, Integer> result = new HashMap<>();
		result.put(ResourceType.WOOD, wood);
		result.put(ResourceType.BRICK, brick);
		result.put(ResourceType.SHEEP, sheep);
		result.put(ResourceType.WHEAT, wheat);
		result.put(ResourceType.ORE, ore);
		return result;
	}

}
