package server.telnet;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.*;

public class SimpleInterpreter implements Interpreter {
	
	private static Pattern tokenRegex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
	
	private PrintWriter out;
	private boolean keepOpen = true;
	
	public SimpleInterpreter(OutputStream ostream) {
		setOut(new PrintWriter(ostream, true));
	}

	@Override
	final public boolean interpret(String line) {
		String[] parts = line.split("\\s+", 2);
		
		// TODO: quote escapes
		if (parts.length == 2) {
			String command = parts[0];
			String[] args = splitWithQuoteEscapes(parts[1]);
			handle(command, args);
		}
		else if (parts.length == 1) {
			if (parts[0].length() == 0) return true;
			handle(parts[0], new String[0]);
		}
		else return true;
		
		return keepOpen;
	}
	
	private String[] splitWithQuoteEscapes(String input) {
		List<String> matchList = new ArrayList<String>();
		
		Matcher regexMatcher = tokenRegex.matcher(input);
		while (regexMatcher.find()) {
		    if (regexMatcher.group(1) != null) {
		        // Add double-quoted string without the quotes
		        matchList.add(regexMatcher.group(1));
		    } else if (regexMatcher.group(2) != null) {
		        // Add single-quoted string without the quotes
		        matchList.add(regexMatcher.group(2));
		    } else {
		        // Add unquoted word
		        matchList.add(regexMatcher.group());
		    }
		}
		
		return matchList.toArray(new String[matchList.size()]);
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
