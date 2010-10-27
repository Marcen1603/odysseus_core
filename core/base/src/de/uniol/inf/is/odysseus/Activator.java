package de.uniol.inf.is.odysseus;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.Distance;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.DolToEur;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.Now;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.function.Polygon;

public class Activator implements BundleActivator {

    private static BundleContext bundleContext;
    
    private IFunction[] functions = new IFunction[]{new DolToEur(), new Now(), new Distance(), new Polygon()};

	/*
     * (non-Javadoc)
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
	public void start(BundleContext context) throws Exception {
        // Add default Functions
    	bundleContext = context;
    	for(IFunction function : functions) {
    		MEP.registerFunction(function);
    	}
    }
    
    public static BundleContext getBundleContext() {
		return bundleContext;
	}

    /*
     * (non-Javadoc)
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
	public void stop(BundleContext context) throws Exception {
    	for(IFunction function : functions) {
    		MEP.unregisterFunction(function.getSymbol());
    	}
    }

}
