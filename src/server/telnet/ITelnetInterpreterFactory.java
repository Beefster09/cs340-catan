package server.telnet;

import java.io.OutputStream;

public interface ITelnetInterpreterFactory {
	
	ITelnetInterpreter getInterpreter(OutputStream out);

}
