package de.uniol.inf.is.odysseus.admission.status;

import java.util.Collection;

import de.uniol.inf.is.odysseus.admission.IAdmissionStatusComponent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class ExecutorAdmissionStatusComponent implements IAdmissionStatusComponent {

	public int getRunningQueryCount() {
		Collection<IPhysicalQuery> queries = AdmissionStatusPlugIn.getServerExecutor().getExecutionPlan().getQueries();
		int runningQueryCount = 0;
		for( IPhysicalQuery query : queries ) {
			if( query.isOpened() ) {
				runningQueryCount++;
			}
		}
		
		return runningQueryCount;
	}
	
}
