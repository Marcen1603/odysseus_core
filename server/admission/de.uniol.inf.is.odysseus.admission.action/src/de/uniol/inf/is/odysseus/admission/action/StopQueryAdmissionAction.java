package de.uniol.inf.is.odysseus.admission.action;

import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;

public class StopQueryAdmissionAction extends AbstractQueryAdmissionAction {

	public StopQueryAdmissionAction( int queryID ) {
		super(queryID);
	}
	
	@Override
	public void execute(IAdmissionEvent baseEvent) {
		AdmissionActionPlugIn.getServerExecutor().stopQuery(getQueryID(), AdmissionActionPlugIn.getActiveSession());
	}

	@Override
	public void revert(IAdmissionEvent baseEvent) {
		AdmissionActionPlugIn.getServerExecutor().startQuery(getQueryID(), AdmissionActionPlugIn.getActiveSession());
	}
	
}
