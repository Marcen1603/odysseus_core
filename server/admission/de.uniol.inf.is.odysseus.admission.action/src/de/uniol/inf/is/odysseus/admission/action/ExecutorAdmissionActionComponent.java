package de.uniol.inf.is.odysseus.admission.action;

import de.uniol.inf.is.odysseus.admission.IAdmissionActionComponent;

public class ExecutorAdmissionActionComponent implements IAdmissionActionComponent {

	public void startQuery( int queryID ) {
		AdmissionActionPlugIn.getServerExecutor().startQuery(queryID, AdmissionActionPlugIn.getActiveSession());
	}
	
	public void stopQuery( int queryID ) {
		AdmissionActionPlugIn.getServerExecutor().stopQuery(queryID, AdmissionActionPlugIn.getActiveSession());
	}
	
	public void suspendQuery( int queryID ) {
		AdmissionActionPlugIn.getServerExecutor().suspendQuery(queryID, AdmissionActionPlugIn.getActiveSession());
	}
	
	public void resumeQuery( int queryID ) {
		AdmissionActionPlugIn.getServerExecutor().resumeQuery(queryID, AdmissionActionPlugIn.getActiveSession());
	}
	
	public void removeQuery( int queryID ) {
		AdmissionActionPlugIn.getServerExecutor().removeQuery(queryID, AdmissionActionPlugIn.getActiveSession());
	}
	
	public void partialQuery( int queryID, int sheddingFactor ) {
		AdmissionActionPlugIn.getServerExecutor().partialQuery(queryID, sheddingFactor, AdmissionActionPlugIn.getActiveSession());
	}

	public void unpartialQuery( int queryID ) {
		AdmissionActionPlugIn.getServerExecutor().partialQuery(queryID, 0, AdmissionActionPlugIn.getActiveSession());
	}
}
