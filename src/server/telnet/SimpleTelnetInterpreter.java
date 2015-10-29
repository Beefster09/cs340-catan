package server.telnet;

import java.io.OutputStream;
import java.io.PrintWriter;

public class SimpleTelnetInterpreter implements ITelnetInterpreter {
	
	private PrintWriter out;
	
	public SimpleTelnetInterpreter(OutputStream ostream) {
		setOut(new PrintWriter(ostream, true));
	}

	@Override
	public boolean interpret(String line) {
		String[] parts = line.split("\\s+", 2);
		
		if (parts.length == 2) {
			String command = parts[0];
			String[] args = parts[1].split("\\s+");
			return handle(command, args);
		}
		else if (parts.length == 1) {
			return handle(parts[0], new String[0]);
		}
		else return true;
	}

	@Override
	public void onOpen() {
		getWriter().println("Hello");
	}

	@Override
	public void onClose() {
		getWriter().println("Goodbye");
	}
	
	protected boolean handle(String command, String[] args) {
		if (command.equals("quit")) return false;
		
		getWriter().print(command + " ");
		for (String arg : args) {
			getWriter().print(arg + " ");
		}
		getWriter().println();
		
		return true;
	}

	public PrintWriter getWriter() {
		return out;
	}

	private void setOut(PrintWriter out) {
		this.out = out;
	}

}
