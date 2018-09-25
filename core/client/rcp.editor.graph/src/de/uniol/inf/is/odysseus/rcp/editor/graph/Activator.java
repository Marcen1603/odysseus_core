package de.uniol.inf.is.odysseus.rcp.editor.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.rcp.editor.graph"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	private List<SDFDatatype> cachedDatatypes;
	
	private List<String> cachedAggregateFunctions;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
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

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		ImageDescriptor i = imageDescriptorFromPlugin(PLUGIN_ID, path);
		if (i == null){
			throw new IllegalArgumentException("Image "+path+" not found");
		}
		return i;
	}
	
	
	public IExecutor getExecutor(){
		return OdysseusRCPPlugIn.getExecutor(); 
	}
	
	public ISession getCaller(){
		return OdysseusRCPPlugIn.getActiveSession();
	}
	
	public synchronized List<SDFDatatype> getInstalledDatatypes(){
		if(cachedDatatypes == null){
			cachedDatatypes = new ArrayList<SDFDatatype>(getExecutor().getRegisteredDatatypes(getCaller()));
			Collections.sort(cachedDatatypes, new Comparator<SDFDatatype>() {
				@Override
				public int compare(SDFDatatype o1, SDFDatatype o2) {
					return o1.getQualName().compareTo(o2.getQualName());					
				}
			});
		}
		return cachedDatatypes;
	}
	
	public synchronized List<String> getInstalledAggregateFunctions(@SuppressWarnings("rawtypes") Class<? extends IStreamObject> datamodel){
		if(cachedAggregateFunctions == null){
			cachedAggregateFunctions = new ArrayList<String>(getExecutor().getRegisteredAggregateFunctions(datamodel.getName(), getCaller()));
			Collections.sort(cachedAggregateFunctions);
		}
		return cachedAggregateFunctions;
	}
	
	public SDFDatatype resolveDatatype(String name){
		for(SDFDatatype dt : getInstalledDatatypes()){
			if(dt.getQualName().equalsIgnoreCase(name)){
				return dt;
			}
		}
		return null;
	}
}
