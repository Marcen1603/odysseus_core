/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MatrixSubMatrixFunction extends AbstractFunction<double[][]> {

    /**
     * 
     */
    private static final long serialVersionUID = 353844443032562193L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE, SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE },
            SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    @Override
    public String getSymbol() {
        return "subMatrix";
    }

    @Override
    public double[][] getValue() {
        RealMatrix a = MatrixUtils.createRealMatrix((double[][]) this.getInputValue(0));
        int startRow = this.getNumericalInputValue(1).intValue();
        int endRow = this.getNumericalInputValue(2).intValue();
        int startColumn = this.getNumericalInputValue(3).intValue();
        int endColumn = this.getNumericalInputValue(4).intValue();
        return getValueInternal(a, startRow, endRow, startColumn, endColumn).getData();
    }

    protected RealMatrix getValueInternal(RealMatrix a, int startRow, int endRow, int startColumn, int endColumn) {
        return a.getSubMatrix(startRow, endRow, startColumn, endColumn);
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.MATRIX_DOUBLE;
    }

    @Override
    public int getArity() {
        return 5;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
        }
        return accTypes[argPos];
    }
}
