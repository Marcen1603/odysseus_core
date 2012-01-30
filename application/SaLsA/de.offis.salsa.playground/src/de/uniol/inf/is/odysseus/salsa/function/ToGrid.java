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
    private final static byte FREE = (byte) 0x00;
    private final static byte UNKNOWN = (byte) 0xFF;
    private final static byte OBSTACLE = (byte) 0x64;
    private final static CvScalar UNKNOWN_PIXEL = opencv_core.cvScalarAll(255);
    private final static CvScalar FREE_PIXEL = opencv_core.cvScalarAll(0);
    private final static CvScalar OBSTACLE_PIXEL = opencv_core.cvScalarAll(100);

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
        // Fill the grid with UNKNWON
        opencv_core.cvSet(image, UNKNOWN_PIXEL);

        // int gridMinX = grid.width;
        // int gridMaxX = 0;
        // int gridMinY = grid.depth;
        // int gridMaxY = 0;

        // List<Coordinate> polygonCoordinates = new ArrayList<Coordinate>();
        // Coordinate tmp = null;
        // // Find the first coordinate in the grid area
        // for (int i = 0; i < coordinates.length; i++) {
        // Coordinate coordinate = coordinates[i];
        // if ((coordinate.x > x) && (coordinate.x < x + width - cellsize) && (coordinate.y > y)
        // && (coordinate.y < y + depth - cellsize)) {
        // tmp = coordinate;
        // polygonCoordinates.add(coordinates[i]);
        // break;
        // }
        // }
        CvPoint convexHullPoints = new CvPoint(coordinates.length);
        for (int i = 0; i < coordinates.length; i++) {
            convexHullPoints.position(i).x((int) ((coordinates[i].x - x) / cellsize))
                    .y(image.height() - (int) ((coordinates[i].y - y) / cellsize));
        }
        cvFillConvexPoly(image, convexHullPoints.position(0), coordinates.length, FREE_PIXEL, 8, 0);
        for (int i = 0; i < convexHullPoints.capacity(); i++) {
            cvRectangle(image, convexHullPoints.position(i), convexHullPoints.position(i),
                    OBSTACLE_PIXEL, CV_FILLED, 8, 0);
        }

        // if (tmp != null) {
        // for (int c = 1; c < coordinates.length; c++) {
        // Coordinate coordinate = coordinates[c];
        // // Check for valid coordinate in the grid area
        // if ((GridUtil.isInGrid(x, y, width, depth, coordinate))
        // && (GridUtil.isInGrid(x, y, width, depth, tmp))) {
        // if (!GridUtil.isInSameGridCell(x, y, cellsize, coordinate, tmp)) {
        // polygonCoordinates.add(coordinate);
        // int minX = (int) Math.min(tmp.x, coordinate.x);
        // int maxX = (int) Math.max(tmp.x, coordinate.x);
        // int minY = (int) Math.min(tmp.y, coordinate.y);
        // int maxY = (int) Math.max(tmp.y, coordinate.y);
        // if (minX > 0) {
        // minX = minX - (int) (cellsize - Math.abs(minX % cellsize));
        // }
        // if (maxX > 0) {
        // maxX = maxX + (int) (cellsize - Math.abs(maxX % cellsize));
        // }
        // if (minY > 0) {
        // minY = minY - (int) (cellsize - Math.abs(minY % cellsize));
        // }
        // if (maxY > 0) {
        // maxY = maxY + (int) (cellsize - Math.abs(maxY % cellsize));
        // }
        // boolean foundStart = false;
        // boolean foundEnd = false;
        //
        // for (int l = minX; l < maxX; l += cellsize) {
        // for (int w = minY; w < maxY; w += cellsize) {
        // if ((l < x + width) && (w < y + depth)) {
        // boolean foundIntersection = false;
        //
        // // Check if the last tmp coordinate is in this grid cell
        // if ((!foundStart)
        // && (GridUtil.isInGridCell(l, w, cellsize, tmp))) {
        // foundStart = true;
        // foundIntersection = true;
        // }
        // // Check if the current coordinate is in the grid cell
        // else if ((!foundEnd)
        // && (GridUtil.isInGridCell(l, w, cellsize, coordinate))) {
        // foundEnd = true;
        // foundIntersection = true;
        // }
        // // Check for an intersection between this grid cell and the
        // // segment
        // // formed by the last tmp coordinate and the current coordinate
        // else if (GridUtil.intersects(l, w, cellsize, tmp, coordinate)) {
        // foundIntersection = true;
        // }
        //
        // if (foundIntersection) {
        //
        // int gridX = (int) ((l - x) / cellsize);
        // int gridY = (int) ((w - y) / cellsize);
        //
        // // Form the bounding box for later calculation and mark the
        // // grid
        // // cell
        // gridMinX = Math.min(gridMinX, gridX);
        // gridMaxX = Math.max(gridMaxX, gridX);
        // gridMinY = Math.min(gridMinY, gridY);
        // gridMaxY = Math.max(gridMaxY, gridY);
        // if (coordinate.distance(tmp) <= cellsize) {
        // image.getByteBuffer().put(
        // gridY * image.widthStep() + gridX, OBSTACLE);
        //
        // }
        // }
        // }
        // }
        // }
        // tmp = coordinate;
        // }
        // }
        // else {
        // tmp = coordinate;
        // }
        // }
        // // Mark all cells inside the polygon that are not marked as an obstacle as free
        // Coordinate[] convexHull = polygonCoordinates.toArray(new Coordinate[] {});
        //
        // for (int w = gridMinX; w < gridMaxX; w++) {
        // for (int d = gridMinY; d < gridMaxY; d++) {
        // if (image.getByteBuffer().get(d * image.widthStep() + w) == UNKNOWN) {
        // Coordinate cell = new Coordinate(x + w * cellsize + cellsize / 2, y + d
        // * cellsize + cellsize / 2);
        // if (GridUtil.isInPolygon(cell, convexHull)) {
        // image.getByteBuffer().put(d * image.widthStep() + w, FREE);
        // }
        // }
        // }
        // }
        // }
        for (int d = 0; d < grid.depth; d++) {
            if (d * image.widthStep() > grid.size) {
                image.getByteBuffer(d * image.widthStep()).get(grid.get(), d * grid.width,
                        image.width());
            }
            else {
                image.getByteBuffer(d * image.widthStep()).get(grid.get(), d * grid.width,
                        image.widthStep());
            }
        }

        opencv_core.cvReleaseImage(image);
        image = null;
        return grid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.GRID;
    }
}
