package de.uniol.inf.is.odysseus.salsa.aggregation;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.salsa.model.Grid;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GridPartialAggregate<T> implements IPartialAggregate<T> {

    private double count;
    private final Grid grid;
    private IplImage image;
    private IplImage mask;

    public GridPartialAggregate(final Grid grid) {
        this.count = 1.0;
        this.grid = new Grid(grid.origin, grid.width * grid.cellsize, grid.depth * grid.cellsize,
                grid.cellsize, grid.getBuffer());
        this.image = opencv_core.cvCreateImage(
                opencv_core.cvSize(this.grid.width, this.grid.depth), opencv_core.IPL_DEPTH_16U, 1);
        this.mask = opencv_core.cvCreateImage(opencv_core.cvSize(this.grid.width, this.grid.depth),
                opencv_core.IPL_DEPTH_8U, 1);

        IplImage tmp = opencv_core.cvCreateImage(
                opencv_core.cvSize(this.grid.width, this.grid.depth), opencv_core.IPL_DEPTH_8U, 1);
        tmp.getByteBuffer().put(this.grid.getBuffer().duplicate());
        // Plausability Grid
        opencv_imgproc.cvThreshold(tmp, tmp, 100, 0, opencv_imgproc.CV_THRESH_TRUNC);
        // Beliefe Grid
        // opencv_imgproc.cvThreshold(tmp, tmp, 100, 0, opencv_imgproc.CV_THRESH_TOZERO_INV);

        opencv_core.cvConvertScale(tmp, this.image, 1, 0);
        opencv_core.cvReleaseImage(tmp);
        tmp = null;

        this.mask.getByteBuffer().put(this.grid.getBuffer().duplicate());
        opencv_imgproc.cvThreshold(this.mask, this.mask, 100, 255, opencv_imgproc.CV_THRESH_BINARY);
    }

    public GridPartialAggregate(final GridPartialAggregate<T> gridPartialAggregate) {
        this.grid = new Grid(this.grid.origin, this.grid.width * this.grid.cellsize,
                this.grid.depth * this.grid.cellsize, this.grid.cellsize,
                gridPartialAggregate.grid.getBuffer());
        this.image = opencv_core.cvCreateImage(
                opencv_core.cvSize(this.grid.width, this.grid.depth), opencv_core.IPL_DEPTH_16U, 1);
        this.mask = opencv_core.cvCreateImage(opencv_core.cvSize(this.grid.width, this.grid.depth),
                opencv_core.IPL_DEPTH_8U, 1);
        opencv_core.cvCopy(gridPartialAggregate.image, this.image);
        opencv_core.cvCopy(gridPartialAggregate.mask, this.mask);
        this.count = gridPartialAggregate.count;
    }

    @Override
    public GridPartialAggregate<T> clone() {
        return new GridPartialAggregate<T>(this);
    }

    public void evaluate() {
        IplImage image = opencv_core.cvCreateImage(
                opencv_core.cvSize(this.grid.width, this.grid.depth), opencv_core.IPL_DEPTH_8U, 1);
        opencv_core.cvConvertScale(this.image, image, 1.0 / this.count, 0);
        opencv_core.cvOr(image, this.mask, image, null);

        for (int d = 0; d < this.grid.depth; d++) {
            if ((d * image.widthStep()) > this.grid.size) {
                image.getByteBuffer(d * image.widthStep()).get(this.grid.get(),
                        d * this.grid.width, image.width());
            }
            else {
                image.getByteBuffer(d * image.widthStep()).get(this.grid.get(),
                        d * this.grid.width, image.widthStep());
            }
        }
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
        IplImage mask = opencv_core.cvCreateImage(opencv_core.cvSize(grid.width, grid.depth),
                opencv_core.IPL_DEPTH_8U, 1);
        mask.getByteBuffer().put(grid.getBuffer().duplicate());
        opencv_imgproc.cvThreshold(mask, mask, 100, 255, opencv_imgproc.CV_THRESH_BINARY);
        opencv_core.cvAnd(this.mask, mask, this.mask, null);

        IplImage merge = opencv_core.cvCreateImage(opencv_core.cvSize(grid.width, grid.depth),
                opencv_core.IPL_DEPTH_16U, 1);

        IplImage tmp = opencv_core.cvCreateImage(
                opencv_core.cvSize(this.grid.width, this.grid.depth), opencv_core.IPL_DEPTH_8U, 1);
        tmp.getByteBuffer().put(grid.getBuffer().duplicate());

        // Plausability Grid
        opencv_imgproc.cvThreshold(tmp, tmp, 100, 0, opencv_imgproc.CV_THRESH_TRUNC);
        // Beliefe Grid
        // opencv_imgproc.cvThreshold(tmp, tmp, 100, 0, opencv_imgproc.CV_THRESH_TOZERO_INV);

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
        final StringBuffer ret = new StringBuffer("GridPartialAggregate (").append(this.hashCode())
                .append(")").append(this.grid);
        return ret.toString();
    }

}
