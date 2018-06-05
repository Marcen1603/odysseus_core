/**
 *
 */
package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SubVectorFunction extends AbstractFunction<double[]> {

    /**
     *
     */
    private static final long serialVersionUID = 1864171890332481275L;
    private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.VECTORS, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS };

    public SubVectorFunction() {
        super("sub", 3, SubVectorFunction.ACC_TYPES, SDFDatatype.VECTOR_DOUBLE);
    }

    @Override
    public double[] getValue() {
        final RealVector a = new ArrayRealVector((double[]) this.getInputValue(0), false);
        final int startRow = this.getNumericalInputValue(1).intValue();
        final int endRow = this.getNumericalInputValue(2).intValue();
        return SubVectorFunction.getValueInternal(a, startRow, endRow).toArray();
    }

    protected static RealVector getValueInternal(final RealVector a, final int startRow, final int endRow) {
        return a.getSubVector(startRow, endRow);
    }

}
