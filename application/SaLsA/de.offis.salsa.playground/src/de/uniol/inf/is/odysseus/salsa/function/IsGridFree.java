package de.uniol.inf.is.odysseus.salsa.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class IsGridFree extends AbstractFunction<Boolean> {
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.MATRIX_DOUBLE
            }, {
                SDFDatatype.INTEGER
            }, {
                SDFDatatype.INTEGER
            }, {
                SDFDatatype.INTEGER
            }, {
                SDFDatatype.INTEGER
            }
    };
    private final static double FREE = 0.0;
    private final static double UNKNOWN = -1.0;
    private final static double OBSTACLE = 1.0;
    @Override
    public int getArity() {
        return 5;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A grid, the x and y coordinates, the width and height.");
        }
        else {
            return accTypes[argPos];
        }
    }

    @Override
    public String getSymbol() {
        return "IsGridFree";
    }

    @Override
    public Boolean getValue() {
        final Double[][] grid = this.getInputValue(0);
        final Integer x = (Integer) this.getInputValue(1);
        final Integer y = (Integer) this.getInputValue(2);
        final Integer width = (Integer) this.getInputValue(3);
        final Integer height = (Integer) this.getInputValue(4);
        boolean free = true;
        for (int i = x; i < width && i < grid.length; i++) {
            for (int j = y; j < height && j < grid[i].length; j++) {
                if (grid[i][j] != FREE) {
                    free = false;
                }
            }
        }
        return free;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.BOOLEAN;
    }

}
