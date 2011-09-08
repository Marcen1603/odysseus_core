package de.uniol.inf.is.odysseus.salsa;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.salsa.function.BoundingBox;
import de.uniol.inf.is.odysseus.salsa.function.ClearBooleanGrid;
import de.uniol.inf.is.odysseus.salsa.function.ClearByteGrid;
import de.uniol.inf.is.odysseus.salsa.function.ClearDoubleGrid;
import de.uniol.inf.is.odysseus.salsa.function.ClearFloatGrid;
import de.uniol.inf.is.odysseus.salsa.function.ExtractSegments;
import de.uniol.inf.is.odysseus.salsa.function.IEPF;
import de.uniol.inf.is.odysseus.salsa.function.InverseBooleanGrid;
import de.uniol.inf.is.odysseus.salsa.function.InverseByteGrid;
import de.uniol.inf.is.odysseus.salsa.function.InverseDoubleGrid;
import de.uniol.inf.is.odysseus.salsa.function.InverseFloatGrid;
import de.uniol.inf.is.odysseus.salsa.function.IsGridFree;
import de.uniol.inf.is.odysseus.salsa.function.IsPedestrian;
import de.uniol.inf.is.odysseus.salsa.function.MergeGeometries;
import de.uniol.inf.is.odysseus.salsa.function.MoveViewPoint;
import de.uniol.inf.is.odysseus.salsa.function.ObjectSize;
import de.uniol.inf.is.odysseus.salsa.function.RotateViewPoint;
import de.uniol.inf.is.odysseus.salsa.function.ToBooleanGrid;
import de.uniol.inf.is.odysseus.salsa.function.ToByteGrid;
import de.uniol.inf.is.odysseus.salsa.function.ToDoubleGrid;
import de.uniol.inf.is.odysseus.salsa.function.ToFloatGrid;

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
            MEP.registerFunction(new ToDoubleGrid());
            MEP.registerFunction(new ToFloatGrid());
            MEP.registerFunction(new ToByteGrid());
            MEP.registerFunction(new ToBooleanGrid());
            MEP.registerFunction(new InverseDoubleGrid());
            MEP.registerFunction(new InverseFloatGrid());
            MEP.registerFunction(new InverseByteGrid());
            MEP.registerFunction(new InverseBooleanGrid());
            MEP.registerFunction(new IEPF());
            MEP.registerFunction(new IsGridFree());
            MEP.registerFunction(new ClearDoubleGrid());
            MEP.registerFunction(new ClearFloatGrid());
            MEP.registerFunction(new ClearByteGrid());
            MEP.registerFunction(new ClearBooleanGrid());
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
            MEP.unregisterFunction("ToDoubleGrid");
            MEP.unregisterFunction("ToFloatGrid");
            MEP.unregisterFunction("ToByteGrid");
            MEP.unregisterFunction("ToBooleanGrid");
            MEP.unregisterFunction("InverseDoubleGrid");
            MEP.unregisterFunction("InverseFloatGrid");
            MEP.unregisterFunction("InverseByteGrid");
            MEP.unregisterFunction("InverseBooleanGrid");
            MEP.unregisterFunction("IEPF");
            MEP.unregisterFunction("IsGridFree");
            MEP.unregisterFunction("ClearDoubleGrid");
            MEP.unregisterFunction("ClearDoubleGrid");
            MEP.unregisterFunction("ClearDoubleGrid");
            MEP.unregisterFunction("ClearDoubleGrid");
        }
        catch (final Exception e) {
            Activator.LOG.error(e.getMessage(), e);
        }
    }

}
