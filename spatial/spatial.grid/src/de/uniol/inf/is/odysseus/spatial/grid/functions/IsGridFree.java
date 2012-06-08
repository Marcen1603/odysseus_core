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
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class IsGridFree extends AbstractFunction<Boolean> {
	/**
     * 
     */
	private static final long serialVersionUID = -5768528294591995540L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFGridDatatype.GRID },
			{ SDFSpatialDatatype.SPATIAL_POINT,
					SDFSpatialDatatype.SPATIAL_LINE_STRING,
					SDFSpatialDatatype.SPATIAL_POLYGON,
					SDFSpatialDatatype.SPATIAL_MULTI_POINT,
					SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING,
					SDFSpatialDatatype.SPATIAL_MULTI_POLYGON,
					SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION,
					SDFSpatialDatatype.SPATIAL_GEOMETRY },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.DOUBLE } };

	@Override
	public int getArity() {
		return 5;
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
							+ " argument(s): A grid, the x and y coordinates, the width and height.");
		}
		return accTypes[argPos];
	}

	@Override
	public String getSymbol() {
		return "IsGridFree";
	}

	@Override
	public Boolean getValue() {
		final Grid grid = (Grid) this.getInputValue(0);
		final Coordinate point = (Coordinate) this.getInputValue(1);
		Double width = (Double) this.getInputValue(2);
		Double depth = (Double) this.getInputValue(3);
		Double threshold = (Double) this.getInputValue(4);

		final int globalGridCenterX = (int) (Math.abs(point.x - grid.origin.x) / grid.cellsize);
		final int globalGridCenterY = (int) (Math.abs(point.y - grid.origin.y) / grid.cellsize);

		int roiWidth = (int) (width / grid.cellsize);
		int roiDepth = (int) (depth / grid.cellsize);

		final int roiX = Math.max(globalGridCenterX - (roiWidth / 2), 0);
		final int roiY = Math.max(globalGridCenterY - (roiDepth / 2), 0);

		roiWidth = (roiX + roiWidth) > grid.width ? grid.width - roiX
				: roiWidth;
		roiDepth = (roiY + roiDepth) < grid.height ? grid.height - roiY
				: roiDepth;

		boolean free = false;
		if ((roiWidth > 0) && (roiDepth > 0)) {
			final CvRect roiRect = new CvRect();

			roiRect.x(roiX);
			roiRect.y(roiY);
			roiRect.width(roiWidth);
			roiRect.height(roiDepth);
			IplImage image = OpenCVUtil.gridToImage(grid);
			opencv_core.cvSetImageROI(image, roiRect);
			double[] minVal = new double[1];
			double[] maxVal = new double[1];
			CvPoint minLoc = new CvPoint();
			CvPoint maxLoc = new CvPoint();
			opencv_core
					.cvMinMaxLoc(image, minVal, maxVal, minLoc, maxLoc, null);
			if (maxVal[0] < threshold) {
				free = true;
			}
			opencv_core.cvResetImageROI(image);
			roiRect.deallocate();
			minLoc.deallocate();
			maxLoc.deallocate();
			image.release();
		}
		return free;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
	}

}
