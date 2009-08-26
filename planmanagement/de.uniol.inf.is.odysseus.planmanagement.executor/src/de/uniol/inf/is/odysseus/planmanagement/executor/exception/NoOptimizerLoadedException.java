package de.uniol.inf.is.odysseus.planmanagement.executor.exception;

public class NoOptimizerLoadedException extends PlanManagementException {

	private static final long serialVersionUID = -3471219280532519112L;

	public NoOptimizerLoadedException() {
		super("Optimizer plugin is not loaded.");
	}
}