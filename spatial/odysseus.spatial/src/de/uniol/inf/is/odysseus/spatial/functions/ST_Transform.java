/** Copyright [2011] [The Odysseus Team]
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

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Stephan Jansen <stephan.jansen@offis.de>
 */
public class ST_Transform extends AbstractFunction<Geometry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8850032331081355095L;

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IFunction#getArity()
	 */
	@Override
	public int getArity() {
		// TODO Auto-generated method stub
		return 2;
	}

    public static final SDFDatatype[][] accTypes = new SDFDatatype[][]{
    	{
	    	SDFSpatialDatatype.SPATIAL_POINT, 
	    	SDFSpatialDatatype.SPATIAL_LINE_STRING, 
	    	SDFSpatialDatatype.SPATIAL_POLYGON,
	    	SDFSpatialDatatype.SPATIAL_MULTI_POINT,
	    	SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING, 
	    	SDFSpatialDatatype.SPATIAL_MULTI_POLYGON, 
	    	SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION,
	    	SDFSpatialDatatype.SPATIAL_GEOMETRY 
		},
		{SDFDatatype.INTEGER}
    };
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IFunction#getAcceptedTypes(int)
	 */
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > this.getArity()){
			throw new IllegalArgumentException(getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		else{
			return accTypes[argPos];
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IFunction#getSymbol()
	 */
	@Override
	public String getSymbol() {
		return "ST_Transform";
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getValue()
	 */
	@Override
	public Geometry getValue() {
		Geometry sourceGeom = ((Geometry)this.getInputValue(0));
		Geometry targetGeom = sourceGeom.getFactory().createGeometry(sourceGeom);
		if (this.getInputValue(1) instanceof Double)
			targetGeom.setSRID(((Double)this.getInputValue(1)).intValue());
		else
			targetGeom.setSRID((Integer)this.getInputValue(1));
	    
	    CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
	    CRSFactory csFactory = new CRSFactory();
	    /*
	     * Create {@link CoordinateReferenceSystem} & CoordinateTransformation.
	     * Normally this would be carried out once and reused for all transformations
	     */ 
	    CoordinateReferenceSystem crs1 = csFactory.createFromName("EPSG:"+sourceGeom.getSRID());
	    CoordinateReferenceSystem crs2 = csFactory.createFromName("EPSG:" + targetGeom.getSRID());

	    CoordinateTransform trans = ctFactory.createTransform(crs1, crs2);

	    /*
	     * Create input and output points.
	     * These can be constructed once per thread and reused.
	     */ 
	    ProjCoordinate source = new ProjCoordinate();
	    ProjCoordinate target = new ProjCoordinate();
	    for (Coordinate sourceCoordinate: targetGeom.getCoordinates()) {
	    	source.x = sourceCoordinate.x;
	    	source.y = sourceCoordinate.y;
	    	source.z = sourceCoordinate.z;
		    
		    /*
		     * Transform point
		     */
		    trans.transform(source, target);
		    sourceCoordinate.x = target.x;
		    sourceCoordinate.y = target.y;
		    sourceCoordinate.z = target.z;
		}
	    targetGeom.geometryChanged();
	    
	    return targetGeom;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getReturnType()
	 */
	@Override
	public SDFDatatype getReturnType() {
		// TODO Auto-generated method stub
		return SDFSpatialDatatype.SPATIAL_GEOMETRY;
	}

}
