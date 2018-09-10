/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.spatial.functions;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

public class GetCoordinate extends AbstractFunction<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5374431167970509952L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFSpatialDatatype.SPATIAL_COORDINATE },
			{ SDFDatatype.INTEGER}};

	public GetCoordinate() {
		super("GetCoordinate", 2, accTypes, SDFSpatialDatatype.DOUBLE);
	}

	@Override
	public Double getValue() {
		Coordinate coordinate = (Coordinate) this.getInputValue(0);
		int index = this.getNumericalInputValue(1).intValue();
		switch (index) {
		case 0:
			return coordinate.x;
		case 1:	
			return coordinate.y;
		case 2:
			return coordinate.z; 
		default:
			return Double.NaN;
		}
	}

}
