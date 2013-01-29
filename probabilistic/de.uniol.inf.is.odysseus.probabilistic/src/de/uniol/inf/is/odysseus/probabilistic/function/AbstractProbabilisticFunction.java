package de.uniol.inf.is.odysseus.probabilistic.function;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.probabilistic.datatype.NormalDistributionMixture;

public abstract class AbstractProbabilisticFunction<T> extends
		AbstractFunction<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1726038091049996390L;
	private List<NormalDistributionMixture> distributions = new ArrayList<NormalDistributionMixture>();

	/**
	 * 
	 * @param distributionIndex
	 * @return
	 */
	public NormalDistributionMixture getDistributions(int distributionIndex) {
		return this.distributions.get(distributionIndex);
	}

	/**
	 * 
	 * @return
	 */
	public List<NormalDistributionMixture> getDistributions() {
		return this.distributions;
	}
}
