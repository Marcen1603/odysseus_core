package de.uniol.inf.is.odysseus.viewer;

import java.util.ArrayList;
import java.util.ListIterator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.viewer.model.create.OdysseusModelProviderSink;
import de.uniol.inf.is.odysseus.viewer.swt.resource.SWTResourceManager;

public class Activator implements BundleActivator, IPlanModificationListener {

	static BundleContext context;
	public static final String XSD_RESOURCES_FILE = "viewer_cfg/resourcesSchema.xsd";
	public static final String XSD_DIAGRAMM_SCHEMA_FILE = "viewer_cfg/diagramSchema.xsd";
	public static final String XSD_SYMBOL_SCHEMA_FILE = "viewer_cfg/symbolSchema.xsd";
	public static final String DIAGRAM_CFG_FILE = "viewer_cfg/diagram.xml";
	public static final String RESOURCES_FILE = "viewer_cfg/resources.xml";
	public static final String SYMBOL_CONFIG_FILE = "viewer_cfg/symbol.xml";
	private Thread viewerThread;
	private ViewerStarter viewerStarter;
	Logger logger = LoggerFactory.getLogger("viewer");

	@Override
	public void start(final BundleContext bc) throws Exception {
		context = bc;
		SWTResourceManager.resourceBundle = bc.getBundle();
		// TODO config aus properties service lesen
		// do initialization inside a thread, so bundle startup
		// isn't blocked by execTracker.waitForService(0)
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				ServiceTracker execTracker = new ServiceTracker(bc,
						IAdvancedExecutor.class.getName(), null);
				execTracker.open();
				IAdvancedExecutor executor;
				try {
					executor = (IAdvancedExecutor) execTracker
							.waitForService(0);
					if (executor != null) {
						ViewerStarterConfiguration cfg = new ViewerStarterConfiguration();
						viewerStarter = new ViewerStarter(null, cfg);
						viewerThread = new Thread(viewerStarter,
								"Viewer Thread");
						viewerThread.start();
						executor.addPlanModificationListener(Activator.this);
						updateModel(executor.getSealedPlan().getRoots());
					} else {
						logger.error("cannot get executor service");
					}
					execTracker.close();
				} catch (InterruptedException e) {
					logger.error("cannot get executor service");
				} catch (PlanManagementException e) {
					logger.error(e.getMessage());
				}
			}

		});
		// daemon thread, so shutdown can be completed,
		// even if waitForService(0) is blocking
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		context = null;
		SWTResourceManager.resourceBundle = null;
		if (viewerThread != null) {
			this.viewerThread.interrupt();
			this.viewerThread = null;
			this.viewerStarter = null;
		}
	}

	public static BundleContext getContext() {
		return context;
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		try {
			ArrayList<IPhysicalOperator> roots = eventArgs.getSender()
					.getSealedPlan().getRoots();
			updateModel(roots);
		} catch (PlanManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateModel(ArrayList<IPhysicalOperator> roots) {
		if (!roots.isEmpty()) {
			ListIterator<IPhysicalOperator> li = roots.listIterator(roots
					.size());
			IPhysicalOperator lastRoot = null;
			do {
				lastRoot = li.previous();
			} while (li.hasPrevious() && lastRoot == null);
			if (lastRoot != null && lastRoot instanceof ISink<?>) {
				this.viewerStarter
						.setModelProvider(new OdysseusModelProviderSink(
								(ISink<?>) lastRoot));
			}
		}
	}

}
