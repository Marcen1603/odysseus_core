package de.uniol.inf.is.odysseus.salsa;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.salsa.function.BoundingBox;
import de.uniol.inf.is.odysseus.salsa.function.ExtractSegments;
import de.uniol.inf.is.odysseus.salsa.function.IsPedestrian;
import de.uniol.inf.is.odysseus.salsa.function.MergeGeometries;
import de.uniol.inf.is.odysseus.salsa.function.MergeGrid;
import de.uniol.inf.is.odysseus.salsa.function.MoveViewPoint;
import de.uniol.inf.is.odysseus.salsa.function.ObjectSize;
import de.uniol.inf.is.odysseus.salsa.function.RotateViewPoint;
import de.uniol.inf.is.odysseus.salsa.function.ToGrid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
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
            MEP.registerFunction(new IsPedestrian());
            MEP.registerFunction(new MergeGeometries());
            MEP.registerFunction(new BoundingBox());
            MEP.registerFunction(new ObjectSize());
            MEP.registerFunction(new ToGrid());
            MEP.registerFunction(new MergeGrid());
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
            MEP.unregisterFunction("IsPedestrian");
            MEP.unregisterFunction("MergeGeometries");
            MEP.unregisterFunction("BoundingBox");
            MEP.unregisterFunction("ObjectSize");
            MEP.unregisterFunction("ToGrid");
            MEP.unregisterFunction("MergeGrid");
        }
        catch (final Exception e) {
            Activator.LOG.error(e.getMessage(), e);
        }
    }

}
