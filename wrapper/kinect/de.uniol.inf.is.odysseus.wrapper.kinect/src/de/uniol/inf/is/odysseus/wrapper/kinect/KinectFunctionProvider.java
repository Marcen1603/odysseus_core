package de.uniol.inf.is.odysseus.wrapper.kinect;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.wrapper.kinect.functions.AffineTransform;
import de.uniol.inf.is.odysseus.wrapper.kinect.functions.ToPointCloud;

/**
 * The function provider is used by the OSGI to get functions provided by this bundle.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class KinectFunctionProvider implements IFunctionProvider {
    /** Registers and stores the logger for this class. */
    private static final Logger LOG = LoggerFactory
            .getLogger(KinectFunctionProvider.class);

    /** Standard constructor. */
    public KinectFunctionProvider() {
    }

    @Override
    public List<IFunction<?>> getFunctions() {
        final List<IFunction<?>> functions = new ArrayList<IFunction<?>>();

        functions.add(new ToPointCloud());
        functions.add(new AffineTransform());
        KinectFunctionProvider.LOG.info(String.format("Register functions: %s",
                functions));
        return functions;
    }

}
