package de.uniol.inf.is.odysseus.rcp.editor.text.cql;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.editor.text.cql"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

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

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	
	public static IExecutor getExecutor(){
		return OdysseusRCPEditorTextPlugIn.getExecutor();
	}
	
	public static List<String> getDatatypeNames(){
		List<String> list = new ArrayList<>();
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		Set<SDFDatatype> dts = OdysseusRCPEditorTextPlugIn.getExecutor().getRegisteredDatatypes(caller);
		for(SDFDatatype dt : dts){
			list.add(dt.getQualName());
		}
		return list;
	}
	
}
