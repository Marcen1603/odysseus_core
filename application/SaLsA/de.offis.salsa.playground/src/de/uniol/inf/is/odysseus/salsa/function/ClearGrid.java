package de.uniol.inf.is.odysseus.salsa.function;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class ClearGrid extends AbstractFunction<Double[][]> {
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.MATRIX_DOUBLE
            }, {
                SDFDatatype.MATRIX_DOUBLE
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
    public Double[][] getValue() {
//        final Double[][] base = Arrays.copyOf((Double[][]) this.getInputValue(0),
//                ((Double[][]) this.getInputValue(0)).length);
         final Double[][] base = this.getInputValue(0);
        final Double[][] grid = this.getInputValue(1);

        for (int i = 0; i < base.length; i++) {
            for (int j = 0; j < base[i].length; j++) {
                if (grid[i][j] >= FREE) {
                 //   base[i][j] = FREE;
                }
            }
        }
        return base;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.MATRIX_DOUBLE;
    }

}
