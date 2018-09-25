package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class MatrixPowerOperator extends AbstractBinaryOperator<double[][]> {

    private static final long serialVersionUID = -3615756862571687620L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.MATRIXS, SDFDatatype.NUMBERS };

    public MatrixPowerOperator() {
        super("^", MatrixPowerOperator.ACC_TYPES, SDFDatatype.MATRIX_DOUBLE);
    }

    @Override
    public int getPrecedence() {
        return 1;
    }

    @Override
    public double[][] getValue() {
        final RealMatrix a = new Array2DRowRealMatrix((double[][]) this.getInputValue(0), false);
        final int b = ((Number) this.getInputValue(1)).intValue();

        return MatrixPowerOperator.getValueInternal(a, b);
    }

    protected static double[][] getValueInternal(final RealMatrix a, final int b) {
        return a.power(b).getData();
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    public boolean isAssociative() {
        return false;
    }

    @Override
    public boolean isLeftDistributiveWith(final IOperator<double[][]> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(final IOperator<double[][]> operator) {
        return false;
    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return null;
    }
}
