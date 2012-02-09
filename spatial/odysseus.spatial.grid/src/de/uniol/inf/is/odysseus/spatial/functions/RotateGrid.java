package de.uniol.inf.is.odysseus.spatial.functions;

import static com.googlecode.javacv.cpp.opencv_core.CV_32F;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_WARP_FILL_OUTLIERS;
import static com.googlecode.javacv.cpp.opencv_imgproc.cv2DRotationMatrix;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvWarpAffine;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvPoint2D32f;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.spatial.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.model.Grid;

public class RotateGrid extends AbstractFunction<Grid> {
	/**
     * 
     */
	private static final long serialVersionUID = -6834872922674099184L;

	private final int flags = CV_INTER_LINEAR + CV_WARP_FILL_OUTLIERS;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.GRID }, { SDFDatatype.DOUBLE } };

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
		} else {
			return RotateViewPoint.accTypes[argPos];
		}
	}

	@Override
	public String getSymbol() {
		return "RotateGrid";
	}

	@Override
	public Grid getValue() {
		final Grid grid = (Grid) this.getInputValue(0);
		Double angle = (Double) this.getInputValue(1);
		IplImage image = cvCreateImage(cvSize(grid.width, grid.depth),
				IPL_DEPTH_8U, 1);
		IplImage rotatedImage = cvCreateImage(cvSize(grid.width, grid.depth),
				IPL_DEPTH_8U, image.nChannels());

		Coordinate origin = grid.origin;
		double cellsize = grid.cellsize;

		CvPoint2D32f center = new CvPoint2D32f(grid.width / 2, grid.depth / 2);
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

		Grid rotatedGrid = new Grid(rotatedOrigin, grid.width * grid.cellsize,
				grid.depth * grid.cellsize, grid.cellsize);
		opencv_core.cvSet(rotatedImage, OpenCVUtil.UNKNOWN);

		OpenCVUtil.gridToImage(grid, image);

		CvMat mapMatrix = CvMat.create(2, 3, CV_32F);
		cv2DRotationMatrix(center, angle, 1.0, mapMatrix);
		cvWarpAffine(image, rotatedImage, mapMatrix, flags, OpenCVUtil.UNKNOWN);
		cvReleaseImage(image);
		image = null;
		OpenCVUtil.imageToGrid(rotatedImage, rotatedGrid);

		cvReleaseImage(rotatedImage);
		rotatedImage = null;

		return rotatedGrid;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.GRID;
	}

}
