package de.uniol.inf.is.odysseus.salsa.function;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvPoint2D32f;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc;
import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.salsa.model.Grid;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SubGrid extends AbstractFunction<Grid> {
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.GRID
            },
            {
                    SDFDatatype.SPATIAL, SDFDatatype.SPATIAL_LINE, SDFDatatype.SPATIAL_MULTI_LINE,
                    SDFDatatype.SPATIAL_MULTI_POINT, SDFDatatype.SPATIAL_MULTI_POLYGON,
                    SDFDatatype.SPATIAL_POINT, SDFDatatype.SPATIAL_POLYGON
            }, {
                SDFDatatype.DOUBLE
            }, {
                SDFDatatype.DOUBLE
            }, {
                SDFDatatype.DOUBLE
            }
    };
    private final static int flags = opencv_imgproc.CV_INTER_LINEAR
            + opencv_imgproc.CV_WARP_FILL_OUTLIERS;
    /**
     * 
     */
    private static final long serialVersionUID = -6671876863268014302L;
    private final static byte UNKNOWN = (byte) 0xFF;
    private final static CvScalar UNKNOWN_PIXEL = opencv_core.cvScalarAll(255);

    @Override
    public SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(
                    this.getSymbol()
                            + " has only "
                            + this.getArity()
                            + " argument(s): The grid, the center point, the length and width in cm, and the rotation angle in degrees.");
        }
        else {
            return SubGrid.accTypes[argPos];
        }
    }

    @Override
    public int getArity() {
        // FIXME Set back to 5
        return 4;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.GRID;
    }

    @Override
    public String getSymbol() {
        return "SubGrid";
    }

    @Override
    public Grid getValue() {
        final Grid grid = (Grid) this.getInputValue(0);
        // final Coordinate point = (Coordinate) this.getInputValue(1);
        // final Double length = (Double) this.getInputValue(2);
        // final Double width = (Double) this.getInputValue(3);
        // final Float angle = (Float) this.getInputValue(4);

        final Coordinate point = new Coordinate(0, 0);
        final Double width = (Double) this.getInputValue(1);
        final Double depth = (Double) this.getInputValue(2);
        // final Double angle = (Double) this.getInputValue(3);
        Double angle = -45.0;
        final double sin = Math.sin(Math.toRadians(angle));
        final double cos = Math.cos(Math.toRadians(angle));

        // Origin of the sub-grid in global coordinate system (in cm)
        // x' = x_0 + (x - x_0) cos(alpha) - (y - y_0) sin(alpha)
        // y' = y_0 + (x - x_0) sin(alpha) + (y - y_0) cos(alpha)
        final double originX = point.x + (((width / 2) * cos) - ((depth / 2) * sin));
        final double originY = point.y + ((width / 2) * sin) + ((depth / 2) * cos);

        final Grid subgrid = new Grid(new Coordinate(originX, originY), width, depth, grid.cellsize);

        IplImage subimage = opencv_core.cvCreateImage(
                opencv_core.cvSize(subgrid.width, subgrid.depth), opencv_core.IPL_DEPTH_8U, 1);
        opencv_core.cvSet(subimage, SubGrid.UNKNOWN_PIXEL);

        // Create global grid view
        IplImage image = opencv_core.cvCreateImage(opencv_core.cvSize(grid.width, grid.depth),
                opencv_core.IPL_DEPTH_8U, 1);
        image.getByteBuffer().put(grid.getBuffer());

        // Position in the global grid (in grid cells)
        final int globalGridCenterX = (int) (Math.abs(point.x - grid.origin.x) / grid.cellsize);
        final int globalGridCenterY = (int) (Math.abs(point.y - grid.origin.y) / grid.cellsize);

        // Width and length of the ROI (can extend) (in grid cells)

        int roiWidth;
        int roiDepth;
        double widthCos = subgrid.width * cos;
        double widthSin = subgrid.width * sin;
        double depthCos = subgrid.depth * cos;
        double depthSin = subgrid.depth * sin;
        roiWidth = (int) (Math.abs(widthCos) + Math.abs(depthSin) + 0.5);
        roiDepth = (int) (Math.abs(widthSin) + Math.abs(depthCos) + 0.5);

        // Center point in the ROI (in grid cells)
        final CvPoint2D32f center = new CvPoint2D32f(roiWidth / 2, roiDepth / 2);

        // X and Y of the ROI (in grid cells)
        final int roiX = Math.max(globalGridCenterX - (roiWidth / 2), 0);
        final int roiY = Math.max(globalGridCenterY - (roiDepth / 2), 0);

        // Width and length of the ROI (in grid cells)
        roiWidth = (roiX + roiWidth) > grid.width ? grid.width - roiX : roiWidth;
        roiDepth = (roiY + roiDepth) > grid.depth ? grid.depth - roiY : roiDepth;

        // Rect for the ROI
        final CvRect roiRect = new CvRect();
        roiRect.x(roiX);
        roiRect.y(roiY);
        roiRect.width(roiWidth);
        roiRect.height(roiDepth);

        IplImage roi = opencv_core.cvCreateImage(
                opencv_core.cvSize(roiRect.width(), roiRect.height()), opencv_core.IPL_DEPTH_8U, 1);
        opencv_core.cvSet(roi, SubGrid.UNKNOWN_PIXEL);

        opencv_core.cvSetImageROI(image, roiRect);
        opencv_core.cvCopy(image, roi);
        opencv_core.cvResetImageROI(image);

        final CvMat mapMatrix = CvMat.create(2, 3, opencv_core.CV_32F);
        opencv_imgproc.cv2DRotationMatrix(center, angle, 1.0, mapMatrix);
        opencv_imgproc.cvWarpAffine(roi, subimage, mapMatrix, SubGrid.flags, SubGrid.UNKNOWN_PIXEL);

        for (int d = 0; d < subgrid.depth; d++) {
            if (d * subimage.widthStep() > subgrid.size) {
                subimage.getByteBuffer(d * subimage.widthStep()).get(subgrid.get(),
                        d * subgrid.width, subimage.width());
            }
            else {
                subimage.getByteBuffer(d * subimage.widthStep()).get(subgrid.get(),
                        d * subgrid.width, subimage.widthStep());
            }
        }

        opencv_core.cvReleaseImage(image);
        image = null;
        opencv_core.cvReleaseImage(roi);
        roi = null;
        opencv_core.cvReleaseImage(subimage);
        subimage = null;
        return subgrid;
    }

}
