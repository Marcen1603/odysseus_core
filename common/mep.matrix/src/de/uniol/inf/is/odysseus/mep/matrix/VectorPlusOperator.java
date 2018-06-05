package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class VectorPlusOperator extends AbstractBinaryOperator<double[]> {

    /**
     *
     */
    private static final long serialVersionUID = 6660768041845323416L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.VECTORS, SDFDatatype.VECTORS };

    public VectorPlusOperator() {
        super("+", VectorPlusOperator.ACC_TYPES, SDFDatatype.VECTOR_DOUBLE);
    }

    @Override
    public int getPrecedence() {
        return 6;
    }

    @Override
    public double[] getValue() {
        final RealVector a = new ArrayRealVector((double[]) this.getInputValue(0), false);
        final RealVector b = new ArrayRealVector((double[]) this.getInputValue(1), false);

        return VectorPlusOperator.getValueInternal(a, b);
    }

    protected static double[] getValueInternal(final RealVector a, final RealVector b) {
        return a.add(b).toArray();
    }

    @Override
    public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
        return ASSOCIATIVITY.LEFT_TO_RIGHT;
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
    public boolean isLeftDistributiveWith(final IOperator<double[]> operator) {
        return false;
    }

    @Override
    public boolean isRightDistributiveWith(final IOperator<double[]> operator) {
        return false;
    }

}
