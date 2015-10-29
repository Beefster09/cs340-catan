package server.telnet;

import java.io.OutputStream;

public class SimpleTelnetInterpreterFactory implements ITelnetInterpreterFactory {
	
	public SimpleTelnetInterpreterFactory() {}

	@Override
	public ITelnetInterpreter getInterpreter(OutputStream out) {
		return new SimpleTelnetInterpreter(out);
	}

	

}
