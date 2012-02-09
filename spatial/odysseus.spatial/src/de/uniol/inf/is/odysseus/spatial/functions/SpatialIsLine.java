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

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class SpatialIsLine extends AbstractFunction<Boolean>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3778075696713179631L;

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IFunction#getArity()
	 */
	@Override
	public int getArity() {
		// TODO Auto-generated method stub
		return 1;
	}

    public static final SDFDatatype[] accTypes = new SDFDatatype[]{
    	SDFSpatialDatatype.SPATIAL_POINT, 
    	SDFSpatialDatatype.SPATIAL_LINE_STRING, 
    	SDFSpatialDatatype.SPATIAL_POLYGON,
    	SDFSpatialDatatype.SPATIAL_MULTI_POINT,
    	SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING, 
    	SDFSpatialDatatype.SPATIAL_MULTI_POLYGON, 
    	SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION,
    	SDFSpatialDatatype.SPATIAL_GEOMETRY 
	};
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IFunction#getAcceptedTypes(int)
	 */
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 1){
			throw new IllegalArgumentException(getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		else{
			return accTypes;
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IFunction#getSymbol()
	 */
	@Override
	public String getSymbol() {
		return "SpatialIsLine";
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getValue()
	 */
	@Override
	public Boolean getValue() {
		return ((Geometry)this.getInputValue(0)).getGeometryType().equalsIgnoreCase("Linestring");
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getReturnType()
	 */
	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
	}

}
