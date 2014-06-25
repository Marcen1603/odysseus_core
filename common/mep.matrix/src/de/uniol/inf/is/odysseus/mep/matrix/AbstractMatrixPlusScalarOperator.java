package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractMatrixPlusScalarOperator extends AbstractBinaryOperator<double[][]> {

    /**
     * 
     */
    private static final long serialVersionUID = -353078863836070665L;

    public AbstractMatrixPlusScalarOperator(final SDFDatatype[][] accTypes) {
        super("+", accTypes, SDFDatatype.MATRIX_DOUBLE);
    }

    @Override
    public int getPrecedence() {
        return 6;
    }

    protected double[][] getValueInternal(final RealMatrix a, final double b) {
        return a.scalarAdd(b).getData();
    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    public boolean isAssociative() {
        return true;
    }

    @Override
    public boolean isLeftDistributiveWith(final IOperator<double[][]> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(final IOperator<double[][]> operator) {
        return false;
    }

}
