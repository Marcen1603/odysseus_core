package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StringSocketLineInputHandler extends AbstractSocketInputHandler<String> {
	private static final Logger LOG = LoggerFactory
			.getLogger(StringSocketLineInputHandler.class);
	private BufferedReader reader;

	public StringSocketLineInputHandler() {
		// needed for declarative service
	}
	
	public StringSocketLineInputHandler(String hostname, int port, String user,
			String password) {
		super(hostname, port, user, password);
	}

	@Override
	public IInputHandler<String> getInstance(Map<String, String> options) {
		return new StringSocketLineInputHandler(options.get("host"), Integer.parseInt(options.get("port")), 
				options.get("user"), options.get("password"));
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
			LOG.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public String getNext() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public String getName() {
		return "StringSocketLine";
	}
	
}
