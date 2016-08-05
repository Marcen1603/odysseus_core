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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.GeometryWrapper;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Stephan Jansen <stephan.jansen@offis.de>
 */
public class ST_SetSRID extends AbstractFunction<GeometryWrapper> {

	private static final long serialVersionUID = -8850032331081355095L;

	public ST_SetSRID() {
		super("ST_SetSRID", 2, accTypes, SDFSpatialDatatype.SPATIAL_GEOMETRY);
	}

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFSpatialDatatype.SPATIAL_POINT, SDFSpatialDatatype.SPATIAL_LINE_STRING,
					SDFSpatialDatatype.SPATIAL_POLYGON, SDFSpatialDatatype.SPATIAL_MULTI_POINT,
					SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING, SDFSpatialDatatype.SPATIAL_MULTI_POLYGON,
					SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION, SDFSpatialDatatype.SPATIAL_GEOMETRY },
			{ SDFDatatype.INTEGER } };

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getValue()
	 */
	@Override
	public GeometryWrapper getValue() {
		Geometry sourceGeom = null;
		if (this.getInputValue(0) instanceof Geometry) {
			sourceGeom = (Geometry) this.getInputValue(0);			
		} else if (this.getInputValue(0) instanceof GeometryWrapper) {
			sourceGeom = ((GeometryWrapper) this.getInputValue(0)).getGeometry();
		}
		Geometry targetGeom = (Geometry) sourceGeom.clone();
		if (this.getInputValue(1) instanceof Double)
			targetGeom.setSRID(((Double) this.getInputValue(1)).intValue());
		else if (this.getInputValue(1) instanceof Integer) {
			targetGeom.setSRID((Integer) this.getInputValue(1));			
		} else {
			long value = this.getInputValue(1);
			int intValue = Math.toIntExact(value);
			targetGeom.setSRID(intValue);
		}
		
		// Use the wrapper to implement the Cloneable-interface
		return new GeometryWrapper(targetGeom);
	}

}
