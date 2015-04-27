package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPlanManager;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.QueryPlanModificationEvent;

public class PlanModificationWatcherPO extends AbstractSource<Tuple<?>> implements IPlanModificationListener {

	final IPlanManager planManager;

	public PlanModificationWatcherPO(IPlanManager planManager){
		this.planManager = planManager;
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		planManager.addPlanModificationListener(this);
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (eventArgs instanceof QueryPlanModificationEvent){
			QueryPlanModificationEvent e = (QueryPlanModificationEvent) eventArgs;
			Tuple<IMetaAttribute> output = new Tuple<IMetaAttribute>(3,false);
			output.setAttribute(0, System.currentTimeMillis());
			output.setAttribute(1, e.getValue().getID()); 
			output.setAttribute(2, e.getEventType().toString());
						
			// BUG: Output has no Metadata
			
			transfer(output);
		}
	}
	
	@Override
	protected void process_close() {
		planManager.removePlanModificationListener(this);
	}

}
