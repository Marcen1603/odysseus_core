package de.uniol.inf.is.odysseus.admission.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.admission.IAdmissionControl;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class ExecutorAdmissionEventGenerator implements IPlanModificationListener {

	private static final Logger LOG = LoggerFactory.getLogger(ExecutorAdmissionEventGenerator.class);
	
	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		IEventType eventType = eventArgs.getEventType();
		IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
		
		IAdmissionControl ac = AdmissionEventPlugIn.getAdmissionControl();
		int queryID = query.getID();
		if( PlanModificationEventType.QUERY_ADDED.equals(eventType)) {
			LOG.debug("Got query add event for query id {}", queryID);
			ac.processEventAsync(new QueryAddAdmissionEvent(queryID));
			
		} else if( PlanModificationEventType.QUERY_REMOVE.equals(eventType)) {
			LOG.debug("Got query remove event for query id {}", queryID);
			ac.processEventAsync(new QueryRemoveAdmissionEvent(queryID));
			
		} else if( PlanModificationEventType.QUERY_START.equals(eventType)) {
			LOG.debug("Got query start event for query id {}", queryID);
			ac.processEventAsync(new QueryStartAdmissionEvent(queryID));
			
		} else if( PlanModificationEventType.QUERY_STOP.equals(eventType)) {
			LOG.debug("Got query stop event for query id {}", queryID);
			ac.processEventAsync(new QueryStopAdmissionEvent(queryID));
			
		} else if( PlanModificationEventType.QUERY_SUSPEND.equals(eventType)) {
			LOG.debug("Got query suspend event for query id {}", queryID);
			ac.processEventAsync(new QuerySuspendAdmissionEvent(queryID));
			
		} else if( PlanModificationEventType.QUERY_RESUME.equals(eventType)) {
			LOG.debug("Got query resume event for query id {}", queryID);
			ac.processEventAsync(new QueryResumeAdmissionEvent(queryID));
			
		} 
		
	}

}
