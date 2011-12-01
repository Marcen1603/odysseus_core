package de.uniol.inf.is.odysseus.salsa.function;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.salsa.model.Grid2D;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class RotateGrid extends AbstractFunction<Grid2D> {
    /**
     * 
     */
    private static final long serialVersionUID = -6834872922674099184L;
    private final static double FREE = 0.0;
    private final static double UNKNOWN = -1.0;
    private final static double OBSTACLE = 1.0;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.GRID_DOUBLE
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
    public Grid2D getValue() {
        final Grid2D grid = (Grid2D) this.getInputValue(0);
        Double angle = (Double) this.getInputValue(1);
        angle = Math.toRadians(angle);
        Coordinate origin = grid.origin;
        double cellsize = grid.cellsize;
        double centerGridX = grid.grid.length / 2;
        double centerGridY = grid.grid[0].length / 2;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double rotatedOriginX = (origin.x - centerGridX * cellsize) * cos
                - (origin.y - centerGridY * cellsize) * sin + centerGridX * cellsize;
        double rotatedOriginY = (origin.x - centerGridX * cellsize) * sin
                + (origin.y - centerGridY * cellsize) * cos + centerGridY * cellsize;

        Coordinate rotatedOrigin = new Coordinate(rotatedOriginX, rotatedOriginY);

        Grid2D rotatedGrid = new Grid2D(rotatedOrigin, grid.grid.length * grid.cellsize,
                grid.grid[0].length * grid.cellsize, grid.cellsize);
        rotatedGrid.fill(UNKNOWN);
        for (int l = 0; l < rotatedGrid.grid.length; l++) {
            for (int w = 0; w < rotatedGrid.grid[0].length; w++) {
                if (rotatedGrid.get(l, w) != OBSTACLE) {
                    int x = (int) ((l + 0.5 - centerGridX) * cos - (w + 0.5 - centerGridY) * sin + centerGridX);
                    int y = (int) ((l + 0.5 - centerGridX) * sin + (w + 0.5 - centerGridY) * cos + centerGridY);
                    int x1 = (int) ((l - centerGridX) * cos - (w - centerGridY) * sin + centerGridX);
                    int y1 = (int) ((l - centerGridX) * sin + (w - centerGridY) * cos + centerGridY);
                    int x2 = (int) ((l + 1 - centerGridX) * cos - (w - centerGridY) * sin + centerGridX);
                    int y2 = (int) ((l + 1 - centerGridX) * sin + (w - centerGridY) * cos + centerGridY);
                    int x3 = (int) ((l + 1 - centerGridX) * cos - (w + 1 - centerGridY) * sin + centerGridX);
                    int y3 = (int) ((l + 1 - centerGridX) * sin + (w + 1 - centerGridY) * cos + centerGridY);
                    int x4 = (int) ((l - centerGridX) * cos - (w + 1 - centerGridY) * sin + centerGridX);
                    int y4 = (int) ((l - centerGridX) * sin + (w + 1 - centerGridY) * cos + centerGridY);
                    if (((x >= 0) && (x < grid.grid.length))
                            && ((y >= 0) && (y < grid.grid[0].length))
                            && (grid.get(x, y) != UNKNOWN) && (rotatedGrid.get(l, w) != OBSTACLE)) {
                        rotatedGrid.set(l, w, grid.get(x, y));
                    }
                    if (((x1 >= 0) && (x1 < grid.grid.length))
                            && ((y1 >= 0) && (y1 < grid.grid[0].length))
                            && (grid.get(x1, y1) != UNKNOWN) && (rotatedGrid.get(l, w) != OBSTACLE)) {
                        rotatedGrid.set(l, w, grid.get(x1, y1));
                    }
                    if (((x2 >= 0) && (x2 < grid.grid.length))
                            && ((y2 >= 0) && (y2 < grid.grid[0].length))
                            && (grid.get(x2, y2) != UNKNOWN) && (rotatedGrid.get(l, w) != OBSTACLE)) {
                        rotatedGrid.set(l, w, grid.get(x2, y2));
                    }
                    if (((x3 >= 0) && (x3 < grid.grid.length))
                            && ((y3 >= 0) && (y3 < grid.grid[0].length))
                            && (grid.get(x3, y3) != UNKNOWN) && (rotatedGrid.get(l, w) != OBSTACLE)) {
                        rotatedGrid.set(l, w, grid.get(x3, y3));
                    }
                    if (((x4 >= 0) && (x4 < grid.grid.length))
                            && ((y4 >= 0) && (y4 < grid.grid[0].length))
                            && (grid.get(x4, y4) != UNKNOWN) && (rotatedGrid.get(l, w) != OBSTACLE)) {
                        rotatedGrid.set(l, w, grid.get(x4, y4));
                    }
                }
            }
        }
        return rotatedGrid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.GRID_DOUBLE;
    }

}
