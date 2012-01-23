package de.uniol.inf.is.odysseus.salsa.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.salsa.model.Grid;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class InverseGrid extends AbstractFunction<Grid> {
    /**
     * 
     */
    private static final long serialVersionUID = 6682089158563417930L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
        {
            SDFDatatype.GRID
        }
    };
    private final static byte FREE = (byte) 0x00;
    private final static byte UNKNOWN = (byte) 0xFF;
    private final static byte OBSTACLE = (byte) 0x64;

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
    public Grid getValue() {
        final Grid grid = this.getInputValue(0);
        final Grid inverseGrid = new Grid(grid.origin, grid.width * grid.cellsize, grid.depth
                * grid.cellsize, grid.cellsize);
        inverseGrid.fill(UNKNOWN);
        for (int l = 0; l < grid.width; l++) {
            for (int w = 0; w < grid.depth; w++) {
                if (grid.get(l, w) <= OBSTACLE) {
                    inverseGrid.set(l, w, (byte) (((byte) 0x64) - grid.get(l, w)));
                }
            }
        }
        return inverseGrid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.GRID;
    }

}
