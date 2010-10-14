package de.uniol.inf.is.odysseus.rcp.viewer.query.impl;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.viewer.query.QueryBuildConfigurationRegistry;
import de.uniol.inf.is.odysseus.rcp.viewer.query.QueryHistory;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.viewer.query";
	private static Activator plugin;
	private static IExecutor executor = null;

	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		QueryBuildConfigurationRegistry.getInstance().loadExtensionList();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
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
		StatusBarManager.getInstance().setMessage(StatusBarManager.SCHEDULER_ID,"Scheduler "+executor.getCurrentSchedulingStrategy()+ " stopped");
	}
	
	// Declarative Service
	public void unbindExecutor(IExecutor ex) {
		executor = null;
		StatusBarManager.getInstance().setMessage(StatusBarManager.EXECUTOR_ID, "No executor found");
	}

}
