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
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.geom.PolarCoordinate;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class AsCartesianCoordinates extends AbstractFunction<Geometry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1802751868075398723L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFSpatialDatatype.SPATIAL_POLAR_COORDINATE } };
	private final GeometryFactory geometryFactory = new GeometryFactory();

	public AsCartesianCoordinates() {
		super("AsCartesianCoordinates",1,accTypes,SDFSpatialDatatype.SPATIAL_GEOMETRY);
	}

	@Override
	public Geometry getValue() {
		@SuppressWarnings("unchecked")
		List<PolarCoordinate> coordinates = (List<PolarCoordinate>) this
				.getInputValue(0);

		final List<Coordinate> points = new ArrayList<Coordinate>();
		Coordinate lastPoint = null;
		for (int i = 0; i < coordinates.size(); i++) {
			PolarCoordinate coordinate = coordinates.get(i);
			final Coordinate point = new Coordinate();
			point.x = coordinate.r * Math.cos(coordinate.a);
			point.y = coordinate.r * Math.sin(coordinate.a);
			if ((lastPoint == null) || (!point.equals2D(lastPoint))) {
				points.add(point);
				lastPoint = point;
			}

		}
		return this.geometryFactory.createMultiPoint(points
				.toArray(new Coordinate[points.size()]));
	}

}
