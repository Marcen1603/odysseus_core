package de.uniol.inf.is.odysseus.salsa.function;

import static com.googlecode.javacv.cpp.opencv_core.CV_32F;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_core.cvScalarAll;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_WARP_FILL_OUTLIERS;
import static com.googlecode.javacv.cpp.opencv_imgproc.cv2DRotationMatrix;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvWarpAffine;

import java.nio.ByteBuffer;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvPoint2D32f;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.salsa.model.Grid;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class RotateGrid extends AbstractFunction<Grid> {
    /**
     * 
     */
    private static final long serialVersionUID = -6834872922674099184L;
    private final static byte UNKNOWN = (byte) 0xFF;
    private final CvScalar UNKNOWN_PIXEL = cvScalarAll(255);
    private final int flags = CV_INTER_LINEAR + CV_WARP_FILL_OUTLIERS;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.GRID
            }, {
                SDFDatatype.DOUBLE
            }
    };

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A grid and an angle in degree.");
        }
        else {
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
        IplImage image = cvCreateImage(cvSize(grid.width, grid.depth), IPL_DEPTH_8U, 1);
        IplImage rotatedImage = cvCreateImage(cvSize(grid.width, grid.depth), IPL_DEPTH_8U,
                image.nChannels());

        Coordinate origin = grid.origin;
        double cellsize = grid.cellsize;

        CvPoint2D32f center = new CvPoint2D32f(grid.width / 2, grid.depth / 2);
        double sin = Math.sin(Math.toRadians(angle));
        double cos = Math.cos(Math.toRadians(angle));

        double rotatedOriginX = (origin.x - center.x() * cellsize) * cos
                - (origin.y - center.y() * cellsize) * sin + center.x() * cellsize;
        double rotatedOriginY = (origin.x - center.x() * cellsize) * sin
                + (origin.y - center.y() * cellsize) * cos + center.y() * cellsize;

        Coordinate rotatedOrigin = new Coordinate(rotatedOriginX, rotatedOriginY);

        Grid rotatedGrid = new Grid(rotatedOrigin, grid.width * grid.cellsize, grid.depth
                * grid.cellsize, grid.cellsize);
        rotatedGrid.fill(UNKNOWN);

       // ByteBuffer imageBuffer = image.getByteBuffer().put(grid.getBuffer());
        image.getByteBuffer().put(grid.getBuffer());
//        ByteBuffer buffer = grid.getBuffer();
//        for (int w = 0; w < grid.width; w++) {
//            for (int d = 0; d < grid.depth; d++) {
//                imageBuffer.put(d * image.widthStep() + w, (byte) buffer.get());
//            }
//        }

        CvMat mapMatrix = CvMat.create(2, 3, CV_32F);
        cv2DRotationMatrix(center, angle, 1.0, mapMatrix);
        cvWarpAffine(image, rotatedImage, mapMatrix, flags, UNKNOWN_PIXEL);
        cvReleaseImage(image);
        image = null;
        rotatedGrid.setBuffer(rotatedImage.getByteBuffer());
        cvReleaseImage(rotatedImage);
        rotatedImage = null;
        // for (int l = 0; l < rotatedGrid.length; l++) {
        // for (int w = 0; w < rotatedGrid.width; w++) {
        // if (rotatedGrid.get(l, w) != OBSTACLE) {
        // int x = (int) ((l + 0.5 - centerGridX) * cos - (w + 0.5 - centerGridY) * sin +
        // centerGridX);
        // int y = (int) ((l + 0.5 - centerGridX) * sin + (w + 0.5 - centerGridY) * cos +
        // centerGridY);
        // int x1 = (int) ((l - centerGridX) * cos - (w - centerGridY) * sin + centerGridX);
        // int y1 = (int) ((l - centerGridX) * sin + (w - centerGridY) * cos + centerGridY);
        // int x2 = (int) ((l + 1 - centerGridX) * cos - (w - centerGridY) * sin + centerGridX);
        // int y2 = (int) ((l + 1 - centerGridX) * sin + (w - centerGridY) * cos + centerGridY);
        // int x3 = (int) ((l + 1 - centerGridX) * cos - (w + 1 - centerGridY) * sin + centerGridX);
        // int y3 = (int) ((l + 1 - centerGridX) * sin + (w + 1 - centerGridY) * cos + centerGridY);
        // int x4 = (int) ((l - centerGridX) * cos - (w + 1 - centerGridY) * sin + centerGridX);
        // int y4 = (int) ((l - centerGridX) * sin + (w + 1 - centerGridY) * cos + centerGridY);
        // if (((x >= 0) && (x < grid.length)) && ((y >= 0) && (y < grid.width))
        // && (grid.get(x, y) != UNKNOWN) && (rotatedGrid.get(l, w) != OBSTACLE)) {
        // rotatedGrid.set(l, w, grid.get(x, y));
        // }
        // if (((x1 >= 0) && (x1 < grid.length)) && ((y1 >= 0) && (y1 < grid.width))
        // && (grid.get(x1, y1) != UNKNOWN) && (rotatedGrid.get(l, w) != OBSTACLE)) {
        // rotatedGrid.set(l, w, grid.get(x1, y1));
        // }
        // if (((x2 >= 0) && (x2 < grid.length)) && ((y2 >= 0) && (y2 < grid.width))
        // && (grid.get(x2, y2) != UNKNOWN) && (rotatedGrid.get(l, w) != OBSTACLE)) {
        // rotatedGrid.set(l, w, grid.get(x2, y2));
        // }
        // if (((x3 >= 0) && (x3 < grid.length)) && ((y3 >= 0) && (y3 < grid.width))
        // && (grid.get(x3, y3) != UNKNOWN) && (rotatedGrid.get(l, w) != OBSTACLE)) {
        // rotatedGrid.set(l, w, grid.get(x3, y3));
        // }
        // if (((x4 >= 0) && (x4 < grid.length)) && ((y4 >= 0) && (y4 < grid.width))
        // && (grid.get(x4, y4) != UNKNOWN) && (rotatedGrid.get(l, w) != OBSTACLE)) {
        // rotatedGrid.set(l, w, grid.get(x4, y4));
        // }
        // }
        // }
        // }
        return rotatedGrid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.GRID;
    }

}
