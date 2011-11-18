package de.uniol.inf.is.odysseus.salsa.function;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.salsa.model.Grid2D;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SubDoubleGrid extends AbstractFunction<Grid2D> {
    /**
     * 
     */
    private static final long serialVersionUID = -6671876863268014302L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.GRID_DOUBLE
            },
            {
                SDFDatatype.INTEGER
            },
            {
                    SDFDatatype.SPATIAL, SDFDatatype.SPATIAL_LINE, SDFDatatype.SPATIAL_MULTI_LINE,
                    SDFDatatype.SPATIAL_MULTI_POINT, SDFDatatype.SPATIAL_MULTI_POLYGON,
                    SDFDatatype.SPATIAL_POINT, SDFDatatype.SPATIAL_POLYGON
            }, {
                SDFDatatype.INTEGER
            }
    };

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
        return "SubDoubleGrid";
    }

    @Override
    public Grid2D getValue() {
        final Grid2D grid = (Grid2D) this.getInputValue(0);
        final Coordinate point = (Coordinate) this.getInputValue(1);
        final Double width = (Double) this.getInputValue(2);
        final Double height = (Double) this.getInputValue(3);

        final int positionX = (int) Math.ceil((point.x - grid.origin.x) / grid.cellsize);
        final int positionY = (int) Math.ceil((point.y - grid.origin.y) / grid.cellsize);

        int startX = (int) Math.max(positionX - width / 2, 0);
        int startY = (int) Math.max(positionY - height / 2, 0);
        final int endX = (int) Math.min(startX + width, grid.grid.length);
        final int endY = (int) Math.min(startY + height, grid.grid[0].length);

        if (startX >= endX) {
            startX = endX - 1;
        }
        if (startY >= endY) {
            startY = endY - 1;
        }
        final Grid2D subgrid = new Grid2D(new Coordinate(grid.origin.x + grid.cellsize * startX,
                grid.origin.y + grid.cellsize * startY), endX * grid.cellsize,
                endY * grid.cellsize, grid.cellsize);
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                subgrid.set(i - startX, j - startY, grid.get(i, j));
            }
        }
        return subgrid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.GRID_DOUBLE;
    }
}
