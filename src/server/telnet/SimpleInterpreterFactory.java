package server.telnet;

import java.io.OutputStream;

public class SimpleInterpreterFactory implements InterpreterFactory {
	
	public SimpleInterpreterFactory() {}

	@Override
	public Interpreter getInterpreter(OutputStream out) {
		return new SimpleInterpreter(out);
	}

	

}
