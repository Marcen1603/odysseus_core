package de.uniol.inf.is.odysseus.spatial;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.GeoHashMODataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.movingobject.MovingObjectDataStructureRegistry;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.FastGeoHashSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.FastQuadTreeSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.GeoHashSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.NaiveSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.QuadTreeSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.SpatioTemporalDataStructuresRegistry;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.access.transport.SpatioTemporalDataStructureTransportHandler;

@Deprecated
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

		// Spatio temporal data structures
		SpatioTemporalDataStructuresRegistry.register(NaiveSTDataStructure.class, NaiveSTDataStructure.TYPE);
		SpatioTemporalDataStructuresRegistry.register(QuadTreeSTDataStructure.class, QuadTreeSTDataStructure.TYPE);
		SpatioTemporalDataStructuresRegistry.register(FastQuadTreeSTDataStructure.class,
				FastQuadTreeSTDataStructure.TYPE);
		SpatioTemporalDataStructuresRegistry.register(GeoHashSTDataStructure.class, GeoHashSTDataStructure.TYPE);
		SpatioTemporalDataStructuresRegistry.register(FastGeoHashSTDataStructure.class,
				FastGeoHashSTDataStructure.TYPE);

		// Moving object data structures
		MovingObjectDataStructureRegistry.register(GeoHashMODataStructure.class, GeoHashMODataStructure.TYPE);
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