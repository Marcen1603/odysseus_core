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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * Calculates the distance based on the haversine formula.
 * 
 * @author Tobias Brandt
 *
 */
public class CalculateDistance extends AbstractFunction<Double> {

	private static final long serialVersionUID = 5262980729413311930L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER },
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER },
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER },
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER } };

	public CalculateDistance() {
		super("CalculateDistance", 4, accTypes, SDFSpatialDatatype.DOUBLE);
	}

	@Override
	public Double getValue() {
		double sourceLat = this.getNumericalInputValue(0);
		double sourceLong = this.getNumericalInputValue(1);
		double targetLat = this.getNumericalInputValue(2);
		double targetLong = this.getNumericalInputValue(3);
		// This calculation is based on HEVERSINE formula
		double earthRadius = 6371; // earthRadius in Kilometers
		double dLat = Math.toRadians(targetLat - sourceLat);
		double dLng = Math.toRadians(targetLong - sourceLong);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(sourceLat))
				* Math.cos(Math.toRadians(targetLat)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = earthRadius * c;
		return distance;
	}

}
