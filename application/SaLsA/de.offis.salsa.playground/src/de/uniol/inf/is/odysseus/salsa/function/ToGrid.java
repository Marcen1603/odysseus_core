package de.uniol.inf.is.odysseus.salsa.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ToGrid extends AbstractFunction<Double[][]> {

    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                    SDFDatatype.SPATIAL, SDFDatatype.SPATIAL_LINE, SDFDatatype.SPATIAL_MULTI_LINE,
                    SDFDatatype.SPATIAL_MULTI_POINT, SDFDatatype.SPATIAL_MULTI_POLYGON,
                    SDFDatatype.SPATIAL_POINT, SDFDatatype.SPATIAL_POLYGON
            }, {
                SDFDatatype.INTEGER
            }, {
                SDFDatatype.INTEGER
            }, {
                SDFDatatype.INTEGER
            }, {
                SDFDatatype.INTEGER
            }, {
                SDFDatatype.INTEGER
            }
    };
    private final static double FREE = 0.0;
    private final static double UNKNOWN = -1.0;
    private final static double OBSTACLE = 1.0;

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
    public Double[][] getValue() {
        final Geometry geometry = (Geometry) this.getInputValue(0);
        final Double x = (Double) this.getInputValue(1);
        final Double y = (Double) this.getInputValue(2);
        final Double width = (Double) this.getInputValue(3);
        final Double height = (Double) this.getInputValue(4);
        final Double cellsize = this.getInputValue(5);

        // FIXME check for real size of grid
        Double[][] grid = new Double[(int) (width / cellsize) + 1][(int) (height / cellsize) + 1];
        for (Double[] cells : grid) {
            Arrays.fill(cells, UNKNOWN);
        }

        int gridMinX = (int) Math.ceil(width / cellsize);
        int gridMaxX = 0;
        int gridMinY = (int) Math.ceil(height / cellsize);
        int gridMaxY = 0;
        Coordinate[] coordinates = geometry.getCoordinates();

        List<Coordinate> polygonCoordinates = new ArrayList<Coordinate>();
        Coordinate tmp = null;
        // Find the first coordinate in the grid area
        for (int i = 0; i < coordinates.length; i++) {
            Coordinate coordinate = coordinates[i];
            if ((coordinate.x > x) && (coordinate.x < x + width - cellsize) && (coordinate.y > y)
                    && (coordinate.y < y + height - cellsize)) {
                tmp = coordinate;
                polygonCoordinates.add(coordinates[i]);
                break;
            }

        }
        if (tmp != null) {
            for (int i = 1; i < coordinates.length; i++) {
                Coordinate coordinate = coordinates[i];
                // Check for valid coordinate in the grid area
                if ((isInGrid(x, y, width, height, coordinate))
                        && (isInGrid(x, y, width, height, tmp))) {
                    if (coordinate.distance(tmp) > cellsize / 2) {
                        polygonCoordinates.add(coordinate);
                        int minX = (int) Math.min(tmp.x, coordinate.x);
                        int maxX = (int) Math.max(tmp.x, coordinate.x);
                        int minY = (int) Math.min(tmp.y, coordinate.y);
                        int maxY = (int) Math.max(tmp.y, coordinate.y);

                        minX = minX - (int) (cellsize - Math.abs(minX % cellsize));
                        maxX = maxX + (int) (cellsize - Math.abs(maxX % cellsize));
                        minY = minY - (int) (cellsize - Math.abs(minY % cellsize));
                        maxY = maxY + (int) (cellsize - Math.abs(maxY % cellsize));
                        boolean foundStart = false;
                        boolean foundEnd = false;

                        for (int j = minX; j < maxX; j += cellsize) {
                            for (int k = minY; k < maxY; k += cellsize) {
                                if ((j < x + width) && (k < y + height)) {
                                    boolean foundIntersection = false;

                                    // Check if the last tmp coordinate is in this grid cell
                                    if ((!foundStart) && (isInGridCell(j, k, cellsize, tmp))) {
                                        foundStart = true;
                                        foundIntersection = true;
                                    }
                                    // Check if the current coordinate is in the grid cell
                                    else if ((!foundEnd)
                                            && (isInGridCell(j, k, cellsize, coordinate))) {
                                        foundEnd = true;
                                        foundIntersection = true;
                                    }
                                    // Check for an intersection between this grid cell and the
                                    // segment
                                    // formed by the last tmp coordinate and the current coordinate
                                    else if (intersects(j, k, cellsize, tmp, coordinate)) {
                                        foundIntersection = true;
                                    }

                                    if (foundIntersection) {

                                        int gridX = (int) ((j - x) / cellsize);
                                        int gridY = (int) ((k - y) / cellsize);

                                        // Form the bounding box for later calculation and mark the
                                        // grid
                                        // cell
                                        gridMinX = Math.min(gridMinX, gridX);
                                        gridMaxX = Math.max(gridMaxX, gridX);
                                        gridMinY = Math.min(gridMinY, gridY);
                                        gridMaxY = Math.max(gridMaxY, gridY);
                                        grid[gridX][gridY] = OBSTACLE;
                                    }
                                }
                            }
                        }
                        tmp = coordinate;

                    }
                }
                else {
                    tmp = coordinate;
                }
            }
            // Mark all cells inside the polygon that are not marked as an obstacle as free
            Coordinate[] convexHull = polygonCoordinates.toArray(new Coordinate[] {});
            for (int i = gridMinX; i < gridMaxX; i++) {
                for (int j = gridMinY; j < gridMaxY; j++) {
                    if (grid[i][j] < 0.0) {
                        Coordinate cell = new Coordinate(x + i * cellsize + cellsize / 2, y + j
                                * cellsize + cellsize / 2);
                        if (isInPolygon(cell, convexHull)) {
                            grid[i][j] = FREE;
                        }
                    }
                }
            }
        }
        return grid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.MATRIX_DOUBLE;
    }

    /**
     * Checks weather the given {@link Coordinate} is in the given grid
     * 
     * @param x
     *            The x-coordinate of the grid
     * @param y
     *            The y-coordinate of the grid
     * @param height
     *            The height of the grid
     * @param width
     *            The width of the grid
     * @param coordinate
     *            The coordinate to check
     * @return true if the coordinate is in the grid else false
     */
    private boolean isInGrid(double x, double y, double height, double width, Coordinate coordinate) {
        return ((coordinate.x >= x) && (coordinate.x <= x + width) && (coordinate.y >= y) && (coordinate.y <= y
                + height));
    }

    /**
     * Checks weather the given {@link Coordinate} is in the given grid cell
     * 
     * @param x
     *            The x-coordinate of the grid cell
     * @param y
     *            The y-coordinate of the grid cell
     * @param cellsize
     *            The size of the grid cell
     * @param coordinate
     *            The coordinate to check
     * @return true if the coordinate is in the grid cell else false
     */
    private boolean isInGridCell(double x, double y, double cellsize, Coordinate coordinate) {
        return ((coordinate.x >= x) && (coordinate.x <= x + cellsize) && (coordinate.y >= y) && (coordinate.y <= y
                + cellsize));
    }

    /**
     * Calculate the denominator for line line intersection
     * 
     * @param x1
     *            The x-coordinate of the first corner of the grid cell
     * @param y1
     *            The y-coordinate of the first corner of the grid cell
     * @param x2
     *            The x-coordinate of the second corner of the grid cell
     * @param y2
     *            The y-coordinate of the second corner of the grid cell
     * @param from
     *            The first {@link Coordinate} of the polygon
     * @param to
     *            The second {@link Coordinate} of the polygon
     * @return The denominator
     */
    private double getDenominator(double x1, double y1, double x2, double y2, Coordinate from,
            Coordinate to) {
        return ((to.y - from.y) * (x2 - x1)) - ((to.x - from.x) * (y2 - y1));
    }

    /**
     * Calculate the first numerator for line line intersection
     * 
     * @param x1
     *            The x-coordinate of the first corner of the grid cell
     * @param y1
     *            The y-coordinate of the first corner of the grid cell
     * @param x2
     *            The x-coordinate of the second corner of the grid cell
     * @param y2
     *            The y-coordinate of the second corner of the grid cell
     * @param x3
     *            The x-coordinate of the first point of the polygon
     * @param y3
     *            The y-coordinate of the first point of the polygon
     * @param x4
     *            The x-coordinate of the second point of the polygon
     * @param y4
     *            The y-coordinate of the second point of the polygon
     * @return The first numerator
     */
    private double getNumeratorA(double x1, double y1, double x2, double y2, double x3, double y3,
            double x4, double y4) {
        return ((x4 - x3) * (y1 - y3)) - ((y4 - y3) * (x1 - x3));
    }

    /**
     * Calculate the second numerator for line line intersection
     * 
     * @param x1
     *            The x-coordinate of the first corner of the grid cell
     * @param y1
     *            The y-coordinate of the first corner of the grid cell
     * @param x2
     *            The x-coordinate of the second corner of the grid cell
     * @param y2
     *            The y-coordinate of the second corner of the grid cell
     * @param x3
     *            The x-coordinate of the first point of the polygon
     * @param y3
     *            The y-coordinate of the first point of the polygon
     * @return The second numerator
     */
    private double getNumeratorB(double x1, double y1, double x2, double y2, double x3, double y3) {
        return ((x2 - x1) * (y1 - y3)) - ((y2 - y1) * (x1 - x3));
    }

    /**
     * Checks weather the given grid cell intersects with a line of the polygon given by two
     * coordinates
     * 
     * @param x
     *            The x-coordinate of the grid cell
     * @param y
     *            The y-coordinate of the grid cell
     * @param cellsize
     *            The size of the grid cell
     * @param from
     *            The first {@link Coordinate} of the polygon
     * @param to
     *            The second {@link Coordinate} of the polygon
     * @return true if the line formed by the coordinates intersects with the grid cell else false
     */
    private boolean intersects(double x, double y, double cellsize, Coordinate from, Coordinate to) {
        // Check bottom left to top left segment for intersection
        double denominator = getDenominator(x, y, x, y + cellsize, from, to);
        double numeratorA = getNumeratorA(x, y, x, y + cellsize, from.x, from.y, to.x, to.y);
        double numeratorB = getNumeratorB(x, y, x, y + cellsize, from.x, from.y);
        if ((denominator != 0.0) && (isIntersection(denominator, numeratorA, numeratorB))) {
            return true;
        }

        // Check top left to top right segment for intersection
        denominator = getDenominator(x, y + cellsize, x + cellsize, y + cellsize, from, to);
        numeratorA = getNumeratorA(x, y + cellsize, x + cellsize, y + cellsize, from.x, from.y,
                to.x, to.y);
        numeratorB = getNumeratorB(x, y + cellsize, x + cellsize, y + cellsize, from.x, from.y);
        if (isIntersection(denominator, numeratorA, numeratorB)) {
            return true;
        }

        // Check top right to bottom right segment for intersection
        denominator = getDenominator(x + cellsize, y + cellsize, x + cellsize, y, from, to);
        numeratorA = getNumeratorA(x + cellsize, y + cellsize, x + cellsize, y, from.x, from.y,
                to.x, to.y);
        numeratorB = getNumeratorB(x + cellsize, y + cellsize, x + cellsize, y, from.x, from.y);
        if (isIntersection(denominator, numeratorA, numeratorB)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if the given numerators and the denominator allows an intersection between the two
     * line segments
     * which is true if A/d and B/d are both between the values 0 and 1
     * 
     * @param denominator
     *            The denominator
     * @param numeratorA
     *            The first numerator
     * @param numeratorB
     *            The second numerator
     * @return
     */
    private boolean isIntersection(double denominator, double numeratorA, double numeratorB) {
        double factorA = Math.abs(numeratorA / denominator);
        double factorB = Math.abs(numeratorB / denominator);
        if (((factorA >= 0) && (factorA <= 1)) && ((factorB >= 0) && (factorB <= 1))) {
            return true;
        }
        return false;
    }

    /**
     * Calculates the bounding box of a polygon given by an array of coordinates
     * 
     * @param polygon
     *            The polygon as a list of {@link Coordinate}
     * @return The bounding box as an array of four double values with 0: minX, 1:minY, 2:maxX,
     *         3:maxY
     */
    private double[] getBoundingBox(Coordinate[] polygon) {
        final double[] boundingBox = new double[4];
        for (int i = 0; i < polygon.length; i++) {
            final double x = polygon[i].x;
            final double y = polygon[i].y;
            if (x < boundingBox[0]) {
                boundingBox[0] = x;
            }
            if (y < boundingBox[1]) {
                boundingBox[1] = y;
            }
            if (x > boundingBox[2]) {
                boundingBox[2] = x;
            }
            if (y > boundingBox[3]) {
                boundingBox[3] = y;
            }
        }
        return boundingBox;
    }

    /**
     * Checks weather the given coordinate is in the given polygon
     * 
     * @param coordinate
     *            The {@link Coordinate} to check
     * @param polygon
     *            The polygon as an array of {@link Coordinate}
     * @return true if the coordinate is in the polygon else false
     */
    private boolean isInPolygon(Coordinate coordinate, Coordinate[] polygon) {
        int j = polygon.length - 1;
        boolean isIn = false;
        final double[] boundingBox = this.getBoundingBox(polygon);
        if ((coordinate.x > boundingBox[2]) || (coordinate.x < boundingBox[0])
                || (coordinate.y > boundingBox[3]) || (coordinate.y < boundingBox[1])) {
            return false;
        }
        else {
            for (int i = 0; i < polygon.length; i++) {
                if (((polygon[i].y < coordinate.y) && (polygon[j].y >= coordinate.y))
                        || ((polygon[j].y < coordinate.y) && (polygon[i].y >= coordinate.y))) {
                    if (polygon[i].x + (coordinate.y - polygon[i].y)
                            / (polygon[j].y - polygon[i].y) * (polygon[j].x - polygon[i].x) < coordinate.x) {
                        isIn = !isIn;
                    }
                }
                j = i;
            }
            return ((isIn) ? true : false);
        }
    }
}
