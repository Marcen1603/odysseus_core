package de.uniol.inf.is.odysseus.admission.action;

import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;

public class RemoveQueryAdmissionAction extends AbstractQueryAdmissionAction {

	public RemoveQueryAdmissionAction( int queryID ) {
		super(queryID);
	}
	
	@Override
	public void execute(IAdmissionEvent baseEvent) {
		AdmissionActionPlugIn.getServerExecutor().removeQuery(getQueryID(), AdmissionActionPlugIn.getActiveSession());
	}

	@Override
	public void revert(IAdmissionEvent baseEvent) {
	}
	
}
