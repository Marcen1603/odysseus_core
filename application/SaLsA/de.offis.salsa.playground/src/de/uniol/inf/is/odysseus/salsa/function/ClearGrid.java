package de.uniol.inf.is.odysseus.salsa.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.salsa.model.Grid2D;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ClearGrid extends AbstractFunction<Grid2D> {
    /**
     * 
     */
    private static final long serialVersionUID = 558853050550138757L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.GRID_DOUBLE
            }, {
                SDFDatatype.GRID_DOUBLE
            }
    };
    private final static double FREE = 0.0;
    private final static double UNKNOWN = -1.0;
    private final static double OBSTACLE = 1.0;

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): Two grids.");
        }
        else {
            return accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "ClearGrid";
    }

    @Override
    public Grid2D getValue() {
        final Grid2D base = this.getInputValue(0);
        final Grid2D grid = this.getInputValue(1);

        int startX = (int) ((grid.origin.x - base.origin.x) / grid.cellsize);
        int startY = (int) ((grid.origin.y - base.origin.y) / grid.cellsize);
        int endX = (int) ((grid.origin.x - base.origin.x + grid.grid.length * grid.cellsize) / grid.cellsize);
        int endY = (int) ((grid.origin.y - base.origin.y + grid.grid[0].length * grid.cellsize) / grid.cellsize);
        for (int l = startX; l < base.grid.length && l < endX; l++) {
            for (int w = startY; w < base.grid[l].length && l < endY; w++) {
                if ((grid.get(l, w) >= FREE) && (base.get(l, w) == OBSTACLE)) {
                    base.set(l, w, FREE);
                }
            }
        }
        return base;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.GRID_DOUBLE;
    }

}
