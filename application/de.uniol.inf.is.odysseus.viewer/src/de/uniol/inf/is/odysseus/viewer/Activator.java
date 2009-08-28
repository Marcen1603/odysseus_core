package de.uniol.inf.is.odysseus.viewer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class Activator implements BundleActivator {

	static BundleContext context;
	public static final String XSD_RESOURCES_FILE = "viewer_cfg/resourcesSchema.xsd";
	public static final String XSD_DIAGRAMM_SCHEMA_FILE = "viewer_cfg/diagramSchema.xsd";
	public static final String XSD_SYMBOL_SCHEMA_FILE = "viewer_cfg/symbolSchema.xsd";
	
	@Override
	public void start(BundleContext bc) throws Exception {
		context = bc;
		
	}

	@Override
	public void stop(BundleContext arg0) throws Exception {
		context = null;
	}
	
	public static BundleContext getContext() {
		return context;
	}

}
