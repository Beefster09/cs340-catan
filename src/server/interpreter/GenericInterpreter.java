package server.interpreter;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * <b>Gotcha:</b> varargs <i>cannot</i> be primitive types.
 * 
 * @author Justin Snyder
 * 
 */
public class GenericInterpreter extends SimpleInterpreter {

	private static Map<String, CommandInfo> helpData = null;
	private static Map<String, Method> dispatchTable = null;

	// Workaround for this not being able to be static
	private void initializeDispatchTable() {
		dispatchTable = new HashMap<>();
		helpData = new TreeMap<>();

		for (Method method : getClass().getMethods()) {
			if (method.isAnnotationPresent(Command.class)) {

				String commandName = camelToDash(method.getName());
				// you can't overload commands... yet.
				assert !dispatchTable.containsKey(commandName);

				dispatchTable.put(commandName, method);

				// annotation is also used to derive help data
				Command command = null;
				for (Annotation annotation : method.getAnnotations()) {
					if (annotation instanceof Command) {
						command = (Command) annotation;
						break;
					}
				}
				// command cannot be null at this point
				String description = command.description();
				if (description.equals("?")) {
					description = command.info();
				}

				helpData.put(commandName, new CommandInfo(command.args(),
						command.info(), description));
			}
		}

	}

	private static String camelToDash(String str) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < str.length(); ++i) {
			char c = str.charAt(i);
			if (Character.isUpperCase(c)) {
				result.append('-');
			}
			result.append(Character.toLowerCase(c));
		}

		return result.toString();
	}

	public GenericInterpreter(OutputStream ostream) {
		super(ostream);

		if (dispatchTable == null)
			initializeDispatchTable();
	}

	@Override
	final protected void handle(String command, String[] strArgs) {
		command = camelToDash(command);
		if (dispatchTable.containsKey(command)) {
			Method handler = dispatchTable.get(command);
			int numArgs = handler.getParameterTypes().length;

			if (!handler.isVarArgs() && strArgs.length > numArgs) {
				getWriter().println("Too many arguments.");
				helpOnCommand(command);
				return;
			}

			// Convert args to typed args
			List<Object> args = new ArrayList<>();
			int currentArg = 0;
			Class<?> varArgType = null;
			for (Class<?> type : handler.getParameterTypes()) {
				if (handler.isVarArgs() && currentArg >= numArgs - 1) {
					varArgType = type;
					break;
				}

				Object value = null;
				if (currentArg < strArgs.length) {
					try {
						value = convertString(strArgs[currentArg], type);
					} catch (Exception e) {
						getWriter().println("Invalid type for argument #" + currentArg);
						getWriter().println(e.getMessage());
						helpOnCommand(command);
						return;
					}
				}
				args.add(value);
				++currentArg;
			}

			if (varArgType != null && handler.isVarArgs()) {
				Class<?> type = varArgType.getComponentType();
				List<Object> varArgs = new ArrayList<>();
				for (; currentArg < strArgs.length; ++currentArg) {
					try {
						varArgs.add(convertString(strArgs[currentArg], type));
					} catch (Exception e) {
						getWriter().println("Invalid type for argument #" + currentArg);
						getWriter().println(e.getMessage());
						helpOnCommand(command);
						return;
					}
				}
				processVarArgs(args, varArgs, currentArg, type);
			}

			try {
				Object result = handler.invoke(this, args.toArray());
				if (result != null) {
					getWriter().println(resultString() + result.toString());
				}
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException | NullPointerException
					| ClassCastException e) {
				e.printStackTrace();
				helpOnCommand(command);
			}
		} else {
			getWriter().println("No such command: " + command);
		}
	}

	protected String resultString() {
		return "RESULT: ";
	}

	@SuppressWarnings("unchecked")
	private <T> void processVarArgs(List<Object> args, List<Object> varArgs,
			int currentArg, Class<T> type) {
		T[] arr = (T[]) Array.newInstance(type, varArgs.size());
		for (int i = 0; i < varArgs.size(); ++i) {
			arr[i] = (T) type.cast(varArgs.get(i));
		}

		// Make sure it's the correct type for varargs
		args.add((T[]) arr);
	}

	private Object convertString(String string, Class<?> type)
			throws UnsupportedTypeException {
		if (type.equals(String.class)) {
			return string;
		} else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
			return Integer.parseInt(string);
		} else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
			return Long.parseLong(string);
		} else if (type.equals(Short.TYPE) || type.equals(Short.class)) {
			return Short.parseShort(string);
		} else if (type.equals(Byte.TYPE) || type.equals(Byte.class)) {
			return Byte.parseByte(string);
		} else if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
			return Boolean.parseBoolean(string);
		} else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
			return Double.parseDouble(string);
		} else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
			return Float.parseFloat(string);
		} else {
			if (type.isEnum()) {
				try {
					Method converter = type.getMethod("fromString",
							String.class);

					if (Modifier.isStatic(converter.getModifiers())
							&& converter.getReturnType().equals(type)) {
						return converter.invoke(null, string);
					} else {
						throw new UnsupportedTypeException(String.class, type);
					}
				} catch (NoSuchMethodException | SecurityException
						| IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					throw new UnsupportedTypeException(String.class, type);
				}

			} else {
				try {
					return type.getConstructor(String.class).newInstance(string);
				} catch (NoSuchMethodException | SecurityException
						| InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					throw new UnsupportedTypeException(String.class, type);
				}
			}
		}
	}

	@Command(info = "Closes this interpreter session.")
	public void quit() {
		exitInterpreter();
	}

	@Command(info = "Echoes user input.")
	public int echo(String... input) {
		PrintWriter out = getWriter();
		for (String chunk : input) {
			out.print(chunk);
			out.print(' ');
		}
		out.println();

		return input.length;
	}

	@Command(info = "Shows a list of available commands")
	public void commands() {
		PrintWriter out = getWriter();

		List<String> commands = new ArrayList<>(dispatchTable.keySet());
		Collections.sort(commands);
		out.print("Available Commands: ");
		Iterator<String> iter = commands.iterator();
		while (iter.hasNext()) {
			out.print(iter.next());
			if (iter.hasNext()) {
				out.print(", ");
			}
		}

		out.println();
	}

	@Command(args = { "[command]" }, info = "Gives help for all commands or a specific command.", description = "Shows a list of available commands and their descriptions. If used "
			+ "on a single command, this gives a long description of the given command.")
	public void help(String command) {
		if (command == null) {
			help();
		} else {
			helpOnCommand(command);
		}
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

	private void helpOnCommand(String command) {
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
