package de.uniol.inf.is.odysseus.test.component.jira;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

    // The shared instance
    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    /**
     * The constructor
     */
    public Activator() {
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void start(BundleContext context) throws Exception {
        Activator.context = context;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        Activator.context = null;
    }

}
