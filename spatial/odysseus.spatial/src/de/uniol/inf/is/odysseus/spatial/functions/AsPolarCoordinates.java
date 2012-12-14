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

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class AsPolarCoordinates extends AbstractFunction<PolarCoordinate[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8192680754462139112L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFSpatialDatatype.SPATIAL_GEOMETRY } };

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s): A geometry.");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "AsPolarCoordinates";
	}

	@Override
	public PolarCoordinate[] getValue() {
		Geometry geometry = (Geometry) this.getInputValue(0);
		List<PolarCoordinate> polarCoordinates = new ArrayList<PolarCoordinate>();
		for (Coordinate coordinate : geometry.getCoordinates()) {
			double radius = Math.sqrt(Math.pow(coordinate.x, 2)
					+ Math.pow(coordinate.y, 2));
			double angle = Math.toDegrees(Math
					.atan2(coordinate.y, coordinate.x));
			if (angle < 0.0) {
				angle += 360;
			}
			if (angle >= 360.0) {
				angle -= ((int) (angle / 360.0)) * 360;
			}
			polarCoordinates.add(new PolarCoordinate(radius, angle));
		}
		return null;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFSpatialDatatype.SPATIAL_POLAR_COORDINATE;
	}

}
