package de.uniol.inf.is.odysseus.probabilistic.function;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.OrOperator;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticOrOperator extends
		AbstractProbabilisticBinaryOperator<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5389925293553053618L;

	@Override
	public boolean isCommutative() {
		return true;
	}

	@Override
	public boolean isAssociative() {
		return true;
	}

	@Override
	public boolean isLeftDistributiveWith(IOperator<Double> operator) {
		return operator.getClass() == OrOperator.class;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<Double> operator) {
		return operator.getClass() == OrOperator.class;
	}

	@Override
	public int getPrecedence() {
		return 13;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
	}

	@Override
	public String getSymbol() {
		return "||";
	}

	@Override
	public Double getValue() {
		return FastMath.max(getNumericalInputValue(0),
				getNumericalInputValue(1));
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[] {
			SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER,
			SDFDatatype.FLOAT, SDFDatatype.LONG, SDFDatatype.DOUBLE };

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
