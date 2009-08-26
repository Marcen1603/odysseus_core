package de.uniol.inf.is.odysseus.scheduler.exception;

public class NoSchedulerLoadedException extends Exception {

	private static final long serialVersionUID = 6796481607553864967L;

	public NoSchedulerLoadedException() {
		super("No scheduler loaded.");
	}
}
