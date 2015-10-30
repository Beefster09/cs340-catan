package server.telnet;

import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GenericInterpreterFactory implements
		InterpreterFactory {
	
	Constructor<? extends GenericInterpreter> interpCtor;
	
	public GenericInterpreterFactory(
			Class<? extends GenericInterpreter> interpClass)
			throws NoSuchMethodException, SecurityException {
		this.interpCtor = interpClass.getConstructor(OutputStream.class);
	}

	@Override
	public Interpreter getInterpreter(OutputStream out) {
		try {
			return interpCtor.newInstance(out);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

}
