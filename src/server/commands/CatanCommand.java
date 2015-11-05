package server.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import shared.Utils;
import shared.exceptions.InvalidActionException;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.model.ModelFacade;

/**
 * A generic implementation of commands that uses reflection to do its business.
 * <div>This makes class makes certain assumptions:
 * <ul>
 * <li>all arguments are serializable to JSONObjects with one call to toJSONObject
 * if they are not builtin types.</li>
 * <li>all non-builtin types of the arguments must either have a static fromJSONObject
 * method or a constructor that takes a single JSONObject (the model classes all qualify
 * for this assumption)</li>
 * </div>
 * @author Justin Snyder
 * 
 */
public class CatanCommand implements ICatanCommand {

	public static void main(String[] args) throws Exception {
		ModelFacade model = new ModelFacade();
		CatanCommand command_old = new CatanCommand("doBuildRoad",
				new EdgeLocation(0, 0, EdgeDirection.North), "asdffdsa!");

		JSONObject json = command_old.toJSONObject();

		String jsonString = json.toJSONString();

		System.out.println(jsonString);

		JSONObject json_new = (JSONObject) (new JSONParser().parse(jsonString));

		CatanCommand command = new CatanCommand(json_new);

		command.execute(model);
	}

	private Method method;
	private Object[] arguments;

	/**
	 * Creates a generic command from the ModelFacade class by inferring the correct
	 * method from argument types
	 * 
	 * @param clazz
	 * @param method
	 * @param args
	 * @throws SecurityException if the method is inaccessible
	 * @throws NoSuchMethodException if the method doesn't exist
	 */
	public CatanCommand(String method, Object... args)
			throws NoSuchMethodException, SecurityException {
		setDispatch(method, args);
	}

	private void setDispatch(String method, Object... args)
			throws NoSuchMethodException, SecurityException {
		List<Class<?>> argTypes = new ArrayList<>();
		for (Object arg : args) {
			argTypes.add(arg.getClass());
		}
		this.method = ModelFacade.class.getMethod(method,
				argTypes.toArray(new Class<?>[argTypes.size()]));
		arguments = args;
	}

	/** Creates a CatanCommand from a JSON representation
	 * @param json
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public CatanCommand(JSONObject json) throws Exception {
		String methodName = (String) json.get("method");
		List<Object> jsonArgs = (List<Object>) json.get("arguments");

		List<Object> args = new ArrayList<>();
		for (Object arg : jsonArgs) {
			if (arg instanceof JSONObject) {
				JSONObject jsonArg = (JSONObject) arg;
				Class<?> type = Class.forName((String) jsonArg.get("<class>"));
				jsonArg.remove("<class>");
				try {
					Method converter = type.getMethod("fromJSONObject", JSONObject.class);

					if (Modifier.isStatic(converter.getModifiers())
							&& converter.getReturnType().equals(type)) {
						args.add(converter.invoke(null, jsonArg));
					}
					else throw new Exception(); // a bit hackish...
				} catch (Exception e) {
					try {
						Constructor<?> ctor = type
								.getConstructor(JSONObject.class);

						args.add(ctor.newInstance(jsonArg));
					} catch (Exception ex) {
						ex.printStackTrace();
						args.add(null);
					}
				}
			} else {
				args.add(arg);
			}
		}
		
		setDispatch(methodName, args.toArray());
	}

	@Override
	public void execute(ModelFacade model) throws InvalidActionException {
		try {
			method.invoke(model, arguments);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
			throw new InvalidActionException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject json = new JSONObject();

		List<Object> args = new ArrayList<>();
		for (Object arg : arguments) {
			args.add(makeJSONCompatible(arg));
		}

		json.put("method", method.getName());
		json.put("arguments", args);

		return json;
	}

	@SuppressWarnings("unchecked")
	private Object makeJSONCompatible(Object arg) {
		Class<?> type = arg.getClass();
		if (type.isArray()) {
			return Arrays.asList((Object[]) arg);
		} else if (Utils.isBuiltinType(type)) {
			return arg; // The JSON serializer handles this properly
		} else {
			try {
				// Time to fake duck typing! YAAAY!
				Method converter = type.getMethod("toJSONObject");

				assert converter.getReturnType().equals(JSONObject.class);
				JSONObject json = (JSONObject) converter.invoke(arg);

				// Make sure to put in type information so we can get the data
				// back later
				json.put("<class>", type.getCanonicalName());

				return json;
			} catch (NoSuchMethodException | SecurityException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				assert false;
				return null;
			}
		}
	}

}
