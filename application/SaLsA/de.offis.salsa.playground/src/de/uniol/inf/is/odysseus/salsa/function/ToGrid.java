package de.uniol.inf.is.odysseus.salsa.function;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.salsa.common.GridUtil;
import de.uniol.inf.is.odysseus.salsa.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.salsa.model.Grid;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ToGrid extends AbstractFunction<Grid> {
    /**
     * 
     */
    private static final long serialVersionUID = -8606524441544525424L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
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
            }, {
                SDFDatatype.DOUBLE
            }, {
                SDFDatatype.DOUBLE
            }
    };

    @Override
    public int getArity() {
        return 6;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(
                    this.getSymbol()
                            + " has only "
                            + this.getArity()
                            + " argument(s): A geometry, the x and y coordinates, the width and height, and the cellsize.");
        }
        else {
            return accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "ToGrid";
    }

    @Override
    public Grid getValue() {
        final Geometry geometry = (Geometry) this.getInputValue(0);
        final Double x = (Double) this.getInputValue(1);
        final Double y = (Double) this.getInputValue(2);
        Double width = (Double) this.getInputValue(3);
        Double depth = (Double) this.getInputValue(4);
        final Double cellsize = ((Double) this.getInputValue(5));
        final Coordinate[] coordinates = geometry.getCoordinates();

        final Grid grid = new Grid(new Coordinate(x, y), width, depth, cellsize);
        IplImage image = opencv_core.cvCreateImage(opencv_core.cvSize(grid.width, grid.depth),
                opencv_core.IPL_DEPTH_8U, 1);

        cvZero(image);
        opencv_core.cvSet(image, OpenCVUtil.UNKNOWN);

        CvPoint convexHullPoints = new CvPoint(coordinates.length);
        Coordinate coordinate;
        for (int i = 0; i < coordinates.length; i++) {
            coordinate = coordinates[i];
            convexHullPoints.position(i).x((int) ((coordinate.x - x) / cellsize + 0.5))
                    .y(image.height() - (int) ((coordinate.y - y) / cellsize + 0.5));
        }
        cvFillPoly(image, convexHullPoints, new int[] {
            coordinates.length
        }, 1, OpenCVUtil.FREE, 4, 0);

        for (int i = 0; i < coordinates.length; i++) {
            coordinate = coordinates[i];
            if ((coordinate.x >= x) && (coordinate.x < x + width) && (coordinate.y >= y)
                    && (coordinate.y < y + depth)) {
                CvPoint point = new CvPoint((int) ((coordinate.x - x) / cellsize), image.height()
                        - (int) ((coordinate.y - y) / cellsize));
                cvRectangle(image, point, point, OpenCVUtil.OBSTACLE, CV_FILLED, 8, 0);
            }
        }
        OpenCVUtil.imageToGrid(image, grid);

        opencv_core.cvReleaseImage(image);
        image = null;
        return grid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.GRID;
    }
}
