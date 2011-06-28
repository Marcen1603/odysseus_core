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

/**
 * @author kpancratz
 *
 */
public class SpatialBuffer extends AbstractFunction {

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IFunction#getArity()
	 */
	@Override
	public int getArity() {
		// TODO Auto-generated method stub
		return 2;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IFunction#getAcceptedTypes(int)
	 */
	@Override
	public String[] getAcceptedTypes(int argPos) {
		if(argPos <= 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > this.getArity()){
			throw new IllegalArgumentException(getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		else{
			String[] accTypes = null;
			switch(argPos){
			case 1: 
				accTypes = new String[7];
				accTypes[0] = "SpatialPoint";
				accTypes[1] = "SpatialMultiPoint";
				accTypes[2] = "SpatialLine";
				accTypes[3] = "SpatialMultiLine";
				accTypes[4] = "SpatialPolygon";
				accTypes[5] = "SpatialMultiPolygon";
				accTypes[6] = "Spatial";
				break;
			case 2:
				accTypes = new String[1];
				accTypes[0] = "Double";
			}
			
			return accTypes;
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IFunction#getSymbol()
	 */
	@Override
	public String getSymbol() {
		return "SpatialBuffer";
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getValue()
	 */
	@Override
	public Object getValue() {
		return ((Geometry)this.getInputValue(0)).buffer((Double)this.getInputValue(1));
	
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getReturnType()
	 */
	@Override
	public String getReturnType() {
		// TODO Auto-generated method stub
		return "Spatial";
	}

}
