package de.uniol.inf.is.odysseus.admission.event;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;


public class QueryAddAdmissionEvent extends AbstractQueryAdmissionEvent {

	public QueryAddAdmissionEvent( IPhysicalQuery query ) {
		super(query);
	}
}
