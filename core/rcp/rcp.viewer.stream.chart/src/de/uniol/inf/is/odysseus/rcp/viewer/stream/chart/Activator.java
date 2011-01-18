package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.DoubleDatatypeProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.IntegerDatatypeProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.LongDatatypeProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.PointInTimeDatatypeProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype.ViewableDatatypeRegistry;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.viewer.stream.diagram"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	private static BundleContext bundleContext;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		bundleContext= context;
		
		// register all allowed datatypes and convertes
		ViewableDatatypeRegistry.getInstance().register(new LongDatatypeProvider());
		ViewableDatatypeRegistry.getInstance().register(new IntegerDatatypeProvider());
		ViewableDatatypeRegistry.getInstance().register(new DoubleDatatypeProvider());
		ViewableDatatypeRegistry.getInstance().register(new PointInTimeDatatypeProvider());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		bundleContext = null;
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

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

}
