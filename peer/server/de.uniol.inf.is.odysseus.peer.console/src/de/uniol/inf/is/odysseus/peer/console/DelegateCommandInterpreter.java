package de.uniol.inf.is.odysseus.peer.console;

import java.util.Dictionary;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.osgi.framework.Bundle;

public class DelegateCommandInterpreter implements CommandInterpreter {

	private int i = 0;
	private final String[] args;

	public DelegateCommandInterpreter(String[] args) {
		this.args = args;
	}

	@Override
	public Object execute(String cmd) {
		return null;
	}

	@Override
	public String nextArgument() {
		if (i < args.length) {
			return this.args[i++];
		}
		return null;
	}

	@Override
	public void print(Object o) {
	}

	@Override
	public void printBundleResource(Bundle bundle, String resource) {
	}

	@Override
	public void printDictionary(@SuppressWarnings("rawtypes") Dictionary dic, String title) {
	}

	@Override
	public void printStackTrace(Throwable t) {
	}

	@Override
	public void println() {
	}

	@Override
	public void println(Object o) {
	}
}