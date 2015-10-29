package server.telnet;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GenericTelnetInterpreter extends SimpleTelnetInterpreter {

	public GenericTelnetInterpreter(OutputStream ostream) {
		super(ostream);
	}

	@Override
	protected boolean handle(String command, String[] args) {
		try {
			Method handler = this.getClass().getMethod(command, String[].class);
			
			if (handler.getReturnType().equals(Boolean.TYPE)) {
				try {
					return (boolean) handler.invoke(args);
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
	
	protected boolean quit(String[] args) {
		return false;
	}

}
