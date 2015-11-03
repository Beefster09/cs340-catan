package server.cheat;

import java.io.OutputStream;

import server.interpreter.Command;
import server.interpreter.GenericInterpreter;
import server.interpreter.GenericInterpreterFactory;
import server.telnet.*;
import shared.definitions.DevCardType;

public class CatanCheatHandler extends GenericInterpreter {

	private final String cheatCode;

	private boolean cheatsEnabled = false;

	public static void main(String[] args) throws Exception {
		TelnetServer server = new TelnetServer(
				new GenericInterpreterFactory(CatanCheatHandler.class, "qwerty"));

		server.run();
	}

	public CatanCheatHandler(OutputStream ostream) {
		super(ostream);
		
		cheatCode = "secret";
	}

	public CatanCheatHandler(OutputStream ostream, String code) {
		super(ostream);
		
		cheatCode = code;
	}
	
	@Override
	protected String resultString() {
		return "==> ";
	}

	@Command(args = {"<code>"}, info = "Enables cheats if the cheat code is correct.")
	public void cheat(String code) {
		if (code.equals(cheatCode)) {
			cheatsEnabled = true;
			getWriter().println("Cheats enabled!");
		} else {
			getWriter().println("Invalid cheat code.");
		}
	}
	
	@Command(info = "Disables cheats.")
	public void uncheat() {
		cheatsEnabled = false;
		getWriter().println("Cheats disabled.");
	}

	@Command(args = {"<roll...>"}, info = "Sets the next roll(s).")
	public void setRoll(Integer... rolls) {
		if (!cheatsEnabled) {
			getWriter().println("Cheats are disabled.");
			return;
		}
		
		// TODO: in the future this will actually do something
		for (int roll : rolls) getWriter().println(roll);
		getWriter().println("Success-ish!");
	}

	@Command(args = {"<card...>"}, info = "Sets the next dev card(s).")
	public void setDevCard(DevCardType... cards) {
		if (!cheatsEnabled) {
			getWriter().println("Cheats are disabled.");
			return;
		}

		for (DevCardType card : cards) getWriter().println(card);
		getWriter().println("Success-ish!");
	}

}
