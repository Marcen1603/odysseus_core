package de.uniol.inf.is.odysseus.spatial.grid.functions;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc;
import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

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
		} else {
			return accTypes[argPos];
		}
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

		IplImage image = opencv_core.cvCreateImage(
				opencv_core.cvSize(grid.width, grid.depth),
				opencv_core.IPL_DEPTH_8U, 1);
		OpenCVUtil.gridToImage(grid, image);

		final int globalGridCenterX = (int) (Math.abs(point.x - grid.origin.x) / grid.cellsize);
		final int globalGridCenterY = (int) (Math.abs(point.y - grid.origin.y) / grid.cellsize);

		int roiWidth = (int) (width / grid.cellsize);
		int roiDepth = (int) (depth / grid.cellsize);

		final int roiX = (int) Math.max(globalGridCenterX - (roiWidth / 2), 0);
		final int roiY = (int) Math.max(globalGridCenterY - (roiDepth / 2), 0);

		roiWidth = (roiX + roiWidth) > grid.width ? grid.width - roiX
				: roiWidth;
		roiDepth = (roiY + roiDepth) < grid.depth ? grid.depth - roiY
				: roiDepth;

		boolean free = false;
		if ((roiWidth > 0) && (roiDepth > 0)) {
			final CvRect roiRect = new CvRect();

			roiRect.x(roiX);
			roiRect.y(roiY);
			roiRect.width(roiWidth);
			roiRect.height(roiDepth);

			IplImage roi = opencv_core.cvCreateImage(
					opencv_core.cvSize(roiRect.width(), roiRect.height()),
					opencv_core.IPL_DEPTH_8U, 1);

			opencv_core.cvSet(roi, OpenCVUtil.UNKNOWN);

			opencv_core.cvSetImageROI(image, roiRect);
			opencv_core.cvCopy(image, roi);
			opencv_core.cvResetImageROI(image);
			opencv_core.cvReleaseImage(image);

			opencv_imgproc.cvThreshold(roi, roi, threshold, 255,
					opencv_imgproc.CV_THRESH_BINARY);
			CvScalar avg = opencv_core.cvAvg(roi, null);
			opencv_core.cvReleaseImage(roi);
			if (avg.getVal(0) == 0.0) {
				free = true;
			}
		}

		return free;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
	}

}
