package de.uniol.inf.is.odysseus.admission.rules;

import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;

public class CheckQueryAgainAdmissionEvent implements IAdmissionEvent {

	private final int queryID;
	
	public CheckQueryAgainAdmissionEvent( int queryID ) {
		this.queryID = queryID;
	}
	
	public int getQueryID() {
		return queryID;
	}
}
