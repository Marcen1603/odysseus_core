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
public class ProbabilisticDivisionNumberLHSOperator extends
		ProbabilisticDivisionOperator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7306731435009999772L;

	@Override
	public ProbabilisticDouble getValue() {
		ProbabilisticDouble a = new ProbabilisticDouble(
				getNumericalInputValue(0), 1.0);
		AbstractProbabilisticValue<?> b = getInputValue(1);
		return getValueInternal(a, b);
	}

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER,
					SDFDatatype.LONG, SDFDatatype.FLOAT, SDFDatatype.DOUBLE },
			{ SDFProbabilisticDatatype.PROBABILISTIC_BYTE,
					SDFProbabilisticDatatype.PROBABILISTIC_SHORT,
					SDFProbabilisticDatatype.PROBABILISTIC_INTEGER,
					SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
					SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE,
					SDFProbabilisticDatatype.PROBABILISTIC_LONG } };

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
