package de.uniol.inf.is.odysseus.rcp.viewer.query.impl;


import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.event.IEventListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.viewer.query.QueryHistory;
import de.uniol.inf.is.odysseus.scheduler.event.SchedulerManagerEvent;
import de.uniol.inf.is.odysseus.scheduler.event.SchedulerManagerEvent.SchedulerManagerEventType;
import de.uniol.inf.is.odysseus.scheduler.event.SchedulingEvent.SchedulingEventType;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin implements IEventListener {

	static Logger logger = LoggerFactory.getLogger(Activator.class);
	
	private static Activator plugin;
	private static IExecutor executor = null;

	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

//		QueryBuildConfigurationRegistry.getInstance().loadExtensionList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		QueryHistory.getInstance().save();
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public static IExecutor getExecutor() {
		return executor;
	}

	// Declarative Service
	public void bindExecutor(IExecutor ex) {
		executor = ex;
		StatusBarManager.getInstance().setMessage(StatusBarManager.EXECUTOR_ID, "Executor "+executor.getName()+" ready");
		try {
			StatusBarManager.getInstance().setMessage(StatusBarManager.SCHEDULER_ID,executor.getCurrentSchedulerID()+" ("+executor.getCurrentSchedulingStrategyID()+ ") "+(executor.isRunning()?" running ":" stopped "));
			executor.getSchedulerManager().subscribeToAll(this);
			executor.getSchedulerManager().getActiveScheduler().subscribeToAll(this);
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
	}

	// Declarative Service
	public void unbindExecutor(IExecutor ex) {
		executor = null;
		try{
			StatusBarManager.getInstance().setMessage(StatusBarManager.EXECUTOR_ID,
			"No executor found");
		}catch(Exception e){
			//ignore
		}
	}

	@Override
	public void eventOccured(IEvent<?, ?> event) {
		
		logger.debug(""+event);
		
		if (event.getEventType() == SchedulerManagerEventType.SCHEDULER_REMOVED){
			((SchedulerManagerEvent)event).getValue().unSubscribeFromAll(this);
		}else if (event.getEventType() == SchedulerManagerEventType.SCHEDULER_SET){
			((SchedulerManagerEvent)event).getValue().subscribeToAll(this);
		}
		
		if (event.getEventType() == SchedulingEventType.SCHEDULING_STARTED
				|| event.getEventType() == SchedulingEventType.SCHEDULING_STOPPED ||
				event.getEventType() == SchedulerManagerEventType.SCHEDULER_REMOVED ||
				event.getEventType() == SchedulerManagerEventType.SCHEDULER_SET	) {
			try {
				StatusBarManager.getInstance().setMessage(StatusBarManager.SCHEDULER_ID,executor.getCurrentSchedulerID()+" ("+executor.getCurrentSchedulingStrategyID()+ ") "+(executor.isRunning()?" running ":" stopped "));
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
		}
		

	}

}
