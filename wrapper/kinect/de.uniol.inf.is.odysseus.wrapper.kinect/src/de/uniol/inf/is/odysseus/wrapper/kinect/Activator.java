package de.uniol.inf.is.odysseus.wrapper.kinect;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Activator class for the bundle.
 * 
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class Activator implements BundleActivator {
    /**
     * Stores the bundle context.
     */
    private static BundleContext context;

    /**
     * Gets the context of the bundle.
     * 
     * @return bundle context.
     */
    public static BundleContext getContext() {
        return context;
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

}
