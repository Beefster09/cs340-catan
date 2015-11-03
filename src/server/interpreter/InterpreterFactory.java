package server.interpreter;

import java.io.OutputStream;

public interface InterpreterFactory {
	
	Interpreter getInterpreter(OutputStream out);

}
