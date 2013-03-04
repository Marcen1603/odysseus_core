package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class MatrixPlusOperator extends AbstractBinaryOperator<double[][]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8047651458910200756L;

	@Override
	public int getPrecedence() {
		return 6;
	}

	@Override
	public String getSymbol() {
		return "+";
	}

	@Override
	public double[][] getValue() {
		RealMatrix a = MatrixUtils.createRealMatrix((double[][]) this
				.getInputValue(0));
		RealMatrix b = MatrixUtils.createRealMatrix((double[][]) this
				.getInputValue(1));

		return getValueInternal(a, b);
	}

	protected double[][] getValueInternal(RealMatrix a, RealMatrix b) {
		return a.add(b).getData();
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.MATRIX_DOUBLE;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
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
	public boolean isLeftDistributiveWith(IOperator<double[][]> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<double[][]> operator) {
		return false;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[] {
			SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE,
			SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE };

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
