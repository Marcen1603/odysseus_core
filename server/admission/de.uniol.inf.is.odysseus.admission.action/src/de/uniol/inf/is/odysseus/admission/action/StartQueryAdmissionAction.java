package de.uniol.inf.is.odysseus.admission.action;

import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;
import de.uniol.inf.is.odysseus.admission.IAdmissionStatus;

public class StartQueryAdmissionAction extends AbstractQueryAdmissionAction {

	public StartQueryAdmissionAction( int queryID ) {
		super(queryID);
	}
	
	@Override
	public void execute(IAdmissionEvent baseEvent, IAdmissionStatus status) {
		AdmissionActionPlugIn.getServerExecutor().startQuery(getQueryID(), AdmissionActionPlugIn.getActiveSession());
	}

	@Override
	public void revert(IAdmissionEvent baseEvent, IAdmissionStatus status) {
		AdmissionActionPlugIn.getServerExecutor().stopQuery(getQueryID(), AdmissionActionPlugIn.getActiveSession());
	}
	
}
