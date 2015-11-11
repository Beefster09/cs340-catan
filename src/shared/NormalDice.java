package shared;

import java.util.Random;

public class NormalDice implements IDice {
	
	private Random rand = new Random();

	@Override
	public int roll() {
		return rand.nextInt(6) + rand.nextInt(6) + 2;
	}

}
