package de.uniol.inf.is.odysseus.probabilistic.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticNotOperator extends
		AbstractProbabilisticUnaryOperator<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4123516281481943058L;

	@Override
	public int getPrecedence() {
		return 3;
	}

	@Override
	public String getSymbol() {
		return "!";
	}

	@Override
	public Double getValue() {
		return 1 - ((Number) getInputValue(0)).doubleValue();
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFDatatype.DOUBLE;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.RIGHT_TO_LEFT;
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
		if (argPos > 0) {
			throw new IllegalArgumentException("! has only 1 argument.");
		}
		return accTypes;
	}

}
