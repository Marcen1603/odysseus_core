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
public class ProbabilisticContinuousPlusNumberLHSOperator extends AbstractProbabilisticContinuousPlusNumberOperator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5223018724039314319L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.FLOAT, SDFDatatype.DOUBLE }, { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE } };

	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > (this.getArity() - 1)) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return ProbabilisticContinuousPlusNumberLHSOperator.accTypes[argPos];
	}

	@Override
	public boolean isCommutative() {
		return false;
	}

	@Override
	public NormalDistributionMixture getValue() {
		Double a = this.getNumericalInputValue(0);
		final NormalDistributionMixture b = this.getDistributions(((ProbabilisticContinuousDouble) this.getInputValue(1)).getDistribution());
		return getValueInternal(b, a);
	}
}
