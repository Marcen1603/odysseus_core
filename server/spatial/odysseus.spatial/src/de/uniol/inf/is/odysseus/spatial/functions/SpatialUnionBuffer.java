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
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author kpancratz
 *
 */
public class SpatialUnionBuffer extends AbstractFunction<Geometry> {

	private static final long serialVersionUID = -1290987836780172890L;

	public SpatialUnionBuffer() {
		super("SpatialUnionBuffer",3,accTypes,SDFSpatialDatatype.SPATIAL_GEOMETRY);
	}
	
    public static final SDFDatatype[] accTypes1 = new SDFDatatype[]{
    	SDFSpatialDatatype.SPATIAL_POINT, 
    	SDFSpatialDatatype.SPATIAL_LINE_STRING, 
    	SDFSpatialDatatype.SPATIAL_POLYGON,
    	SDFSpatialDatatype.SPATIAL_MULTI_POINT,
    	SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING, 
    	SDFSpatialDatatype.SPATIAL_MULTI_POLYGON, 
    	SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION,
    	SDFSpatialDatatype.SPATIAL_GEOMETRY 
	};
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][]{accTypes1,accTypes1,accTypes1};

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getValue()
	 */
	@Override
	public Geometry getValue() {
		
		Geometry[] geometrys = new Geometry[2]; 
			
		geometrys[0] = (Geometry)this.getInputValue(0);
		geometrys[1] = (Geometry)this.getInputValue(1);

		GeometryFactory geometryFactory = new GeometryFactory();
		
		GeometryCollection polygonCollection = geometryFactory.createGeometryCollection(geometrys); 		
		
		
		return polygonCollection.buffer((Double)this.getInputValue(2));
	}

}
