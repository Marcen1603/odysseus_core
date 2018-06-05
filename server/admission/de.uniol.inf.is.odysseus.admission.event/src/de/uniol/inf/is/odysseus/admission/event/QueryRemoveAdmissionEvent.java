package de.uniol.inf.is.odysseus.admission.event;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;


public class QueryRemoveAdmissionEvent extends AbstractQueryAdmissionEvent {

	public QueryRemoveAdmissionEvent( IPhysicalQuery query ) {
		super(query);
	}
}
