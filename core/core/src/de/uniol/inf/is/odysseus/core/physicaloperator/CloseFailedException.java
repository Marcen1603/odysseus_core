package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;

public class CloseFailedException extends PlanManagementException {

	private static final long serialVersionUID = -5963534548010949966L;

	public CloseFailedException(String message) {
		super(message);
	}
	
	public CloseFailedException(Exception e) {
		super(e);
	}
	
}
