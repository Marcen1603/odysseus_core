package de.uniol.inf.is.odysseus.admission.status;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

/**
 * Provides the status for complex load shedding in consideration of priorities.
 * 
 * The CPU load together witch the latency and queue lengths of each query is used in this status component.
 * 
 * @author Jannes
 *
 */
public class ComplexLoadSheddingWPAdmissionStatusComponent implements ILoadSheddingAdmissionStatusComponent {

	@Override
	public void addQuery(IPhysicalQuery query) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeQuery(IPhysicalQuery query) {
		// TODO Auto-generated method stub

	}

	@Override
	public void runLoadShedding() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollBackLoadShedding() {
		// TODO Auto-generated method stub

	}

	@Override
	public void measureStatus() {
		// TODO Auto-generated method stub
		
	}

}
