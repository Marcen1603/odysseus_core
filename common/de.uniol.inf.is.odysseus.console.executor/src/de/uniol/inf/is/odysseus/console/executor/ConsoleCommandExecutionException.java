package de.uniol.inf.is.odysseus.console.executor;

public class ConsoleCommandExecutionException extends Exception {

	private static final String EXCEPTION_BASE_MSG = "An exception during executing the command is thrown: ";
	private static final long serialVersionUID = 1L;

	public ConsoleCommandExecutionException() {
		super();
	}

	public ConsoleCommandExecutionException(String command, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(EXCEPTION_BASE_MSG + command, cause, enableSuppression, writableStackTrace);
	}

	public ConsoleCommandExecutionException(String command, Throwable cause) {
		super(EXCEPTION_BASE_MSG + command, cause);
	}

	public ConsoleCommandExecutionException(String command) {
		super(EXCEPTION_BASE_MSG + command);
	}

	public ConsoleCommandExecutionException(Throwable cause) {
		super(cause);
	}

	
}
