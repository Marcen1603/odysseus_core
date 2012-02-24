package de.uniol.inf.is.odysseus.wrapper.base;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;

public class Activator implements BundleActivator {

    private static BundleContext context;

    private static IDataDictionary dd;
    
    static BundleContext getContext() {
        return Activator.context;
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }
    
    public void bindDataDictionary(IDataDictionary dd){
    	Activator.dd = dd;
    }

    public void unbindDataDictionary(IDataDictionary dd){
    	Activator.dd = null;
    }

    public static IDataDictionary getDataDictionary() {
		return dd;
	}
    
}
