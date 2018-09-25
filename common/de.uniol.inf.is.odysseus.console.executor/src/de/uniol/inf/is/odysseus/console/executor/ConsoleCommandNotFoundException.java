package de.uniol.inf.is.odysseus.console.executor;

public class ConsoleCommandNotFoundException extends Exception {

	private static final String EXCEPTION_BASE_MSG = "Could not find the console command ";
	private static final long serialVersionUID = 1L;

	public ConsoleCommandNotFoundException() {
		super();
	}

	public ConsoleCommandNotFoundException(String command, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(EXCEPTION_BASE_MSG + command, cause, enableSuppression, writableStackTrace);
	}

	public ConsoleCommandNotFoundException(String command, Throwable cause) {
		super(EXCEPTION_BASE_MSG + command, cause);
	}

	public ConsoleCommandNotFoundException(String command) {
		super(EXCEPTION_BASE_MSG + command);
	}

	public ConsoleCommandNotFoundException(Throwable cause) {
		super(cause);
	}
}
