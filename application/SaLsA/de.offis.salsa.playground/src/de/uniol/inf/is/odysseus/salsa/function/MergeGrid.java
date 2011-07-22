package de.uniol.inf.is.odysseus.salsa.function;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class MergeGrid extends AbstractFunction<Double[][]> {
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.MATRIX_DOUBLE
            }, {
                SDFDatatype.MATRIX_DOUBLE
            }
    };

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
                    + " argument(s): A geometry.");
        }
        else {
            return accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "MergeGrid";
    }

    @Override
    public Double[][] getValue() {
        final Double[][] grid1 = (Double[][]) this.getInputValue(0);
        final Double[][] grid2 = (Double[][]) this.getInputValue(0);
        if ((grid1.length == grid2.length) && (grid1[0].length == grid2[0].length)) {
            Double[][] grid = new Double[grid1.length][grid1[0].length];
            Arrays.fill(grid, -1.0);

            for (int i = 0; i < grid1.length; i++) {
                for (int j = 0; j < grid1[i].length; j++) {
                    grid[i][j] = grid1[i][j] > grid2[i][j] ? grid1[i][j] : grid2[i][j];
                }
            }
            return grid;
        }
        return null;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.MATRIX_DOUBLE;
    }

}
