package de.uniol.inf.is.odysseus.salsa.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.salsa.model.Grid2D;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class InverseGrid extends AbstractFunction<Grid2D> {
    /**
     * 
     */
    private static final long serialVersionUID = 6682089158563417930L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
        {
            SDFDatatype.GRID_DOUBLE
        }
    };
    private final static double FREE = 0.0;
    private final static double UNKNOWN = -1.0;
    private final static double OBSTACLE = 1.0;

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument: A grid.");
        }
        else {
            return accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "InverseGrid";
    }

    @Override
    public Grid2D getValue() {
        final Grid2D grid = this.getInputValue(0);
        for (int l = 0; l < grid.grid.length; l++) {
            for (int w = 0; w < grid.grid[l].length; w++) {
                if (grid.get(l, w) >= FREE) {
                    grid.set(l, w, Math.abs(grid.get(l, w) - 1));
                }
            }
        }
        return grid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.GRID_DOUBLE;
    }

}
