package shared;

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

}
