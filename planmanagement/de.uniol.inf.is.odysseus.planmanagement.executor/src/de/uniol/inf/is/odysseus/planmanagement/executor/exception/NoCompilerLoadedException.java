package de.uniol.inf.is.odysseus.planmanagement.executor.exception;

/**
 * NoCompilerLoadedException describes an {@link Exception} which occurs if no
 * compiler services is registered.
 * 
 * @author Wolf Bauer
 * 
 */
public class NoCompilerLoadedException extends PlanManagementException {

	private static final long serialVersionUID = -3471219280532519112L;

	/**
	 * Constructor of NoCompilerLoadedException. Message is:
	 * "Compiler plugin is not loaded."
	 * 
	 */
	public NoCompilerLoadedException() {
		super("Compiler plugin is not loaded.");
	}
}