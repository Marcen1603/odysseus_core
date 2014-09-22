package de.uniol.inf.is.odysseus.peer.console;

import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleOutputStream extends PrintStream {

	private String output = "";
	
	public ConsoleOutputStream(OutputStream out) {
		super(out);
	}
	
	@Override
	public void println(String x) {
		output = output + x + "\n";
	}
	 
	@Override
	public void println() {
		output = output + "\n";
	}
	
	public String getOutput() {
		return output;
	}
}
