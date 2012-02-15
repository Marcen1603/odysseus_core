package de.uniol.inf.is.odysseus.database.spatial;

import java.sql.Types;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.database.connection.DatatypeRegistry;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_GEOMETRY, Types.OTHER);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION, Types.OTHER);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_POINT, Types.OTHER);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_LINE_STRING, Types.OTHER);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_POLYGON, Types.OTHER);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_MULTI_POINT, Types.OTHER);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING, Types.OTHER);
		DatatypeRegistry.getInstance().registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_MULTI_POLYGON, Types.OTHER);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_GEOMETRY);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_POINT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_LINE_STRING);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_POLYGON);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_MULTI_POINT);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING);
		DatatypeRegistry.getInstance().registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_MULTI_POLYGON);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
}
