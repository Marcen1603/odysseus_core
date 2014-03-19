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
 * 
 */
public class MatrixSubMatrixFunction extends AbstractFunction<double[][]> {

    /**
     * 
     */
    private static final long serialVersionUID = 353844443032562193L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.MATRIXS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public MatrixSubMatrixFunction() {
        super("subMatrix", 5, accTypes, SDFDatatype.MATRIX_DOUBLE);
    }

    @Override
    public double[][] getValue() {
        RealMatrix a = new Array2DRowRealMatrix((double[][]) this.getInputValue(0), false);
        int startRow = this.getNumericalInputValue(1).intValue();
        int endRow = this.getNumericalInputValue(2).intValue();
        int startColumn = this.getNumericalInputValue(3).intValue();
        int endColumn = this.getNumericalInputValue(4).intValue();
        return getValueInternal(a, startRow, endRow, startColumn, endColumn).getData();
    }

    protected RealMatrix getValueInternal(RealMatrix a, int startRow, int endRow, int startColumn, int endColumn) {
        return a.getSubMatrix(startRow, endRow, startColumn, endColumn);
    }

}
