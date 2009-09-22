package de.uniol.inf.is.odysseus.scheduler.exception;

/**
 * SchedulingException describes an {@link Exception} which occurs while trying
 * to access a scheduler and no scheduler is registered.
 * 
 * @author Wolf Bauer
 * 
 */
/**
 * @author Wolf Bauer
 *
 */
public class NoSchedulerLoadedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6796481607553864967L;

	/**
	 * Creates a new NoSchedulerLoadedException.
	 */
	public NoSchedulerLoadedException() {
		super("No scheduler loaded.");
	}
}
