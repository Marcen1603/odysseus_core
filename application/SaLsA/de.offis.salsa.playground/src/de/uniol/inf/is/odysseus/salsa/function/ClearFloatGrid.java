package de.uniol.inf.is.odysseus.salsa.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ClearFloatGrid extends AbstractFunction<Float[][]> {
    /**
     * 
     */
    private static final long serialVersionUID = -3472068441371230697L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.MATRIX_DOUBLE,SDFDatatype.MATRIX_FLOAT
            }, {
                SDFDatatype.MATRIX_DOUBLE,SDFDatatype.MATRIX_FLOAT
            }
    };
    private final static float FREE = 0.0f;
    private final static float UNKNOWN = -1.0f;
    private final static float OBSTACLE = 1.0f;

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
        return "ClearFloatGrid";
    }

    @Override
    public Float[][] getValue() {
        final Float[][] base = this.getInputValue(0);
        final Float[][] grid = this.getInputValue(1);

        for (int i = 0; i < base.length; i++) {
            for (int j = 0; j < base[i].length; j++) {
                if ((grid[i][j] >= FREE) && (base[i][j] == OBSTACLE)) {
                    base[i][j] = FREE;
                }
            }
        }
        return base;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.MATRIX_FLOAT;
    }

}
