package de.uniol.inf.is.odysseus.spatial;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.spatial.datastructures.FastGeoHashSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.FastQuadTreeSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.GeoHashSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.MovingObjectDataStructuresRegistry;
import de.uniol.inf.is.odysseus.spatial.datastructures.NaiveSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.QuadTreeSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.access.transport.SpatioTemporalDataStructureTransportHandler;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		TransportHandlerRegistry.register(new SpatioTemporalDataStructureTransportHandler());
		MovingObjectDataStructuresRegistry.register(NaiveSTDataStructure.class, NaiveSTDataStructure.TYPE);
		MovingObjectDataStructuresRegistry.register(QuadTreeSTDataStructure.class, QuadTreeSTDataStructure.TYPE);
		MovingObjectDataStructuresRegistry.register(FastQuadTreeSTDataStructure.class,
				FastQuadTreeSTDataStructure.TYPE);
		MovingObjectDataStructuresRegistry.register(GeoHashSTDataStructure.class, GeoHashSTDataStructure.TYPE);
		MovingObjectDataStructuresRegistry.register(FastGeoHashSTDataStructure.class, FastGeoHashSTDataStructure.TYPE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}