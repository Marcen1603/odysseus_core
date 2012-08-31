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
package de.uniol.inf.is.odysseus.fusion.function.spatial.distance;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.fusion.util.Mahalanobis;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author abolles
 * 
 */
public class SpatialIsWithinMahalanobisDistance extends AbstractFunction<Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8795757250503029994L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IFunction#getArity()
	 */
	@Override
	public int getArity() {
		// TODO Auto-generated method stub
		return 3;
	}

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFSpatialDatatype.SPATIAL_POINT }, { SDFDatatype.DOUBLE } };

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.mep.IFunction#getAcceptedTypes(int)
	 */
	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos >= 3) {
			throw new IllegalArgumentException(getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		// 0 and 1 are equal
		switch (argPos) {
			case 0:
			case 1:
				return accTypes[0];
			case 2:
				return accTypes[1];
		}
		return null; // never reached
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IFunction#getSymbol()
	 */
	@Override
	public String getSymbol() {
		return "SpatialIsWithinMahalanobisDistance";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IExpression#getValue()
	 */
	@Override
	public Boolean getValue() {
		//Eliminate matches between same tuple.
		if(this.getInputValue(0).equals(this.getInputValue(1))){
			return false;
		}
		
		Point pointA = (Point)this.getInputValue(0);
		Point pointB = (Point)this.getInputValue(1);
		
		double[] dA = {pointA.getX() , pointA.getY()};
		double[] dB = {pointB.getX() , pointB.getY()};
		double distance = (Double)this.getInputValue(3);
		
		return (Mahalanobis.euclidean_2( dA, dB) < distance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.mep.IExpression#getReturnType()
	 */
	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
	}

}
