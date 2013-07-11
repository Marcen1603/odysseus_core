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
public class ProbabilisticContinuousPlusNumberRHSOperator extends AbstractProbabilisticContinuousPlusNumberOperator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7844361382196518561L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE }, { SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.FLOAT, SDFDatatype.DOUBLE } };

	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > (this.getArity() - 1)) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return ProbabilisticContinuousPlusNumberRHSOperator.accTypes[argPos];
	}

	@Override
	public boolean isCommutative() {
		return false;
	}

	@Override
	public NormalDistributionMixture getValue() {
		final NormalDistributionMixture a = (NormalDistributionMixture) this.getInputValue(0);
		Double b = this.getNumericalInputValue(1);
		return getValueInternal(a, b);
	}
}
