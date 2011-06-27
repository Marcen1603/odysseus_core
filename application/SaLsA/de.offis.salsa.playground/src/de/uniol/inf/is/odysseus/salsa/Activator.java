package de.uniol.inf.is.odysseus.salsa;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.salsa.function.ExtractSegments;
import de.uniol.inf.is.odysseus.salsa.function.MoveViewPoint;
import de.uniol.inf.is.odysseus.salsa.function.RotateViewPoint;

public class Activator implements BundleActivator {
    private static Logger LOG = LoggerFactory.getLogger(BundleActivator.class);
    private static BundleContext context;

    /**
     * @return The Activator Context
     */
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
        try {
            MEP.registerFunction(new ExtractSegments());
            MEP.registerFunction(new MoveViewPoint());
            MEP.registerFunction(new RotateViewPoint());
        }
        catch (final Exception e) {
            Activator.LOG.error(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        Activator.context = null;
        try {
            MEP.unregisterFunction("ExtractSegments");
            MEP.unregisterFunction("MoveViewPoint");
            MEP.unregisterFunction("RotateViewPoint");
        }
        catch (final Exception e) {
            Activator.LOG.error(e.getMessage(), e);
        }
    }

}
