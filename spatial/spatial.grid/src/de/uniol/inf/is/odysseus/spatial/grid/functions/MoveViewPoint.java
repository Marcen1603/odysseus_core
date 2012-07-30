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

package de.uniol.inf.is.odysseus.spatial.grid.functions;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MoveViewPoint extends AbstractFunction<Geometry> {

	/**
     * 
     */
	private static final long serialVersionUID = -8906668468905044717L;
	private final GeometryFactory geometryFactory = new GeometryFactory();
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFSpatialDatatype.SPATIAL_GEOMETRY }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.DOUBLE } };

	@Override
	public int getArity() {
		return 3;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity()
					+ " argument(s): A geometry and a x and y value.");
		}
		return MoveViewPoint.accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "MoveViewPoint";
	}

	@Override
	public Geometry getValue() {
		final Geometry geometry = (Geometry) this.getInputValue(0);
		final Double x = (Double) this.getInputValue(1);
		final Double y = (Double) this.getInputValue(2);

		final Coordinate[] coordinates = new Coordinate[geometry
				.getCoordinates().length];
		for (int i = 0; i < coordinates.length; i++) {
			Coordinate coordinate = geometry.getCoordinates()[i];
			coordinates[i] = new Coordinate(coordinate.x - x, coordinate.y - y);
		}
		return this.geometryFactory.createMultiPoint(coordinates);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFSpatialDatatype.SPATIAL_GEOMETRY;
	}

}
