package de.uniol.inf.is.odysseus.admission.action;

import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;

public class SuspendQueryAdmissionAction extends AbstractQueryAdmissionAction {

	public SuspendQueryAdmissionAction( int queryID ) {
		super(queryID);
	}
	
	@Override
	public void execute(IAdmissionEvent baseEvent) {
		AdmissionActionPlugIn.getServerExecutor().suspendQuery(getQueryID(), AdmissionActionPlugIn.getActiveSession());
	}

	@Override
	public void revert(IAdmissionEvent baseEvent) {
	}
	
}
