package de.uniol.inf.is.odysseus.admission.status;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.admission.IAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class ExecutorAdmissionStatusComponent implements IAdmissionStatusComponent {

	public int getQueryCount() {
		return AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan().getQueries().size();
	}
	
	public int getRunningQueryCount() {
		return getRunningQueryIDs().size();
	}
	
	public int getStoppedQueryCount() {
		return getStoppedQueryIDs().size();
	}
	
	public int getPartialQueryCount() {
		return getPartialQueryIDs().size();
	}
	
	public int getSuspendedQueryCount() {
		return getSuspendedQueryIDs().size();
	}
	
	public Collection<Integer> getPartialQueryIDs() {
		return getQueryIDsOfState(QueryState.PARTIAL);
	}
	
	public Collection<Integer> getRunningQueryIDs() {
		return getQueryIDsOfState(QueryState.RUNNING);
	}
	
	public Collection<Integer> getSuspendedQueryIDs() {
		return getQueryIDsOfState(QueryState.SUSPENDED);
	}
	
	public Collection<Integer> getStoppedQueryIDs() {
		return getQueryIDsOfState(QueryState.INACTIVE);
	}
	
	public boolean hasRunningQueries() {
		return hasQueriesOfState(QueryState.RUNNING);
	}
	
	public boolean hasPartialQueries() {
		return hasQueriesOfState(QueryState.PARTIAL);
	}
	
	public boolean hasSuspenedQueries() {
		return hasQueriesOfState(QueryState.SUSPENDED);
	}
	
	public boolean hasStoppedQueries() {
		return hasQueriesOfState(QueryState.INACTIVE);
	}
	
	public long getQueryStateTimeMillis( int queryID ) {
		long ts = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan().getQueryById(queryID).getLastQueryStateChangeTS();
		return System.currentTimeMillis() - ts;
	}
	
	private static Collection<Integer> getQueryIDsOfState(QueryState state) {
		Collection<IPhysicalQuery> queries = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan().getQueries();
		Collection<Integer> runningQueryIDs = Lists.newArrayList();
		for( IPhysicalQuery query : queries ) {
			if( query.getState() == state ) {
				runningQueryIDs.add(query.getID());
			}
		}
		
		return runningQueryIDs;
	}
	
	private boolean hasQueriesOfState(QueryState state) {
		Collection<IPhysicalQuery> queries = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan().getQueries();
		for( IPhysicalQuery query : queries ) {
			if( query.getState() == state ) {
				return true;
			}
		}
		
		return false;
	}
}
