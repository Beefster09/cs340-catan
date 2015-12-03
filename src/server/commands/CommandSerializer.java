package server.commands;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.xml.bind.DatatypeConverter;

public class CommandSerializer {
	
	/** Serializes a command so it can be stored or transmitted
	 * @param command The command to serialize
	 * @return A string containing a binary-formatted representation of the command
	 * @throws CommandSerializationException if something prevented the command from being serialized.
	 */
	public static String serialize(ICatanCommand command)
			throws CommandSerializationException {
		SerializableCatanCommand serCommand = command.getSerializable();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		try {
			ObjectOutputStream oStream = new ObjectOutputStream(buffer);
			oStream.writeObject(serCommand);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new CommandSerializationException("Could not serialize the command.", e);
		}
		
		return DatatypeConverter.printBase64Binary(buffer.toByteArray());
	}
	
	/** Deserializes a command that was previously serialized
	 * @param input The binary-formatted string
	 * @return An ICatanCommand with the same data as was represented by the JSON
	 * @throws ParseException if the input is not valid JSON
	 * @throws CommandSerializationException if something else prevented the command
	 * from being deserialized
	 */
	public static ICatanCommand deserialize(String input) throws CommandSerializationException {
		ByteArrayInputStream buffer = new ByteArrayInputStream(
				DatatypeConverter.parseBase64Binary(input));
		try {
			ObjectInputStream iStream = new ObjectInputStream(buffer);
			return ((SerializableCatanCommand) iStream.readObject()).getCommand();
		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			throw new CommandSerializationException("Could not serialize the command.", e);
		}
	}

}
