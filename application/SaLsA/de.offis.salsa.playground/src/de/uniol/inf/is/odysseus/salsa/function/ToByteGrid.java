package de.uniol.inf.is.odysseus.salsa.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.salsa.common.GridUtil;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ToByteGrid extends AbstractFunction<Byte[][]> {
    /**
     * 
     */
    private static final long serialVersionUID = 5318197177659509484L;
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
    private final static byte FREE = (byte) 0x00;
    private final static byte UNKNOWN = (byte) 0xFF;
    private final static byte OBSTACLE = (byte) 0x64;

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
        return "ToByteGrid";
    }

    @Override
    public Byte[][] getValue() {
        final Geometry geometry = (Geometry) this.getInputValue(0);
        final Double x = (Double) this.getInputValue(1);
        final Double y = (Double) this.getInputValue(2);
        final Double width = (Double) this.getInputValue(3);
        final Double height = (Double) this.getInputValue(4);
        final Double cellsize = (Double) this.getInputValue(5);

        // FIXME check for real size of grid
        Byte[][] grid = new Byte[(int) (width / cellsize) + 1][(int) (height / cellsize) + 1];
        for (Byte[] cells : grid) {
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
                if ((GridUtil.isInGrid(x, y, width, height, coordinate))
                        && (GridUtil.isInGrid(x, y, width, height, tmp))) {
                    if (coordinate.distance(tmp) > cellsize / 2) {
                        polygonCoordinates.add(coordinate);
                        int minX = (int) Math.min(tmp.x, coordinate.x);
                        int maxX = (int) Math.max(tmp.x, coordinate.x);
                        int minY = (int) Math.min(tmp.y, coordinate.y);
                        int maxY = (int) Math.max(tmp.y, coordinate.y);

                        if ((minX % cellsize) > 0) {
                            minX = minX - (int) (cellsize - Math.abs(minX % cellsize));
                        }
                        if ((maxX % cellsize) > 0) {
                            maxX = maxX + (int) (cellsize - Math.abs(maxX % cellsize));
                        }
                        if ((minY % cellsize) > 0) {
                            minY = minY - (int) (cellsize - Math.abs(minY % cellsize));
                        }
                        if ((maxY % cellsize) > 0) {
                            maxY = maxY + (int) (cellsize - Math.abs(maxY % cellsize));
                        }
                        boolean foundStart = false;
                        boolean foundEnd = false;

                        for (int j = minX; j < maxX; j += cellsize) {
                            for (int k = minY; k < maxY; k += cellsize) {
                                if ((j < x + width) && (k < y + height)) {
                                    boolean foundIntersection = false;

                                    // Check if the last tmp coordinate is in this grid cell
                                    if ((!foundStart)
                                            && (GridUtil.isInGridCell(j, k, cellsize, tmp))) {
                                        foundStart = true;
                                        foundIntersection = true;
                                    }
                                    // Check if the current coordinate is in the grid cell
                                    else if ((!foundEnd)
                                            && (GridUtil.isInGridCell(j, k, cellsize, coordinate))) {
                                        foundEnd = true;
                                        foundIntersection = true;
                                    }
                                    // Check for an intersection between this grid cell and the
                                    // segment
                                    // formed by the last tmp coordinate and the current coordinate
                                    else if (GridUtil.intersects(j, k, cellsize, tmp, coordinate)) {
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
                        if (GridUtil.isInPolygon(cell, convexHull)) {
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
        return SDFDatatype.MATRIX_BYTE;
    }
}
