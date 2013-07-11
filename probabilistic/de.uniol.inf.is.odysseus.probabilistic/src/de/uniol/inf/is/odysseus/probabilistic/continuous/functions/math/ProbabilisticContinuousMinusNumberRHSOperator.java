package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousMinusNumberRHSOperator extends AbstractProbabilisticContinuousMinusNumberOperator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6021986662578189660L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE }, { SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.FLOAT, SDFDatatype.DOUBLE } };

	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > (this.getArity() - 1)) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return ProbabilisticContinuousMinusNumberRHSOperator.accTypes[argPos];
	}

	@Override
	public boolean isCommutative() {
		return false;
	}

	@Override
	public NormalDistributionMixture getValue() {
		final NormalDistributionMixture a = this.getDistributions(((ProbabilisticContinuousDouble) this.getInputValue(0)).getDistribution());
		Double b = this.getNumericalInputValue(1);
		return getValueInternal(a, b);
	}
}
