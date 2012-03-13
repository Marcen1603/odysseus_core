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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Stephan Jansen <stephan.jansen@offis.de>
 * 
 *         Function to return geometries that are subtypes of Geometry as SpatialGeometry
 */
public class AsGeometry extends AbstractFunction<Geometry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8850032331081355095L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IFunction#getArity()
	 */
	@Override
	public int getArity() {
		// TODO Auto-generated method stub
		return 1;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[] { 
		SDFSpatialDatatype.SPATIAL_GEOMETRY, SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION,
		SDFSpatialDatatype.SPATIAL_POINT, SDFSpatialDatatype.SPATIAL_MULTI_POINT,
		SDFSpatialDatatype.SPATIAL_LINE_STRING, SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING,
		SDFSpatialDatatype.SPATIAL_POLYGON, SDFSpatialDatatype.SPATIAL_MULTI_POLYGON
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IFunction#getAcceptedTypes(int)
	 */
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
        return accTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IFunction#getSymbol()
	 */
	@Override
	public String getSymbol() {
		return "AsGeometry";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IExpression#getValue()
	 */
	@Override
	public Geometry getValue() {
		Geometry g = (Geometry) this.getInputValue(0);
		return (Geometry) g.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IExpression#getReturnType()
	 */
	@Override
	public SDFDatatype getReturnType() {
		// TODO Auto-generated method stub
		return SDFSpatialDatatype.SPATIAL_GEOMETRY;
	}

}
