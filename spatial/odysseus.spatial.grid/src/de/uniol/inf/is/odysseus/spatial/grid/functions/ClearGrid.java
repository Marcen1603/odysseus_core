package de.uniol.inf.is.odysseus.spatial.grid.functions;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_imgproc;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ClearGrid extends AbstractFunction<Grid> {
	/**
     * 
     */
	private static final long serialVersionUID = 558853050550138757L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFGridDatatype.GRID }, { SDFGridDatatype.GRID } };

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s): Two grids.");
		} else {
			return accTypes[argPos];
		}
	}

	@Override
	public String getSymbol() {
		return "ClearGrid";
	}

	@Override
	public Grid getValue() {
		final Grid base = this.getInputValue(0);
		final Grid grid = this.getInputValue(1);
		final Grid result = new Grid(base.origin, base.width * base.cellsize,
				base.depth * base.cellsize, base.cellsize);

		IplImage baseImage = cvCreateImage(cvSize(base.width, base.depth),
				IPL_DEPTH_8U, 1);
		IplImage gridImage = cvCreateImage(cvSize(grid.width, grid.depth),
				IPL_DEPTH_8U, 1);
		OpenCVUtil.gridToImage(base, baseImage);
		OpenCVUtil.gridToImage(grid, gridImage);

		opencv_imgproc.cvThreshold(gridImage, gridImage, 100, 0,
				opencv_imgproc.CV_THRESH_BINARY_INV);

		opencv_core.cvAnd(baseImage, gridImage, baseImage, null);
		OpenCVUtil.imageToGrid(baseImage, result);

		opencv_core.cvReleaseImage(baseImage);
		opencv_core.cvReleaseImage(gridImage);

		return result;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

}
