package de.uniol.inf.is.odysseus.admission.event;

import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;

public abstract class AbstractQueryAdmissionEvent implements IAdmissionEvent {

	private final int queryID;

	public AbstractQueryAdmissionEvent(int queryID) {
		this.queryID = queryID;
	}

	public int getQueryID() {
		return queryID;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + queryID + ")";
	}
}
