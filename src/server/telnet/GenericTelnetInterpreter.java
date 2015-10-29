package server.telnet;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;

public class GenericTelnetInterpreter extends SimpleTelnetInterpreter {
	
	private static Map<String, CommandInfo> helpData = new TreeMap<>();
	
	static {
		helpData.put("quit", new CommandInfo("Closes this telnet session."));
		helpData.put("commands", new CommandInfo("Shows a list of available commands"));
		helpData.put("help", new CommandInfo(new String[]{"[command]"},
				"Gives help for all commands or a specific command.",
				"Shows a list of available commands and their descriptions. " +
				"If used on a single command, this gives a long description of " +
				"the given command."));
	}

	public GenericTelnetInterpreter(OutputStream ostream) {
		super(ostream);
	}
	
	protected static void registerHelp(String command, CommandInfo info) {
		helpData.put(command, info);
	}

	@Override
	protected boolean handle(String command, String[] args) {
		try {
			Method handler = this.getClass().getMethod(command, args.getClass());
			
			if (handler.getReturnType().equals(Boolean.TYPE)) {
				try {
					return (boolean) handler.invoke(this, (Object) args);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} catch (NoSuchMethodException | SecurityException e) {
		}
		
		getWriter().println("No such command: " + command);
		
		return true;
	}
	
	public boolean quit(String[] args) {
		return false;
	}
	
	public boolean commands(String[] args) {
		PrintWriter out = getWriter();
		
		List<String> commands = new ArrayList<>();
		for (Method method : getClass().getMethods()) {
			if (method.getReturnType().equals(Boolean.TYPE) &&
					Arrays.equals(method.getParameterTypes(), new Class<?>[]{String[].class})) {
				commands.add(method.getName());
			}
		}
		Collections.sort(commands);
		out.print("Available Commands: ");
		Iterator<String> iter = commands.iterator();
		while(iter.hasNext()) {
			out.print(iter.next());
			if (iter.hasNext()) {
				out.print(", ");
			}
		}
		
		out.println();
		
		return true;
	}
	
	public boolean help(String[] args) {
		if (args.length == 0) {
			help();
		}
		else if (args.length == 1) {
			help(args[0]);
		}
		else {
			help("help");
		}
		
		return true;
	}
	
	private void help() {
		PrintWriter out = getWriter();
		
		out.println("Available Commands:");
		
		// TODO: pretty printing
		for (Entry<String, CommandInfo> commandEntry : helpData.entrySet()) {
			String command = commandEntry.getKey();
			CommandInfo info = commandEntry.getValue();
			out.print("  " + command + " ");
			for (String arg : info.getArguments()) {
				out.print(arg + " ");
			}
			out.print("\t");
			out.println(info.getShortDescription());
		}
	}

	private void help(String command) {
		PrintWriter out = getWriter();
		
		if (!helpData.containsKey(command)) {
			out.println("Unrecognized command: " + command);
			return;
		}
		
		CommandInfo info = helpData.get(command);
		
		out.print(command + " ");
		for (String arg : info.getArguments()) {
			out.print(arg + " ");
		}
		out.println();
		
		out.println(info.getLongDescription());
	}

}
