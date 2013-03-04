package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.MatrixUtils;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class MatrixIdentityMatrixFunction extends AbstractFunction<double[][]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8690713611947801279L;
	public static final SDFDatatype[] accTypes = new SDFDatatype[] {
			SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER,
			SDFDatatype.LONG, SDFDatatype.FLOAT, SDFDatatype.DOUBLE };

	@Override
	public String getSymbol() {
		return "identity";
	}

	@Override
	public double[][] getValue() {
		int dimension = getNumericalInputValue(0).intValue();
		return getValueInternal(dimension);
	}

	protected double[][] getValueInternal(int dimension) {
		return MatrixUtils.createRealIdentityMatrix(dimension).getData();
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.MATRIX_DOUBLE;
	}

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity() - 1) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument(s).");
		}
		return accTypes;
	}
}
