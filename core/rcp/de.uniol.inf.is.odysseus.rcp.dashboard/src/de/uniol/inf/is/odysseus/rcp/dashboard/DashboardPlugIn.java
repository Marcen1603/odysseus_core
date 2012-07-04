package de.uniol.inf.is.odysseus.rcp.dashboard;

import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser;

/**
 * The activator class controls the plug-in life cycle
 */
public class DashboardPlugIn extends AbstractUIPlugin {

	private static final Logger LOG = LoggerFactory.getLogger(DashboardPlugIn.class);
	
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.dashboard";
	public static final String EXTENSION_POINT_ID = "de.uniol.inf.is.odysseus.rcp.DashboardPart";
	public static final String DASHBOARD_PART_EXTENSION = "prt";
	
	private static DashboardPlugIn plugin;
	
	private static IOdysseusScriptParser scriptParser;
	private static IExecutor executor;
	
	/**
	 * The constructor
	 */
	public DashboardPlugIn() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		DashboardPartExtensionPointResolver.execute(Platform.getExtensionRegistry());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		
		DashboardPartRegistry.unregisterAll();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static DashboardPlugIn getDefault() {
		return plugin;
	}
	
	public void bindScriptParser( IOdysseusScriptParser parser ) {
		LOG.debug("ScriptParser {} bound." , parser);
		
		scriptParser = parser;
	}
	
	public void unbindScriptParser( IOdysseusScriptParser parser ) {
		if( parser == scriptParser ) {
			LOG.debug("ScriptParser {} unbound.", parser);
			
			scriptParser = null;
		}
	}
	
	public static IOdysseusScriptParser getScriptParser() {
		return scriptParser;
	}
	
	public void bindExecutor( IExecutor exec ) {
		executor = exec;
		
		LOG.debug("Executor {} bound." , exec);
	}
	
	public void unbindExecutor( IExecutor exec) {
		if( exec == executor ) {
			executor = null;
			
			LOG.debug("Executor {} unbound." , exec);
		}
	}
	
	public static IExecutor getExecutor() {
		return executor;
	}
	
}
