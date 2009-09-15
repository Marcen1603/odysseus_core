package de.uniol.inf.is.odysseus.planmanagement.executor.exception;

/**
 * NoCompilerLoadedException describes an {@link Exception} which occurs if no
 * optimizer services is registered.
 * 
 * @author Wolf Bauer
 * 
 */
public class NoOptimizerLoadedException extends PlanManagementException {

	private static final long serialVersionUID = -3471219280532519112L;

	/**
	 * Constructor of NoCompilerLoadedException. Message is:
	 * "Optimizer plugin is not loaded."
	 * 
	 */
	public NoOptimizerLoadedException() {
		super("Optimizer plugin is not loaded.");
	}
}