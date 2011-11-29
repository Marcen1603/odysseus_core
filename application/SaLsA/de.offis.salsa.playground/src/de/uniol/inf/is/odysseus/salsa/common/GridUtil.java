package de.uniol.inf.is.odysseus.salsa.common;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public final class GridUtil {

    /**
     * Checks weather the given {@link Coordinate} is in the given grid
     * 
     * @param x
     *            The x-coordinate of the grid
     * @param y
     *            The y-coordinate of the grid
     * @param width
     *            The width of the grid
     * @param height
     *            The height of the grid
     * @param coordinate
     *            The coordinate to check
     * @return true if the coordinate is in the grid else false
     */
    public static boolean isInGrid(double x, double y, double width, double height,
            Coordinate coordinate) {
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
    public static boolean isInGridCell(double x, double y, double cellsize, Coordinate coordinate) {
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
    private static double getDenominator(double x1, double y1, double x2, double y2,
            Coordinate from, Coordinate to) {
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
    private static double getNumeratorA(double x1, double y1, double x2, double y2, double x3,
            double y3, double x4, double y4) {
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
    private static double getNumeratorB(double x1, double y1, double x2, double y2, double x3,
            double y3) {
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
    public static boolean intersects(double x, double y, double cellsize, Coordinate from,
            Coordinate to) {
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
    private static boolean isIntersection(double denominator, double numeratorA, double numeratorB) {
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
    public static double[] getBoundingBox(Coordinate[] polygon) {
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
    public static boolean isInPolygon(Coordinate coordinate, Coordinate[] polygon) {
        int j = polygon.length - 1;
        boolean isIn = false;
        final double[] boundingBox = GridUtil.getBoundingBox(polygon);
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
