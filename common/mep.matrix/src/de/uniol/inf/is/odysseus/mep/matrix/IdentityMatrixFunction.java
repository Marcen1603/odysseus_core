package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.MatrixUtils;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class IdentityMatrixFunction extends AbstractFunction<double[][]> {

    private static final long serialVersionUID = 8690713611947801279L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS };

    public IdentityMatrixFunction() {
        super("identity", 1, IdentityMatrixFunction.ACC_TYPES, SDFDatatype.MATRIX_DOUBLE);
    }

    @Override
    public double[][] getValue() {
        final int dimension = this.getNumericalInputValue(0).intValue();
        return IdentityMatrixFunction.getValueInternal(dimension);
    }

    protected static double[][] getValueInternal(final int dimension) {
        return MatrixUtils.createRealIdentityMatrix(dimension).getData();
    }

}
