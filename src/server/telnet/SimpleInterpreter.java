package server.telnet;

import java.io.OutputStream;
import java.io.PrintWriter;

public class SimpleInterpreter implements Interpreter {
	
	private PrintWriter out;
	private boolean keepOpen = true;
	
	public SimpleInterpreter(OutputStream ostream) {
		setOut(new PrintWriter(ostream, true));
	}

	@Override
	public boolean interpret(String line) {
		// TODO: quote escapes
		
		String[] parts = line.split("\\s+", 2);
		
		if (parts.length == 2) {
			String command = parts[0];
			String[] args = parts[1].split("\\s+");
			handle(command, args);
		}
		else if (parts.length == 1) {
			if (parts[0].length() == 0) return true;
			handle(parts[0], new String[0]);
		}
		else return true;
		
		return keepOpen;
	}

	@Override
	public void onOpen() {
		getWriter().println("Hello");
	}

	@Override
	public void onClose() {
		getWriter().println("Goodbye");
	}
	
	public void prompt() {
		getWriter().print("> ");
		getWriter().flush();
	}
	
	protected void handle(String command, String[] args) {
		if (command.equals("quit")) {
			exitInterpreter();
			return;
		}
		
		getWriter().print(command + " ");
		for (String arg : args) {
			getWriter().print(arg + " ");
		}
		getWriter().println();
	}

	final protected void exitInterpreter() {
		keepOpen = false;
	}

	public PrintWriter getWriter() {
		return out;
	}

	private void setOut(PrintWriter out) {
		this.out = out;
	}

}
