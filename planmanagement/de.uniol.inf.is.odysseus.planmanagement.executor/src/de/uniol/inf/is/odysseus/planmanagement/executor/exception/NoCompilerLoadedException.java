package de.uniol.inf.is.odysseus.planmanagement.executor.exception;

public class NoCompilerLoadedException extends PlanManagementException {

	private static final long serialVersionUID = -3471219280532519112L;

	public NoCompilerLoadedException() {
		super("Compiler plugin is not loaded.");
	}
}