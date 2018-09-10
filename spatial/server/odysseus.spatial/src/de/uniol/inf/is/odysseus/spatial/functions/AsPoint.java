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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Stephan Jansen <stephan.jansen@offis.de>
 * 
 *         Function to return geometries that are Points as SpatialPoint
 */
public class AsPoint extends AbstractFunction<Geometry> {

	private static final long serialVersionUID = -8850032331081355095L;

	public AsPoint() {
		super("AsPoint",1,accTypes,SDFSpatialDatatype.SPATIAL_POINT);
	}
	
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][]{ { SDFSpatialDatatype.SPATIAL_GEOMETRY, SDFSpatialDatatype.SPATIAL_POINT }};

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getValue()
	 */
	@Override
	public Point getValue() {
		GeometryWrapper g = (GeometryWrapper) this.getInputValue(0);
		if (g.getGeometry() instanceof Point)
			return (Point) g.getGeometry().clone();
        return g.getGeometry().getFactory().createPoint(new Coordinate(null));
	}

}
