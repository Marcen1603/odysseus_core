package de.uniol.inf.is.odysseus.admission.event;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;


public class QueryStopAdmissionEvent extends AbstractQueryAdmissionEvent {

	public QueryStopAdmissionEvent( IPhysicalQuery query ) {
		super(query);
	}
}
