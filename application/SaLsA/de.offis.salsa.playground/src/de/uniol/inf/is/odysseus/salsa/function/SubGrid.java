package de.uniol.inf.is.odysseus.salsa.function;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.salsa.model.Grid2D;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SubGrid extends AbstractFunction<Grid2D> {
    /**
     * 
     */
    private static final long serialVersionUID = -6671876863268014302L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.GRID_DOUBLE
            },
            {
                    SDFDatatype.SPATIAL, SDFDatatype.SPATIAL_LINE, SDFDatatype.SPATIAL_MULTI_LINE,
                    SDFDatatype.SPATIAL_MULTI_POINT, SDFDatatype.SPATIAL_MULTI_POLYGON,
                    SDFDatatype.SPATIAL_POINT, SDFDatatype.SPATIAL_POLYGON
            }, {
                SDFDatatype.DOUBLE
            }, {
                SDFDatatype.DOUBLE
            }
    };
    private final static double UNKNOWN = -1.0;

    @Override
    public int getArity() {
        return 4;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A grid, the grid size, a point, and a distance.");
        }
        else {
            return accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "SubGrid";
    }

    @Override
    public Grid2D getValue() {
        final Grid2D grid = (Grid2D) this.getInputValue(0);
        final Coordinate point = (Coordinate) this.getInputValue(1);
        Double length = (Double) this.getInputValue(2);
        Double width = (Double) this.getInputValue(3);

        final int positionX = (int) (((point.x - grid.origin.x) / grid.cellsize) + 0.5);
        final int positionY = (int) (((point.y - grid.origin.y) / grid.cellsize) + 0.5);

        int startX = (int) (positionX - length / 2);
        int startY = (int) (positionY - width / 2);
        final int endX = (int) (startX + length);
        final int endY = (int) (startY + width);

        final Grid2D subgrid = new Grid2D(new Coordinate(grid.origin.x + grid.cellsize * startX,
                grid.origin.y + grid.cellsize * startY), length * grid.cellsize, width
                * grid.cellsize, grid.cellsize);
        subgrid.fill(UNKNOWN);

        int startGridX = (int) Math.max(startX, 0);
        int startGridY = (int) Math.max(startY, 0);
        int endGridX = (int) Math.min(endX, grid.grid.length);
        int endGridY = (int) Math.min(endY, grid.grid[0].length);
        for (int l = startGridX; l < endGridX; l++) {
            for (int w = startGridY; w < endGridY; w++) {
                subgrid.set(l - startGridX, w - startGridY, grid.get(l, w));
            }
        }
        return subgrid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.GRID_DOUBLE;
    }
}
