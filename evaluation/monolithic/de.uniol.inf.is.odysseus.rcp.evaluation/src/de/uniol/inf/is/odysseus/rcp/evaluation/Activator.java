package de.uniol.inf.is.odysseus.rcp.evaluation;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.evaluation"; //$NON-NLS-1$

	private static Activator plugin;
	private static IExecutor executor;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

	public void bindExecutor(IExecutor e){
		executor = e;
	}
	
	public void unbindExecutor(IExecutor e){
		if( executor == e ) {
			executor = null;
		}
	}

	public static IExecutor getExecutor() {
		return executor;
	}
	
}
