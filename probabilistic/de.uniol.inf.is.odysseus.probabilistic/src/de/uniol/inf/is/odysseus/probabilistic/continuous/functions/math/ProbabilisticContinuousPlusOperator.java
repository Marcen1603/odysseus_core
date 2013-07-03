package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.IOperator;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticBinaryOperator;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

public class ProbabilisticContinuousPlusOperator extends AbstractProbabilisticBinaryOperator<NormalDistributionMixture> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2533914833718506956L;

	@Override
	public int getPrecedence() {
		return 6;
	}

	@Override
	public String getSymbol() {
		return "+";
	}

	@Override
	public NormalDistributionMixture getValue() {
		NormalDistributionMixture a = getDistributions(((ProbabilisticContinuousDouble) getInputValue(0)).getDistribution());
		NormalDistributionMixture b = getDistributions(((ProbabilisticContinuousDouble) getInputValue(1)).getDistribution());

		// return getValueInternal(a, b);
		throw new RuntimeException("Operator (" + getSymbol() + ") not implemented");
	}

	protected NormalDistributionMixture getValueInternal(NormalDistributionMixture a, NormalDistributionMixture b) {
		return null;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.mep.IOperator.ASSOCIATIVITY getAssociativity() {
		return ASSOCIATIVITY.LEFT_TO_RIGHT;
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
	public boolean isLeftDistributiveWith(IOperator<NormalDistributionMixture> operator) {
		return false;
	}

	@Override
	public boolean isRightDistributiveWith(IOperator<NormalDistributionMixture> operator) {
		return false;
	}

	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE };

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > this.getArity() - 1) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return accTypes;
	}

}
