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

import static com.googlecode.javacv.cpp.opencv_core.CV_FILLED;
import static com.googlecode.javacv.cpp.opencv_core.cvFillPoly;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_core.cvZero;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.CartesianGrid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ToGrid extends AbstractFunction<CartesianGrid> {
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
	public CartesianGrid getValue() {
		final Geometry geometry = (Geometry) this.getInputValue(0);
		final Double x = this.getNumericalInputValue(1);
		final Double y = this.getNumericalInputValue(2);
		Integer width = this.getNumericalInputValue(3).intValue();
		Integer depth = this.getNumericalInputValue(4).intValue();
		final Double cellsize = this.getNumericalInputValue(5);
		final Coordinate[] coordinates = geometry.getCoordinates();

		final CartesianGrid grid = new CartesianGrid(new Coordinate(x, y),
				width, depth, cellsize);
		IplImage image = IplImage.create(
				opencv_core.cvSize(grid.width, grid.height),
				opencv_core.IPL_DEPTH_64F, 1);

		cvZero(image);
		opencv_core.cvSet(image, OpenCVUtil.UNKNOWN);

		CvPoint convexHullPoints = new CvPoint(coordinates.length);
		Coordinate coordinate;
		for (int i = 0; i < coordinates.length; i++) {
			coordinate = coordinates[i];
			convexHullPoints
					.position(i)
					.x((int) ((coordinate.x - x) / cellsize + 0.5))
					.y(image.height()
							- (int) ((coordinate.y - y) / cellsize + 0.5));
		}
		cvFillPoly(image, convexHullPoints, new int[] { coordinates.length },
				1, OpenCVUtil.FREE, 4, 0);
		convexHullPoints.deallocate();
		CvPoint point = new CvPoint(0, 0);
		for (int i = 0; i < coordinates.length; i++) {
			coordinate = coordinates[i];
			if ((coordinate.x >= x) && (coordinate.x < x + width)
					&& (coordinate.y >= y) && (coordinate.y < y + depth)) {
				point.put((int) ((coordinate.x - x) / cellsize), image.height()
						- (int) ((coordinate.y - y) / cellsize));
				cvRectangle(image, point, point, OpenCVUtil.OBSTACLE,
						CV_FILLED, 8, 0);

			}
		}
		point.deallocate();
		OpenCVUtil.imageToGrid(image, grid);

		image.release();
		image = null;
		return grid;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}
}
