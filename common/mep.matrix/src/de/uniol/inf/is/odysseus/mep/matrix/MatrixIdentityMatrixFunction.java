package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.MatrixUtils;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class MatrixIdentityMatrixFunction extends AbstractFunction<double[][]> {

    private static final long serialVersionUID = 8690713611947801279L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { SDFDatatype.NUMBERS };

    public MatrixIdentityMatrixFunction() {
        super("identity", 1, accTypes, SDFDatatype.MATRIX_DOUBLE);
    }

    @Override
    public double[][] getValue() {
        int dimension = getNumericalInputValue(0).intValue();
        return getValueInternal(dimension);
    }

    protected double[][] getValueInternal(int dimension) {
        return MatrixUtils.createRealIdentityMatrix(dimension).getData();
    }

}
