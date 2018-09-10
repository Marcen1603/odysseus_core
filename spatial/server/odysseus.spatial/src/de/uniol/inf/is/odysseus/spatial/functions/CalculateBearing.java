/*******************************************************************************
 * Copyright 2015 The Odysseus Team
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
 * @author Mazen Salous <mazen.salous@offis.de>
 */
public class CalculateBearing extends AbstractFunction<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1009222368204694026L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER },
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER },
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER },
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER } };

	public CalculateBearing() {
		super("CalculateBearing", 4, accTypes, SDFSpatialDatatype.DOUBLE);
	}

	@Override
	public Double getValue() {
		double sourceLat = this.getNumericalInputValue(0);
		double sourceLong = this.getNumericalInputValue(1);
		double targetLat = this.getNumericalInputValue(2);
		double targetLong = this.getNumericalInputValue(3);
		
		double radsourceLat = Math.toRadians(sourceLat);
		double radTargetLat = Math.toRadians(targetLat);
		double longDiff = Math.toRadians(targetLong - sourceLong);
		double y = Math.sin(longDiff) * Math.cos(radTargetLat);
		double x = Math.cos(radsourceLat) * Math.sin(radTargetLat)
				- Math.sin(radsourceLat) * Math.cos(radTargetLat)
				* Math.cos(longDiff);
		return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
	}

}
