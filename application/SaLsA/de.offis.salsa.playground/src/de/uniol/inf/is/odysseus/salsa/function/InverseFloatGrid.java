package de.uniol.inf.is.odysseus.salsa.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class InverseFloatGrid  extends AbstractFunction<Float[][]> {
    /**
     * 
     */
    private static final long serialVersionUID = 4573210956880331758L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
        {
            SDFDatatype.MATRIX_FLOAT
        }
    };
    private final static double FREE = 0.0f;
    private final static double UNKNOWN = -1.0f;
    private final static double OBSTACLE = 1.0f;

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
        return "InverseFloatGrid";
    }

    @Override
    public Float[][] getValue() {
        final Float[][] grid = this.getInputValue(0);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] >= FREE) {
                    grid[i][j] = Math.abs(grid[i][j] - 1);
                }
            }
        }
        return grid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.MATRIX_FLOAT;
    }

}
