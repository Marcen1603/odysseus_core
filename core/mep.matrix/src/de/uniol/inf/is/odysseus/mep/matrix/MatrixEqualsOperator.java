package de.uniol.inf.is.odysseus.mep.matrix;

import java.util.Arrays;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IOperator;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class MatrixEqualsOperator extends AbstractBinaryOperator<Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7610056055246507473L;

	public static final SDFDatatype[] accTypes = new SDFDatatype[] {
			SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE,
			SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE };

	@Override
	public int getPrecedence() {
		return 9;
	}

	@Override
	public String getSymbol() {
		return "==";
	}

	@Override
	public Boolean getValue() {
		RealMatrix a = MatrixUtils.createRealMatrix((double[][]) this
				.getInputValue(0));
		RealMatrix b = MatrixUtils.createRealMatrix((double[][]) this
				.getInputValue(1));
		return getValueInternal(a, b);
	}

	protected boolean getValueInternal(RealMatrix a, RealMatrix b) {
		return Arrays.equals(a.getData(), b.getData());
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.BOOLEAN;
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
		return false;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<Boolean> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Boolean> operator) {
		return false;
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
