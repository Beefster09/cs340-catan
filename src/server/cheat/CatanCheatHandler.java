package server.cheat;

import java.io.OutputStream;

import server.telnet.*;

public class CatanCheatHandler extends GenericInterpreter {

	private static final String cheatCode = "secret";

	/*static {
		registerHelp("cheat", new CommandInfo(new String[] { "<code>" },
				"Enables cheats if the cheat code is correct."));
		registerHelp("setroll", new CommandInfo("Sets the next roll(s)."));
		registerHelp("uncheat", new CommandInfo("Disables cheats."));
	}*/

	private boolean cheatsEnabled = false;

	public static void main(String[] args) throws Exception {
		TelnetServer server = new TelnetServer(
				new GenericInterpreterFactory(CatanCheatHandler.class));

		server.run();
	}

	public CatanCheatHandler(OutputStream ostream) {
		super(ostream);
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
	public void setroll(Integer... rolls) {
		if (!cheatsEnabled) {
			getWriter().println("Cheats are disabled.");
			return;
		}
		
		// TODO: in the future this will actually do something
		for (int roll : rolls) getWriter().println(roll);
		getWriter().println("Success!");
	}
	
	public void setDevCardStack(String... cards) {
		if (!cheatsEnabled) {
			getWriter().println("Cheats are disabled.");
			return;
		}
		
		getWriter().println("Not implemented!");
	}

}
