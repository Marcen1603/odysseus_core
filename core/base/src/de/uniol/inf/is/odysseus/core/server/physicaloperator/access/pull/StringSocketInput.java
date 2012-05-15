package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.util.Scanner;

/**
 * SocketInput implementation for strings using Java Scanner class
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class StringSocketInput extends SocketInput<String> {
	private final String charset;
	final private String objectDelimiter;
	private Scanner scanner;

	public StringSocketInput(String hostname, int port, String user,
			String password, String charset, String objectDelimiter) {
		super(hostname, port, user, password);
		this.objectDelimiter = objectDelimiter;
		this.charset = charset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.SocketInput
	 * #init()
	 */
	@Override
	public void init() {
		super.init();
		this.scanner = new Scanner(getInputStream(), charset);
		scanner.useDelimiter(objectDelimiter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.IInput
	 * #hasNext()
	 */
	@Override
	public boolean hasNext() {
		return scanner.hasNext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.IInput
	 * #getNext()
	 */
	@Override
	public String getNext() {
		return scanner.next();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.SocketInput
	 * #terminate()
	 */
	@Override
	public void terminate() {
		this.scanner.close();
		super.terminate();
	}
}
