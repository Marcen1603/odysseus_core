
package de.uniol.inf.is.odysseus.spatial;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.FastGeoHashSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.FastQuadTreeSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.GeoHashSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.NaiveSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.QuadTreeSTDataStructure;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.SpatioTemporalDataStructuresRegistry;

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
		
		// Spatio temporal data structures
		SpatioTemporalDataStructuresRegistry.register(NaiveSTDataStructure.class, NaiveSTDataStructure.TYPE);
		SpatioTemporalDataStructuresRegistry.register(QuadTreeSTDataStructure.class, QuadTreeSTDataStructure.TYPE);
		SpatioTemporalDataStructuresRegistry.register(FastQuadTreeSTDataStructure.class,
				FastQuadTreeSTDataStructure.TYPE);
		SpatioTemporalDataStructuresRegistry.register(GeoHashSTDataStructure.class, GeoHashSTDataStructure.TYPE);
		SpatioTemporalDataStructuresRegistry.register(FastGeoHashSTDataStructure.class,
				FastGeoHashSTDataStructure.TYPE);

		// Moving object data structures
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