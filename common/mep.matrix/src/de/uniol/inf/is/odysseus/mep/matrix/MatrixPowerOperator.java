package de.uniol.inf.is.odysseus.mep.matrix;

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
public class MatrixPowerOperator extends AbstractBinaryOperator<double[][]> {

	private static final long serialVersionUID = -3615756862571687620L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.MATRIX_BOOLEAN, SDFDatatype.MATRIX_BYTE,
					SDFDatatype.MATRIX_FLOAT, SDFDatatype.MATRIX_DOUBLE },
			{ SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER,
					SDFDatatype.LONG, SDFDatatype.FLOAT, SDFDatatype.DOUBLE } };;

	public MatrixPowerOperator() {
		super("^", accTypes, SDFDatatype.MATRIX_DOUBLE);
	}

	@Override
	public int getPrecedence() {
		return 1;
	}

	@Override
	public double[][] getValue() {
		RealMatrix a = MatrixUtils.createRealMatrix((double[][]) this
				.getInputValue(0));
		int b = ((Number) getInputValue(1)).intValue();

		return getValueInternal(a, b);
	}

	protected double[][] getValueInternal(RealMatrix a, int b) {
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
	public boolean isLeftDistributiveWith(IOperator<double[][]> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<double[][]> operator) {
		return false;
	}

	@Override
	public de.uniol.inf.is.odysseus.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return null;
	}
}
