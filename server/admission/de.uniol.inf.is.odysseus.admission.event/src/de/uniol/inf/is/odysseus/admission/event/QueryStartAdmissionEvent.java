package de.uniol.inf.is.odysseus.admission.event;


public class QueryStartAdmissionEvent extends AbstractQueryAdmissionEvent {

	public QueryStartAdmissionEvent( int queryID ) {
		super(queryID);
	}
}
