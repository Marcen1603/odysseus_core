package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class JxtaRemoveOperatorPlanListener implements IPlanModificationListener {

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		final IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();

		if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) {
			List<IPhysicalOperator> operators = query.getPhysicalChilds();
			for( IPhysicalOperator operator : operators ) {
				if( !operator.isOpen() ) {
					if( operator instanceof JxtaReceiverPO ) {
						((JxtaReceiverPO<?>)operator).getTransmission().close();
					} else if( operator instanceof JxtaSenderPO ) {
						((JxtaSenderPO<?>)operator).getTransmission().close();
					} 
				}
			}
		} 
	}
	
}
