package server.telnet;

import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GenericTelnetInterpreterFactory implements
		ITelnetInterpreterFactory {
	
	Constructor<? extends GenericTelnetInterpreter> interpCtor;
	
	public GenericTelnetInterpreterFactory(
			Class<? extends GenericTelnetInterpreter> interpClass)
			throws NoSuchMethodException, SecurityException {
		this.interpCtor = interpClass.getConstructor(OutputStream.class);
	}

	@Override
	public ITelnetInterpreter getInterpreter(OutputStream out) {
		try {
			return interpCtor.newInstance(out);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

}