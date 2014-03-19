/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Stephan Jansen <stephan.jansen@offis.de>
 * 
 *         Function to return geometries that are GeometryCollections as SpatialGeometryCollection
 */
public class AsGeometryCollection extends AbstractFunction<Geometry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8850032331081355095L;

	public AsGeometryCollection() {
		super("AsGeometryCollection",1,accTypes,SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION);
	}
	

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][]{ { 
		SDFSpatialDatatype.SPATIAL_GEOMETRY, SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION }};


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getValue()
	 */
	@Override
	public GeometryCollection getValue() {
		Geometry g = (Geometry) this.getInputValue(0);
		if (g instanceof GeometryCollection)
			return (GeometryCollection) g.clone();
        return g.getFactory().createGeometryCollection(null);
	}

}
