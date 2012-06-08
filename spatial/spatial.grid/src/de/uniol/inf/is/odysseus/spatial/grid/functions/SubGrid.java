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

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvPoint2D32f;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc;
import com.vividsolutions.jts.geom.Coordinate;

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
public class SubGrid extends AbstractFunction<Grid> {
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFGridDatatype.GRID },
			{ SDFSpatialDatatype.SPATIAL_GEOMETRY,
					SDFSpatialDatatype.SPATIAL_LINE_STRING,
					SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING,
					SDFSpatialDatatype.SPATIAL_MULTI_POINT,
					SDFSpatialDatatype.SPATIAL_MULTI_POLYGON,
					SDFSpatialDatatype.SPATIAL_POINT,
					SDFSpatialDatatype.SPATIAL_POLYGON },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.DOUBLE } };
	private final static int flags = opencv_imgproc.CV_INTER_LINEAR
			+ opencv_imgproc.CV_WARP_FILL_OUTLIERS;
	/**
     * 
     */
	private static final long serialVersionUID = -6671876863268014302L;

	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(
					this.getSymbol()
							+ " has only "
							+ this.getArity()
							+ " argument(s): The grid, the center point, the length and width in cm, and the rotation angle in degrees.");
		}
		return SubGrid.accTypes[argPos];
	}

	@Override
	public int getArity() {
		return 5;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

	@Override
	public String getSymbol() {
		return "SubGrid";
	}

	@Override
	public Grid getValue() {
		final Grid grid = (Grid) this.getInputValue(0);
		final Coordinate point = (Coordinate) this.getInputValue(1);
		final Integer width = this.getNumericalInputValue(2).intValue();
		final Integer depth = this.getNumericalInputValue(3).intValue();
		final Double angle = this.getNumericalInputValue(4);

		// Double angle = -45.0;
		final double sin = Math.sin(Math.toRadians(angle));
		final double cos = Math.cos(Math.toRadians(angle));

		// Origin of the sub-grid in global coordinate system (in cm)
		// x' = x_0 + (x - x_0) cos(alpha) - (y - y_0) sin(alpha)
		// y' = y_0 + (x - x_0) sin(alpha) + (y - y_0) cos(alpha)
		final double originX = point.x
				+ ((point.x - (width / 2) - point.x) * cos)
				- ((point.y - (depth / 2) - point.y) * sin);
		final double originY = point.y
				+ ((point.x - (width / 2) - point.x) * sin)
				+ ((point.y - (depth / 2) - point.y) * cos);

		final Grid subgrid = new Grid(new Coordinate(originX,
				originY), width, depth, grid.cellsize);

		subgrid.fill(GridUtil.UNKNOWN);

		// Position in the global grid (in grid cells)
		final int globalGridCenterX = (int) (Math.abs(point.x - grid.origin.x) / grid.cellsize);
		final int globalGridCenterY = (int) (Math.abs(point.y - grid.origin.y) / grid.cellsize);

		// Width and length of the ROI (can extend) (in grid cells)

		int roiWidth;
		int roiDepth;
		double widthCos = subgrid.width * cos;
		double widthSin = subgrid.width * sin;
		double depthCos = subgrid.height * cos;
		double depthSin = subgrid.height * sin;
		roiWidth = (int) (Math.abs(widthCos) + Math.abs(depthSin) + 0.5);
		roiDepth = (int) (Math.abs(widthSin) + Math.abs(depthCos) + 0.5);

		// Center point in the ROI (in grid cells)
		final CvPoint2D32f center = new CvPoint2D32f(roiWidth / 2,
				(roiDepth / 2));

		final CvRect subRect = new CvRect();
		if ((globalGridCenterX - (roiWidth / 2)) < 0) {
			subRect.x((roiWidth / 2) - globalGridCenterX);
		} else {
			subRect.x(0);
		}
		if ((globalGridCenterY - (roiDepth / 2)) < 0) {
			subRect.y((roiDepth / 2) - globalGridCenterY);
		} else {
			subRect.y(0);
		}
		subRect.width(Math.min(subgrid.width - subRect.x(), roiWidth));
		subRect.height(Math.min(subgrid.height - subRect.y(), roiDepth));

		// X and Y of the ROI (in grid cells)
		final int roiX = Math.max(globalGridCenterX - (roiWidth / 2), 0);
		final int roiY = Math.max((globalGridCenterY) - (roiDepth / 2), 0);

		// Width and length of the ROI (in grid cells)
		roiWidth = (roiX + roiWidth) > grid.width ? grid.width - roiX
				: roiWidth;
		roiDepth = (roiY + roiDepth) < grid.height ? grid.height - roiY
				: roiDepth;

		if ((roiWidth > 0) && (roiDepth > 0)) {
			// Rect for the ROI
			final CvRect roiRect = new CvRect();

			roiRect.x(roiX);
			roiRect.y(roiY);
			roiRect.width(roiWidth);
			roiRect.height(roiDepth);

			IplImage roi = IplImage.create(
					opencv_core.cvSize(roiRect.width(), roiRect.height()),
					opencv_core.IPL_DEPTH_64F, 1);
			opencv_core.cvSet(roi, opencv_core.cvScalarAll(GridUtil.UNKNOWN));

			IplImage image = OpenCVUtil.gridToImage(grid);
			IplImage subimage = OpenCVUtil.gridToImage(subgrid);
			
			opencv_core.cvSetImageROI(image, roiRect);
			opencv_core.cvCopy(image, roi);
			opencv_core.cvResetImageROI(image);

			image.release();
			
			opencv_core.cvSetImageROI(subimage, subRect);
			final CvMat mapMatrix = CvMat.create(2, 3, opencv_core.CV_64F);
			opencv_imgproc.cv2DRotationMatrix(center, angle, 1.0, mapMatrix);
			opencv_imgproc.cvWarpAffine(roi, subimage, mapMatrix,
					SubGrid.flags, opencv_core.cvScalarAll(GridUtil.UNKNOWN));
			mapMatrix.release();
			opencv_core.cvResetImageROI(subimage);
			roi.release();
			roi = null;
			OpenCVUtil.imageToGrid(subimage, subgrid);
		}
		return subgrid;
	}
}
