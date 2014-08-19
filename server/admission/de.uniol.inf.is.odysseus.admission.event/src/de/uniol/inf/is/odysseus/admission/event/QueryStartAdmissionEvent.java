package de.uniol.inf.is.odysseus.admission.event;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;


public class QueryStartAdmissionEvent extends AbstractQueryAdmissionEvent {

	public QueryStartAdmissionEvent( IPhysicalQuery query ) {
		super(query);
	}
}
