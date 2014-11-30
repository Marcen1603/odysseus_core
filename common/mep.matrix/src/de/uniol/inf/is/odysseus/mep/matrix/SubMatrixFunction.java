/**
 *
 */
package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SubMatrixFunction extends AbstractFunction<double[][]> {

    /**
     *
     */
    private static final long serialVersionUID = 353844443032562193L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.MATRIXS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public SubMatrixFunction() {
        super("sub", 5, SubMatrixFunction.ACC_TYPES, SDFDatatype.MATRIX_DOUBLE);
    }

    @Override
    public double[][] getValue() {
        final RealMatrix a = new Array2DRowRealMatrix((double[][]) this.getInputValue(0), false);
        final int startRow = this.getNumericalInputValue(1).intValue();
        final int endRow = this.getNumericalInputValue(2).intValue();
        final int startColumn = this.getNumericalInputValue(3).intValue();
        final int endColumn = this.getNumericalInputValue(4).intValue();
        return SubMatrixFunction.getValueInternal(a, startRow, endRow, startColumn, endColumn).getData();
    }

    protected static RealMatrix getValueInternal(final RealMatrix a, final int startRow, final int endRow, final int startColumn, final int endColumn) {
        return a.getSubMatrix(startRow, endRow, startColumn, endColumn);
    }

}
