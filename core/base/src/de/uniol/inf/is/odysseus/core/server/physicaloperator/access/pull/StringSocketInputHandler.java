package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

import java.util.Map;
import java.util.Scanner;

/**
 * SocketInput implementation for strings using Java Scanner class
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class StringSocketInputHandler extends
		AbstractSocketInputHandler<String> {
	private String charset;
	private String objectDelimiter;
	private Scanner scanner;
	private boolean keepDelimiter;

	public StringSocketInputHandler() {
		// needed for declarative service
	}

	public StringSocketInputHandler(String hostname, int port, String user,
			String password, String charset, String objectDelimiter,
			boolean keepDelimiter) {
		super(hostname, port, user, password);
		this.objectDelimiter = objectDelimiter;
		this.charset = charset;
		this.keepDelimiter = keepDelimiter;
	}

	@Override
	public IInputHandler<String> getInstance(Map<String, String> options) {
		return new StringSocketInputHandler(options.get("host"),
				Integer.parseInt(options.get("port")), options.get("user"),
				options.get("password"), options.get("charset"),
				options.get("delimiter"), Boolean.parseBoolean(options
						.get("keepdelimiter")));
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
		if (keepDelimiter) {
			return scanner.next()+objectDelimiter;
		} else {
			return scanner.next();
		}
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

	@Override
	public String getName() {
		return "StringSocket";
	}
}
