package de.uniol.inf.is.odysseus.salsa.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class InverseByteGrid extends AbstractFunction<Byte[][]> {
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
        {
            SDFDatatype.MATRIX_BYTE
        }
    };
    private final static byte FREE = (byte) 0x0;
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
        return "InverseByteGrid";
    }

    @Override
    public Byte[][] getValue() {
        final Byte[][] grid = this.getInputValue(0);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] <= OBSTACLE) {
                    grid[i][j] = (byte) (OBSTACLE - grid[i][j]);
                }
            }
        }
        return grid;
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.MATRIX_BYTE;
    }

}
