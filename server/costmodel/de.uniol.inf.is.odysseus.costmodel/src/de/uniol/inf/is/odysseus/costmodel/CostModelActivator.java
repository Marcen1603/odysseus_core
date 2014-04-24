package de.uniol.inf.is.odysseus.costmodel;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.costmodel.impl.LogicalCostModel;
import de.uniol.inf.is.odysseus.costmodel.util.CostModelUtil;

public class CostModelActivator implements BundleActivator, IPlanModificationListener {

	private static final Logger LOG = LoggerFactory.getLogger(CostModelActivator.class);

	private static IServerExecutor serverExecutor;
	
	public void bindExecutor( IExecutor serv) {
		serverExecutor = (IServerExecutor)serv;
		serverExecutor.addPlanModificationListener(this);
	}
	
	public void unbindExecutor( IExecutor serv ) {
		if( serverExecutor == serv ) {
			serverExecutor.removePlanModificationListener(this);
			serverExecutor = null;
		}
	}
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}

	@Override
	public synchronized void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (PlanModificationEventType.QUERY_ADDED.equals(eventArgs.getEventType())) {
			LOG.debug("ADD-Event. Trying cost estimation...");
			
//			PhysicalCostModel pcm = new PhysicalCostModel();
//			IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
//
//			pcm.estimateCost(query.getPhysicalChilds());
			
			LogicalCostModel lcm = new LogicalCostModel();
			IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
			ILogicalOperator logicalPlan = query.getLogicalQuery().getLogicalPlan();
			
			lcm.estimateCost(CostModelUtil.getAllLogicalOperators(logicalPlan));
		}
	}
}
