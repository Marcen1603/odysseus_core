package de.uniol.inf.is.odysseus.admission.event;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.admission.IAdmissionEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public abstract class AbstractQueryAdmissionEvent implements IAdmissionEvent {

	private final IPhysicalQuery query;
	
	public AbstractQueryAdmissionEvent(IPhysicalQuery query) {
		Preconditions.checkNotNull(query, "physical query must not be null!");
		
		this.query = query;
	}

	public int getQueryID() {
		return query.getID();
	}
	
	public int getPriority() {
		return query.getPriority();
	}
	
	public IPhysicalQuery getQuery() {
		return query;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "(" + getQueryID() + ")";
	}
}
