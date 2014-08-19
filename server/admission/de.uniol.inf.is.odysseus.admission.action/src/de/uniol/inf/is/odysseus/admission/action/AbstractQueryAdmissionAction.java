package de.uniol.inf.is.odysseus.admission.action;

import de.uniol.inf.is.odysseus.admission.IAdmissionAction;

public abstract class AbstractQueryAdmissionAction implements IAdmissionAction {

	private final int queryID;
	
	public AbstractQueryAdmissionAction( int queryID ) {
		this.queryID = queryID;
	}
	
	public int getQueryID() {
		return queryID;
	}
}
