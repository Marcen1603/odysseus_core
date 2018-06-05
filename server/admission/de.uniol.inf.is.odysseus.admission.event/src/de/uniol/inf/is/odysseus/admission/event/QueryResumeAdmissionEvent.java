package de.uniol.inf.is.odysseus.admission.event;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;


public class QueryResumeAdmissionEvent extends AbstractQueryAdmissionEvent {

	public QueryResumeAdmissionEvent( IPhysicalQuery query ) {
		super(query);
	}
}
