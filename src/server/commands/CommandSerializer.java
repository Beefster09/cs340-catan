package server.commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CommandSerializer {
	
	public static final JSONParser parser = new JSONParser();
	
	/** Serializes a command so it can be stored or transmitted
	 * @param command The command to serialize
	 * @return A string containing a JSON-formatted representation of the command
	 * @throws CommandSerializationException if something prevented the command from being serialized.
	 */
	@SuppressWarnings("unchecked")
	public static String serialize(ICatanCommand command) throws CommandSerializationException{
		try {
			JSONObject json = command.toJSONObject();
			json.put("<command-class>", command.getClass().getName());
			return json.toJSONString();
		} catch (Exception e) {
			throw new CommandSerializationException("Could not serialize the command", e);
		}
	}
	
	/** Deserializes a command that was previously serialized
	 * @param input The JSON-formatted string
	 * @return An ICatanCommand with the same data as was represented by the JSON
	 * @throws ParseException if the input is not valid JSON
	 * @throws CommandSerializationException if something else prevented the command
	 * from being deserialized
	 */
	@SuppressWarnings("unchecked")
	public static ICatanCommand deserialize(String input)
			throws ParseException, CommandSerializationException {
		JSONObject json = (JSONObject) parser.parse(input);
		try {
			String className = (String) json.get("<command-class>");
			json.remove("<command-class>");
			Class<?> commandClass = Class.forName(className);
			Constructor<? extends ICatanCommand> ctor = 
					(Constructor<? extends ICatanCommand>) 
					commandClass.getConstructor(JSONObject.class);
			return ctor.newInstance(json);
		} catch (ClassNotFoundException | NoSuchMethodException
				| SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | ClassCastException e) {
			throw new CommandSerializationException("Could not deserialize the command.", e);
		}
		
	}

}
