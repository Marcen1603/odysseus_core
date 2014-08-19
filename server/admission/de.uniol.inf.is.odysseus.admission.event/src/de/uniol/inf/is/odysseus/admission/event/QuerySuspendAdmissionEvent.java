package de.uniol.inf.is.odysseus.admission.event;


public class QuerySuspendAdmissionEvent extends AbstractQueryAdmissionEvent {

	public QuerySuspendAdmissionEvent( int queryID ) {
		super(queryID);
	}
}
