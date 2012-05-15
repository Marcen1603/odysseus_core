package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class StringSocketLineInput extends SocketInput<String> {

	private BufferedReader reader;

	public StringSocketLineInput(String hostname, int port, String user,
			String password) {
		super(hostname, port, user, password);
	}

	@Override
	public void init() {
		super.init();
		reader = new BufferedReader(new InputStreamReader(
				getInputStream())); 
	}
	
	@Override
	public boolean hasNext() {
		try {
			return reader.ready();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getNext() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
