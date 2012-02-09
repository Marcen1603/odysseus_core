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
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Stephan Jansen <stephan.jansen@offis.de>
 */
public class FromWKT extends AbstractFunction<Geometry> {

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
		return 1;
	}

    public static final SDFDatatype[] accTypes = new SDFDatatype[]{
    	SDFDatatype.STRING
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
			return accTypes;
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IFunction#getSymbol()
	 */
	@Override
	public String getSymbol() {
		return "FromWKT";
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.mep.IExpression#getValue()
	 */
	@Override
	public Geometry getValue() {
		Geometry g;
		try {
			g = new WKTReader().read((String)this.getInputValue(0));
		} catch (ParseException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		return g;
	
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
