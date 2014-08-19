package de.uniol.inf.is.odysseus.admission.event;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;


public class QuerySuspendAdmissionEvent extends AbstractQueryAdmissionEvent {

	public QuerySuspendAdmissionEvent( IPhysicalQuery query ) {
		super(query);
	}
}
