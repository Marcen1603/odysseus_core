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

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author kpancratz
 *
 */
public class SpatialUnionBuffer extends AbstractFunction {

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IFunction#getArity()
	 */
	@Override
	public int getArity() {
		// TODO Auto-generated method stub
		return 3;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IFunction#getAcceptedTypes(int)
	 */
	@Override
	public Class[] getAcceptedTypes(int argPos) {
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > this.getArity()){
			throw new IllegalArgumentException(getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		else{
			Class<?>[] accTypes = new Class<?>[2];
			accTypes[0] = Geometry.class;
			accTypes[1] = Double.class;
			return accTypes;
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IFunction#getSymbol()
	 */
	@Override
	public String getSymbol() {
		return "SpatialUnionBuffer";
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getValue()
	 */
	@Override
	public Object getValue() {
		
		Geometry[] geometrys = new Geometry[2]; 
			
		geometrys[0] = (Geometry)this.getInputValue(0);
		geometrys[1] = (Geometry)this.getInputValue(1);

		GeometryFactory geometryFactory = new GeometryFactory();
		
		GeometryCollection polygonCollection = geometryFactory.createGeometryCollection(geometrys); 		
		
		
		return polygonCollection.buffer((Double)this.getInputValue(2));
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getReturnType()
	 */
	@Override
	public Class getReturnType() {
		// TODO Auto-generated method stub
		return Geometry.class;
	}

}
