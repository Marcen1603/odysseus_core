package de.uniol.inf.is.odysseus.admission.status;

import java.util.Collection;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.admission.IAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class ExecutorAdmissionStatusComponent implements IAdmissionStatusComponent {

	public int getRunningQueryCount() {
		return getRunningQueryIDs().size();
	}
	
	public Collection<Integer> getRunningQueryIDs() {
		Collection<IPhysicalQuery> queries = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan().getQueries();
		Collection<Integer> runningQueryIDs = Lists.newArrayList();
		for( IPhysicalQuery query : queries ) {
			if( query.isOpened() ) {
				runningQueryIDs.add(query.getID());
			}
		}
		
		return runningQueryIDs;
	}
	
	public boolean hasRunningQueries() {
		Collection<IPhysicalQuery> queries = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan().getQueries();
		for( IPhysicalQuery query : queries ) {
			if( query.isOpened() ) {
				return true;
			}
		}
		
		return false;
	}
}
