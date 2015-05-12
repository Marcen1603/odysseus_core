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

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.is.odysseus.spatial.utilities.Ellipsoid;
import de.uniol.inf.is.odysseus.spatial.utilities.GeodeticCalculator;
import de.uniol.inf.is.odysseus.spatial.utilities.GlobalCoordinates;

/**
 * @author Mazen Salous <mazen.salous@offis.de>
 */
public class CalculateEndingCoordinates extends AbstractFunction<Coordinate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1009222368204694026L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER },
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER },
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER },
			{ SDFDatatype.DOUBLE, SDFDatatype.FLOAT, SDFDatatype.INTEGER } };

	public CalculateEndingCoordinates() {
		super("CalculateEndingCoordinates", 4, accTypes, SDFSpatialDatatype.DOUBLE);
	}

	@Override
	public Coordinate getValue() {
		double startLatitude = this.getNumericalInputValue(0);
		double startLongitude = this.getNumericalInputValue(1);
		double startBearing = this.getNumericalInputValue(2);
		double distance = this.getNumericalInputValue(3);
		GeodeticCalculator calculator = new GeodeticCalculator();
		GlobalCoordinates res = calculator.calculateEndingGlobalCoordinates(Ellipsoid.WGS84, new GlobalCoordinates(startLatitude, startLongitude),startBearing, distance);
		return new Coordinate(res.getLatitude(), res.getLongitude());
	}

}
