package de.uniol.inf.is.odysseus.probabilistic.continuous.base.predicate;

import de.uniol.inf.is.odysseus.probabilistic.base.predicate.IProbabilisticPredicateResult;
import de.uniol.inf.is.odysseus.probabilistic.continuous.datatype.NormalDistributionMixture;

public class ProbabilisticContinuousPredicateResult implements IProbabilisticPredicateResult<NormalDistributionMixture> {
	private final double probability;
	private final NormalDistributionMixture value;

	public ProbabilisticContinuousPredicateResult(final double probability, final NormalDistributionMixture value) {
		this.probability = probability;
		this.value = value;
	}

	@Override
	public double getProbability() {
		return this.probability;
	}

	@Override
	public NormalDistributionMixture getValue() {
		return this.value;
	}

}
