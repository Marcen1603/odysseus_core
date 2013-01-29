package de.uniol.inf.is.odysseus.probabilistic.function;

import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;

public abstract class AbstractProbabilisticFunction<T> extends
		AbstractFunction<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1726038091049996390L;
	private NormalDistributionMixture[] distributions;

	/**
	 * 
	 * @param distributions
	 */
	public void setDistributions(NormalDistributionMixture[] distributions) {
		this.distributions = distributions;
	}

	/**
	 * 
	 * @param distributionIndex
	 * @return
	 */
	public NormalDistributionMixture getDistributions(int distributionIndex) {
		return this.distributions[distributionIndex];
	}
}
