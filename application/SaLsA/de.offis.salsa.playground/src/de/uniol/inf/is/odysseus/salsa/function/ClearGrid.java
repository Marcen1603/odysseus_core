package de.uniol.inf.is.odysseus.salsa.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.salsa.model.Grid;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ClearGrid extends AbstractFunction<Grid> {
    /**
     * 
     */
    private static final long serialVersionUID = 558853050550138757L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.GRID
            }, {
                SDFDatatype.GRID
            }
    };
    private final static byte FREE = (byte) 0x00;
    private final static byte UNKNOWN = (byte) 0xFF;
    private final static byte OBSTACLE = (byte) 0x64;

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
    public Grid getValue() {
        final Grid base = this.getInputValue(0);
        final Grid grid = this.getInputValue(1);

        int startX = (int) ((grid.origin.x - base.origin.x) / grid.cellsize);
        int startY = (int) ((grid.origin.y - base.origin.y) / grid.cellsize);
        int endX = (int) ((grid.origin.x - base.origin.x + grid.width * grid.cellsize) / grid.cellsize);
        int endY = (int) ((grid.origin.y - base.origin.y + grid.depth * grid.cellsize) / grid.cellsize);
        for (int l = startX; l < base.width && l < endX; l++) {
            for (int w = startY; w < base.depth && l < endY; w++) {
                if ((grid.get(l, w) >= FREE) && (base.get(l, w) == OBSTACLE)) {
                    base.set(l, w, FREE);
                }
            }
        }
        return base;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.GRID;
    }

}
