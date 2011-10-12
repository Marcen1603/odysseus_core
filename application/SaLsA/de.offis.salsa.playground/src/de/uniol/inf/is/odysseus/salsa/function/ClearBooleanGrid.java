package de.uniol.inf.is.odysseus.salsa.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ClearBooleanGrid extends AbstractFunction<Boolean[][]> {
    /**
     * 
     */
    private static final long serialVersionUID = 4671931088592907112L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.MATRIX_BOOLEAN
            }, {
                SDFDatatype.MATRIX_BOOLEAN
            }
    };
    private final static boolean FREE = false;
    private final static boolean OBSTACLE = true;

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
        return "ClearBooleanGrid";
    }

    @Override
    public Boolean[][] getValue() {
        final Boolean[][] base = this.getInputValue(0);
        final Boolean[][] grid = this.getInputValue(1);

        for (int i = 0; i < base.length; i++) {
            for (int j = 0; j < base[i].length; j++) {
                if ((grid[i][j] == FREE) && (base[i][j] == OBSTACLE)) {
                    base[i][j] = FREE;
                }
            }
        }
        return base;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.MATRIX_BOOLEAN;
    }

}
