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
package de.uniol.inf.is.odysseus.spatial;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Marco Grawunder
 */
public class SpatialDatatypeProvider implements IDatatypeProvider {

	@Override
	public List<SDFDatatype> getDatatypes() {
		List<SDFDatatype> ret = new ArrayList<>();
		ret.add(SDFSpatialDatatype.SPATIAL_COORDINATE);
		ret.add(SDFSpatialDatatype.SPATIAL_COORDINATE_SEQUENCE);
		ret.add(SDFSpatialDatatype.SPATIAL_POINT);
		ret.add(SDFSpatialDatatype.SPATIAL_LINE_STRING);
		ret.add(SDFSpatialDatatype.SPATIAL_LINEAR_RING);
		ret.add(SDFSpatialDatatype.SPATIAL_LINEAR_RING_ARRAY);
		ret.add(SDFSpatialDatatype.SPATIAL_POLYGON);
		ret.add(SDFSpatialDatatype.SPATIAL_MULTI_POINT);
		ret.add(SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING);
		ret.add(SDFSpatialDatatype.SPATIAL_MULTI_POLYGON);
		ret.add(SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION);
		ret.add(SDFSpatialDatatype.SPATIAL_GEOMETRY);
		return ret;
	}

}
