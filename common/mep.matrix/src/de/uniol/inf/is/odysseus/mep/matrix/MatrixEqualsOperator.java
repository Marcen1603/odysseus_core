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


	private static final long serialVersionUID = -7610056055246507473L;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {SDFDatatype.MATRIXS,SDFDatatype.MATRIXS};

	public MatrixEqualsOperator() {
		super("==",accTypes,SDFDatatype.BOOLEAN);
	}
	
	@Override
	public int getPrecedence() {
		return 9;
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

}
