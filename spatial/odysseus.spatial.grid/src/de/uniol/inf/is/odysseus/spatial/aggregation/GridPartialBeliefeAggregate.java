package de.uniol.inf.is.odysseus.spatial.aggregation;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.spatial.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.model.Grid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GridPartialBeliefeAggregate<T> implements IPartialAggregate<T> {

	private double count;
	private final Grid grid;
	private IplImage image;
	private IplImage mask;

	public GridPartialBeliefeAggregate(final Grid grid) {
		this.count = 1.0;
		this.grid = new Grid(grid.origin, grid.width * grid.cellsize,
				grid.depth * grid.cellsize, grid.cellsize, grid.getBuffer());
		this.image = opencv_core.cvCreateImage(
				opencv_core.cvSize(this.grid.width, this.grid.depth),
				opencv_core.IPL_DEPTH_16U, 1);
		this.mask = opencv_core.cvCreateImage(
				opencv_core.cvSize(this.image.width(), this.image.height()),
				opencv_core.IPL_DEPTH_8U, 1);

		IplImage tmp = opencv_core.cvCreateImage(
				opencv_core.cvSize(this.image.width(), this.image.height()),
				opencv_core.IPL_DEPTH_8U, 1);
		OpenCVUtil.gridToImage(this.grid, tmp);

		opencv_imgproc.cvThreshold(tmp, tmp, 100, 0,
				opencv_imgproc.CV_THRESH_TRUNC);

		opencv_core.cvConvertScale(tmp, this.image, 1, 0);
		opencv_core.cvReleaseImage(tmp);
		tmp = null;

		OpenCVUtil.gridToImage(grid, this.mask);
		opencv_imgproc.cvThreshold(this.mask, this.mask, 100, 255,
				opencv_imgproc.CV_THRESH_BINARY);
	}

	public GridPartialBeliefeAggregate(
			final GridPartialBeliefeAggregate<T> gridPartialAggregate) {
		this.grid = new Grid(this.grid.origin, this.grid.width
				* this.grid.cellsize, this.grid.depth * this.grid.cellsize,
				this.grid.cellsize, gridPartialAggregate.grid.getBuffer());
		this.image = opencv_core.cvCreateImage(
				opencv_core.cvSize(this.grid.width, this.grid.depth),
				opencv_core.IPL_DEPTH_16U, 1);
		this.mask = opencv_core.cvCreateImage(
				opencv_core.cvSize(this.image.width(), this.image.height()),
				opencv_core.IPL_DEPTH_8U, 1);
		opencv_core.cvCopy(gridPartialAggregate.image, this.image);
		opencv_core.cvCopy(gridPartialAggregate.mask, this.mask);
		this.count = gridPartialAggregate.count;
	}

	@Override
	public GridPartialBeliefeAggregate<T> clone() {
		return new GridPartialBeliefeAggregate<T>(this);
	}

	public void evaluate() {
		IplImage image = opencv_core.cvCreateImage(
				opencv_core.cvSize(this.image.width(), this.image.height()),
				opencv_core.IPL_DEPTH_8U, 1);
		opencv_core.cvConvertScale(this.image, image, 1.0 / this.count, 0);
		opencv_core.cvOr(image, this.mask, image, null);

		OpenCVUtil.imageToGrid(image, this.grid);

		opencv_core.cvReleaseImage(image);
		opencv_core.cvReleaseImage(this.image);
		opencv_core.cvReleaseImage(this.mask);
		image = null;
		this.image = null;
		this.mask = null;
	}

	public Grid getGrid() {
		return this.grid;
	}

	public void merge(final Grid grid) {
		this.count++;
		IplImage mask = opencv_core.cvCreateImage(
				opencv_core.cvSize(grid.width, grid.depth),
				opencv_core.IPL_DEPTH_8U, 1);
		OpenCVUtil.gridToImage(grid, mask);

		opencv_imgproc.cvThreshold(mask, mask, 100, 255,
				opencv_imgproc.CV_THRESH_BINARY);
		opencv_core.cvAnd(this.mask, mask, this.mask, null);

		IplImage merge = opencv_core.cvCreateImage(
				opencv_core.cvSize(this.image.width(), this.image.height()),
				opencv_core.IPL_DEPTH_16U, 1);

		IplImage tmp = opencv_core.cvCreateImage(
				opencv_core.cvSize(this.image.width(), this.image.height()),
				opencv_core.IPL_DEPTH_8U, 1);
		OpenCVUtil.gridToImage(grid, tmp);

		opencv_imgproc.cvThreshold(tmp, tmp, 100, 0,
				opencv_imgproc.CV_THRESH_TRUNC);

		opencv_core.cvConvertScale(tmp, merge, 1, 0);
		opencv_core.cvReleaseImage(tmp);
		tmp = null;

		opencv_core.cvAdd(this.image, merge, this.image, null);

		opencv_core.cvReleaseImage(merge);
		opencv_core.cvReleaseImage(mask);
		mask = null;
		merge = null;
	}

	@Override
	public String toString() {
		final StringBuffer ret = new StringBuffer("GridPartialAggregate (")
				.append(this.hashCode()).append(")").append(this.grid);
		return ret.toString();
	}

}
