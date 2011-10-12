package de.uniol.inf.is.odysseus.salsa.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ClearByteGrid extends AbstractFunction<Byte[][]> {
    /**
     * 
     */
    private static final long serialVersionUID = -6276355544794895058L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
            {
                SDFDatatype.MATRIX_BYTE
            }, {
                SDFDatatype.MATRIX_BYTE
            }
    };
    private final static byte FREE = (byte) 0x0;
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
        return "ClearByteGrid";
    }

    @Override
    public Byte[][] getValue() {
        final Byte[][] base = this.getInputValue(0);
        final Byte[][] grid = this.getInputValue(1);

        for (int i = 0; i < base.length; i++) {
            for (int j = 0; j < base[i].length; j++) {
                if ((grid[i][j] <= OBSTACLE) && (base[i][j] == OBSTACLE)) {
                    base[i][j] = FREE;
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
