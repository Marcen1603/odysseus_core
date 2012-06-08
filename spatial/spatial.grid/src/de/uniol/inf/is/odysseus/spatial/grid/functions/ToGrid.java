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

package de.uniol.inf.is.odysseus.spatial.grid.functions;

import static com.googlecode.javacv.cpp.opencv_core.cvFillPoly;
import static com.googlecode.javacv.cpp.opencv_core.cvSet2D;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.grid.common.GridUtil;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ToGrid extends AbstractFunction<Grid> {
	/**
     * 
     */
	private static final long serialVersionUID = -8606524441544525424L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFSpatialDatatype.SPATIAL_GEOMETRY,
					SDFSpatialDatatype.SPATIAL_POLYGON },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.DOUBLE } };

	@Override
	public int getArity() {
		return 6;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(
					this.getSymbol()
							+ " has only "
							+ this.getArity()
							+ " argument(s): A geometry, the x and y coordinates, the width and height, and the cellsize.");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "ToGrid";
	}

	@Override
	public Grid getValue() {
		final Geometry geometry = (Geometry) this.getInputValue(0);
		final Coordinate origin = (Coordinate) this.getInputValue(1);
		Integer width = this.getNumericalInputValue(2).intValue();
		Integer height = this.getNumericalInputValue(3).intValue();
		final Double cellsize = this.getNumericalInputValue(4);

		final Coordinate[] coordinates = geometry.getCoordinates();

		final Grid grid = new Grid(origin, width, height,
				cellsize);

		grid.fill(GridUtil.UNKNOWN);

		IplImage image = OpenCVUtil.gridToImage(grid);
		CvPoint convexHullPoints = new CvPoint(coordinates.length);
		Coordinate coordinate;
		for (int i = 0; i < coordinates.length; i++) {
			coordinate = coordinates[i];
			convexHullPoints.position(i)
					.x((int) ((coordinate.x - origin.x) / cellsize + 0.5))
					.y((int) ((coordinate.y - origin.y) / cellsize + 0.5));
		}
		cvFillPoly(image, convexHullPoints, new int[] { coordinates.length },
				1, opencv_core.cvScalarAll(GridUtil.FREE), 4, 0);
		convexHullPoints.deallocate();
		for (int i = 0; i < coordinates.length; i++) {
			coordinate = coordinates[i];
			if ((coordinate.x >= origin.x)
					&& (coordinate.x < origin.x + width * grid.cellsize)
					&& (coordinate.y >= origin.y)
					&& (coordinate.y < origin.y + height * grid.cellsize)) {

				cvSet2D(image, (int) ((coordinate.y - origin.y) / cellsize),
						(int) ((coordinate.x - origin.x) / cellsize),
						opencv_core.cvScalarAll(GridUtil.OBSTACLE));
			}
		}
		return OpenCVUtil.imageToGrid(image, grid);
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}
}
