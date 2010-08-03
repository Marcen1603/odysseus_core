package de.uniol.inf.is.odysseus.rcp.viewer.model.activator;

import java.util.ArrayList;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.viewer.model.Model;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.impl.OdysseusModelProviderMultipleSink;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.impl.OdysseusModelProviderSinkOneWay;

public class Activator implements BundleActivator, IPlanModificationListener  {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		try {
			ArrayList<IPhysicalOperator> roots = eventArgs.getSender().getSealedPlan().getRoots();
			ArrayList<IQuery> queries = eventArgs.getSender().getSealedPlan().getQueries();
			if (!roots.isEmpty()) {

				IPhysicalOperator lastRoot = roots.get(roots.size() - 1);
				IQuery lastQuery = queries.get(queries.size() - 1);
				if (lastRoot != null && lastRoot instanceof ISink<?>) {
					IModelProvider<IPhysicalOperator> provider = new OdysseusModelProviderSinkOneWay((ISink<?>) lastRoot, lastQuery);
					
					ArrayList<ISink<?>> sinkRoots = new ArrayList<ISink<?>>();
					for(IPhysicalOperator po : roots ) 
						sinkRoots.add((ISink<?>)po);
					
					IModelProvider<IPhysicalOperator> providerActiveModel = new OdysseusModelProviderMultipleSink(sinkRoots);
					
					Model.getInstance().getModelManager().addModel(provider.get());
					Model.getInstance().getModelManager().setActiveModel(providerActiveModel.get());
				}
			}
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}

	public void bindExecutor(IAdvancedExecutor executor) {
		executor.addPlanModificationListener(this);
		StatusBarManager.getInstance().setMessage(StatusBarManager.EXECUTOR_ID, "Executor ready");
	}
	
	public void unbindExecutor(IAdvancedExecutor executor) {
		executor.removePlanModificationListener(this);
		StatusBarManager.getInstance().setMessage(StatusBarManager.EXECUTOR_ID, "No executor found");
	}
}
