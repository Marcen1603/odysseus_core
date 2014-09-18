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
		return getQueryCount() - getRunningQueryCount() - getPartialQueryCount();
	}
	
	public int getPartialQueryCount() {
		return getPartialQueryIDs().size();
	}
	
	public Collection<Integer> getPartialQueryIDs() {
		Collection<IPhysicalQuery> queries = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan().getQueries();
		Collection<Integer> runningQueryIDs = Lists.newArrayList();
		for( IPhysicalQuery query : queries ) {
			if( query.getState() == QueryState.PARTIAL ) {
				runningQueryIDs.add(query.getID());
			}
		}
		
		return runningQueryIDs;
	}
	
	public Collection<Integer> getRunningQueryIDs() {
		Collection<IPhysicalQuery> queries = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan().getQueries();
		Collection<Integer> runningQueryIDs = Lists.newArrayList();
		for( IPhysicalQuery query : queries ) {
			if( query.getState() == QueryState.RUNNING ) {
				runningQueryIDs.add(query.getID());
			}
		}
		
		return runningQueryIDs;
	}
	
	public boolean hasRunningQueries() {
		Collection<IPhysicalQuery> queries = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan().getQueries();
		for( IPhysicalQuery query : queries ) {
			if( query.getState() == QueryState.RUNNING ) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean hasPartialQueries() {
		Collection<IPhysicalQuery> queries = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan().getQueries();
		for( IPhysicalQuery query : queries ) {
			if( query.getState() == QueryState.PARTIAL ) {
				return true;
			}
		}
		
		return false;
	}
}
