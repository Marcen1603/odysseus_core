package de.uniol.inf.is.odysseus.salsa.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

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
    private final GeometryFactory geometryFactory = new GeometryFactory();

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
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A geometry.");
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

        Double[][] grid = new Double[(int) Math.ceil(width / cellsize)][(int) Math.ceil(height
                / cellsize)];
        for (Double[] cells : grid) {
            Arrays.fill(cells, -1.0);
        }

        int gridMinX = (int) Math.ceil(width / cellsize);
        int gridMaxX = 0;
        int gridMinY = (int) Math.ceil(height / cellsize);
        int gridMaxY = 0;
        Coordinate[] coordinates = geometry.getCoordinates();

        List<Coordinate> polygonCoordinates = new ArrayList<Coordinate>();
        polygonCoordinates.add(coordinates[0]);
        Coordinate tmp = coordinates[0];
        for (int i = 1; i < coordinates.length; i++) {
            Coordinate coordinate = coordinates[i];
            if ((coordinate.x >= x) && (coordinate.x <= x + width - cellsize)
                    && (coordinate.y >= y) && (coordinate.y <= y + height - cellsize)) {
                if (coordinate.distance(tmp) >= cellsize/2) {
                    int minX = (int) Math.min(tmp.x, coordinate.x);
                    int maxX = (int) Math.max(tmp.x, coordinate.x);
                    int minY = (int) Math.min(tmp.y, coordinate.y);
                    int maxY = (int) Math.max(tmp.y, coordinate.y);

                    minX = minX - (int) (cellsize - Math.abs(minX % cellsize));
                    maxX = maxX + (int) (cellsize - Math.abs(maxX % cellsize));
                    minY = minY - (int) (cellsize - Math.abs(minY % cellsize));
                    maxY = maxY + (int) (cellsize - Math.abs(maxY % cellsize));
                    boolean intersect = false;
                    boolean foundStart = false;
                    boolean foundEnd = false;
                    for (int j = minX; j < maxX; j += cellsize) {
                        for (int k = minY; k < maxY; k += cellsize) {
                            if ((!foundStart) && (isInGridCell(j, k, cellsize, tmp))) {
                                foundStart = true;
                                int gridX = (int) ((j - x) / cellsize);
                                int gridY = (int) ((k - y) / cellsize);
                                gridMinX = Math.min(gridMinX, gridX);
                                gridMaxX = Math.max(gridMaxX, gridX);
                                gridMinY = Math.min(gridMinY, gridY);
                                gridMaxY = Math.max(gridMaxY, gridY);
                                grid[gridX][gridY] = 1.0;
                                intersect = true;
                            }
                            else if ((!foundEnd) && (isInGridCell(j, k, cellsize, coordinate))) {
                                foundEnd = true;
                                int gridX = (int) ((j - x) / cellsize);
                                int gridY = (int) ((k - y) / cellsize);
                                gridMinX = Math.min(gridMinX, gridX);
                                gridMaxX = Math.max(gridMaxX, gridX);
                                gridMinY = Math.min(gridMinY, gridY);
                                gridMaxY = Math.max(gridMaxY, gridY);
                                grid[gridX][gridY] = 1.0;
                                intersect = true;
                            }
                            else if (intersects(j, k, cellsize, tmp, coordinate)) {
                                int gridX = (int) ((j - x) / cellsize);
                                int gridY = (int) ((k - y) / cellsize);

                                gridMinX = Math.min(gridMinX, gridX);
                                gridMaxX = Math.max(gridMaxX, gridX);
                                gridMinY = Math.min(gridMinY, gridY);
                                gridMaxY = Math.max(gridMaxY, gridY);

                                grid[gridX][gridY] = 1.0;

                                intersect = true;
                            }
                        }
                    }
                    if (!intersect) {
                        System.out.println("Something is wrong!: " + minX + " " + maxX + " " + minY
                                + " " + maxY + " " + tmp + " " + coordinate);
                    }
                    tmp = coordinate;
                    polygonCoordinates.add(coordinate);
                }
            }
        }
        Coordinate[] convexHull = polygonCoordinates.toArray(new Coordinate[] {});
        for (int i = gridMinX; i < gridMaxX; i++) {
            for (int j = gridMinY; j < gridMaxY; j++) {
                if (grid[i][j] < 0.0) {
                    Coordinate cell = new Coordinate(x + i * cellsize + cellsize / 2, y + j
                            * cellsize + cellsize / 2);
                    if (isInPolygon(cell, convexHull)) {
                        grid[i][j] = 0.0;
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

    private boolean isInGridCell(double x, double y, double cellsize, Coordinate coordinate) {
        return ((coordinate.x >= x) && (coordinate.x <= x + cellsize) && (coordinate.y >= y) && (coordinate.y <= y
                + cellsize));
    }

    private double getDenominator(double cellsize, Coordinate from, Coordinate to) {
        return ((to.y - from.y) * cellsize) - ((to.x - from.x) * cellsize);
    }

    private double getNumeratorA(double x1, double y1, double x2, double y2, double x3, double y3,
            double x4, double y4) {
        return ((x4 - x3) * (y1 - y3)) - ((y4 - y3) * (x1 - x3));
    }

    private double getNumeratorB(double x1, double y1, double x2, double y2, double x3, double y3) {
        return ((x2 - x1) * (y1 - y3)) - ((y2 - y1) * (x1 - x3));
    }

    private boolean intersects(double x, double y, double cellsize, Coordinate from, Coordinate to) {
        double denominator = getDenominator(cellsize, from, to);
        if (denominator != 0.0) {
            double numeratorA = getNumeratorA(x, y, x, y + cellsize, from.x, from.y, to.x, to.y);
            double numeratorB = getNumeratorB(x, y, x, y + cellsize, from.x, from.y);
            if (isIntersection(denominator, numeratorA, numeratorB)) {
                return true;
            }
            numeratorA = getNumeratorA(x, y + cellsize, x + cellsize, y + cellsize, from.x, from.y,
                    to.x, to.y);
            numeratorB = getNumeratorB(x, y + cellsize, x + cellsize, y + cellsize, from.x, from.y);
            if (isIntersection(denominator, numeratorA, numeratorB)) {
                return true;
            }
            numeratorA = getNumeratorA(x + cellsize, y + cellsize, x + cellsize, y, from.x, from.y,
                    to.x, to.y);
            numeratorB = getNumeratorB(x + cellsize, y + cellsize, x + cellsize, y, from.x, from.y);
            if (isIntersection(denominator, numeratorA, numeratorB)) {
                return true;
            }
        }
        return false;
    }

    private boolean isIntersection(double denominator, double numeratorA, double numeratorB) {
        double factorA = Math.abs(numeratorA / denominator);
        double factorB = Math.abs(numeratorB / denominator);
        if (((factorA >= 0) && (factorA <= 1)) && ((factorB >= 0) && (factorB <= 1))) {
            return true;
        }
        return false;
    }

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
