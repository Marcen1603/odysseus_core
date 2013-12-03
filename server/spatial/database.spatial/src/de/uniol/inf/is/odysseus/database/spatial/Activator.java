/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.database.spatial;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

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
//		DatatypeRegistry.registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_GEOMETRY, Types.OTHER);
//		DatatypeRegistry.registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION, Types.OTHER);
//		DatatypeRegistry.registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_POINT, Types.OTHER);
//		DatatypeRegistry.registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_LINE_STRING, Types.OTHER);
//		DatatypeRegistry.registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_POLYGON, Types.OTHER);
//		DatatypeRegistry.registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_MULTI_POINT, Types.OTHER);
//		DatatypeRegistry.registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING, Types.OTHER);
//		DatatypeRegistry.registerStreamToDatabase(SDFSpatialDatatype.SPATIAL_MULTI_POLYGON, Types.OTHER);
//		
//		DatatypeRegistry.registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_GEOMETRY);
//		DatatypeRegistry.registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION);
//		DatatypeRegistry.registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_POINT);
//		DatatypeRegistry.registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_LINE_STRING);
//		DatatypeRegistry.registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_POLYGON);
//		DatatypeRegistry.registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_MULTI_POINT);
//		DatatypeRegistry.registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING);
//		DatatypeRegistry.registerDatabaseToStream(Types.OTHER, SDFSpatialDatatype.SPATIAL_MULTI_POLYGON);
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
