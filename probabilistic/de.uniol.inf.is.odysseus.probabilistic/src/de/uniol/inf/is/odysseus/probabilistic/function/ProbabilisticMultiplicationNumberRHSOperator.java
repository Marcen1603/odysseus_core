package de.uniol.inf.is.odysseus.probabilistic.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticMultiplicationNumberRHSOperator extends
		ProbabilisticMultiplicationOperator {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5137851422240905510L;

	@Override
	public ProbabilisticDouble getValue() {
		AbstractProbabilisticValue<?> a = getInputValue(0);
		ProbabilisticDouble b = new ProbabilisticDouble(
				getNumericalInputValue(1), 1.0);
		return getValueInternal(a, b);
	}

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFProbabilisticDatatype.PROBABILISTIC_BYTE,
					SDFProbabilisticDatatype.PROBABILISTIC_SHORT,
					SDFProbabilisticDatatype.PROBABILISTIC_INTEGER,
					SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
					SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE,
					SDFProbabilisticDatatype.PROBABILISTIC_LONG },
			{ SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER,
					SDFDatatype.LONG, SDFDatatype.FLOAT, SDFDatatype.DOUBLE } };

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
		return accTypes[argPos];
	}

}
