package de.uniol.inf.is.odysseus.admission.action;

import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;

public class ResumeQueryAdmissionAction extends AbstractQueryAdmissionAction {

	public ResumeQueryAdmissionAction( int queryID ) {
		super(queryID);
	}
	
	@Override
	public void execute(IAdmissionEvent baseEvent) {
		AdmissionActionPlugIn.getServerExecutor().resumeQuery(getQueryID(), AdmissionActionPlugIn.getActiveSession());
	}

	@Override
	public void revert(IAdmissionEvent baseEvent) {
	}
	
}
