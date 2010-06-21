package de.uniol.inf.is.odysseus.rcp.viewer.model.activator;

import java.util.ArrayList;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.viewer.model.Model;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.impl.OdysseusModelProviderMultipleSink;
import de.uniol.inf.is.odysseus.rcp.viewer.model.create.impl.OdysseusModelProviderSinkOneWay;

public class Activator implements BundleActivator, IPlanModificationListener  {

	private Logger logger = LoggerFactory.getLogger("rcp.viewer.model");
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		
		final Activator that = this;
		Thread t = new Thread( new Runnable() {

			@Override
			public void run() {
				ServiceTracker execTracker = new ServiceTracker(context, IAdvancedExecutor.class.getName(), null);
				execTracker.open();
				IAdvancedExecutor executor;
				try {
					executor = (IAdvancedExecutor) execTracker.waitForService(0);
					if (executor != null) {
						executor.addPlanModificationListener(that);
					} else {
						logger.error("cannot get executor service");
					}
					execTracker.close();
				} catch (InterruptedException e) {
					logger.error("cannot get executor service");
				} 			
			}
			
		});
		
		t.start();
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

}
