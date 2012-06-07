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

import static com.googlecode.javacv.cpp.opencv_core.CV_64F;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_WARP_FILL_OUTLIERS;
import static com.googlecode.javacv.cpp.opencv_imgproc.cv2DRotationMatrix;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvWarpAffine;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvPoint2D32f;
import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.grid.model.CartesianGrid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RotateGrid extends AbstractFunction<CartesianGrid> {
	/**
     * 
     */
	private static final long serialVersionUID = -6834872922674099184L;

	private final int flags = CV_INTER_LINEAR + CV_WARP_FILL_OUTLIERS;
	private static final double UNKNOWN = -Math.log(1.0 - 0.5);
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFGridDatatype.GRID }, { SDFDatatype.DOUBLE } };

	@Override
	public int getArity() {
		return 2;
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
					+ " argument(s): A grid and an angle in degree.");
		}
		return RotateViewPoint.accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "RotateGrid";
	}

	@Override
	public CartesianGrid getValue() {
		final CartesianGrid grid = (CartesianGrid) this.getInputValue(0);
		Double angle = (Double) this.getInputValue(1);

		Coordinate origin = grid.origin;
		double cellsize = grid.cellsize;

		CvPoint2D32f center = new CvPoint2D32f(grid.width / 2, grid.height / 2);
		double sin = Math.sin(Math.toRadians(angle));
		double cos = Math.cos(Math.toRadians(angle));

		double rotatedOriginX = (origin.x - center.x() * cellsize) * cos
				- (origin.y - center.y() * cellsize) * sin + center.x()
				* cellsize;
		double rotatedOriginY = (origin.x - center.x() * cellsize) * sin
				+ (origin.y - center.y() * cellsize) * cos + center.y()
				* cellsize;

		Coordinate rotatedOrigin = new Coordinate(rotatedOriginX,
				rotatedOriginY);

		CartesianGrid rotatedGrid = new CartesianGrid(rotatedOrigin,
				grid.width, grid.height, grid.cellsize);
		rotatedGrid.fill(UNKNOWN);

		CvMat mapMatrix = CvMat.create(2, 3, CV_64F);
		cv2DRotationMatrix(center, angle, 1.0, mapMatrix);
		cvWarpAffine(grid.getImage(), rotatedGrid.getImage(), mapMatrix, flags,
				opencv_core.cvScalarAll(UNKNOWN));
		mapMatrix.release();
		grid.release();
		return rotatedGrid;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

}
