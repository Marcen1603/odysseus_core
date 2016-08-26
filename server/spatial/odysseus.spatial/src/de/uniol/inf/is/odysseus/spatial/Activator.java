package de.uniol.inf.is.odysseus.spatial;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.access.transport.SpatioTemporalDataStructureTransportHandler;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		TransportHandlerRegistry.register(new SpatioTemporalDataStructureTransportHandler());
		//DataHandlerRegistry.registerDataHandler(new GraphDataHandler());
		//MovingObjectDataStructuresRegistry.register(NaiveSTDataStructure.class, NaiveSTDataStructure);
	
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}