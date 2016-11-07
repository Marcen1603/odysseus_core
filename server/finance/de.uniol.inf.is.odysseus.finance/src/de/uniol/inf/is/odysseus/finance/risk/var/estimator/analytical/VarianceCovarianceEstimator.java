package de.uniol.inf.is.odysseus.finance.risk.var.estimator.analytical;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.finance.risk.var.model.IVaRForecaster;
import de.uniol.inf.is.odysseus.finance.risk.var.model.analytical.VarianceCovarianceVaRForecaster;
import de.uniol.inf.is.odysseus.timeseries.IHasEstimationData;

/**
 * 
 * The VarianceCovariance Estimator creates an VarianceCovarianceForecaster by a
 * given distribution (e.g. NormalDistribution)
 * 
 * @author Christoph Schröer
 *
 */
public class VarianceCovarianceEstimator extends AnalyticalEstimator
		implements IHasEstimationData<Tuple<ITimeInterval>> {

	public static final String NAME = "VarianceCovarianceEstimator";

	private int volatilityIndex;

	private Double currentVolatility;

	private RealDistribution distribution;

	public VarianceCovarianceEstimator(int volatilityIndex) {
		super("NormalDistribution");
		this.volatilityIndex = volatilityIndex;

		// one constant distribution
		this.distribution = this.getDistributionByName(this.distributionName);
	}

	public VarianceCovarianceEstimator(String distributionName, int volatilityIndex) {
		super(distributionName);
		this.volatilityIndex = volatilityIndex;
		this.distribution = this.getDistributionByName(this.distributionName);
	}

	@Override
	public void estimateModel() {

		IVaRForecaster forecaster = new VarianceCovarianceVaRForecaster(currentVolatility);
		forecaster.setDistribution(this.distribution);

		this.varForecaster = forecaster;

	}

	@Override
	public void addEstimationData(Tuple<ITimeInterval> data) {
		this.currentVolatility = data.getAttribute(this.volatilityIndex);
	}

	@Override
	public void removeEstimationData(Tuple<ITimeInterval> data) {
		// nothing todo
	}

	@Override
	public void clear() {
		this.currentVolatility = null;
	}

	private RealDistribution getDistributionByName(String distributionNameLocal) {

		switch (distributionNameLocal) {
		case "NormalDistribution":

			// Standard Normal distribution;
			return new NormalDistribution(0, 1);
		}
		throw new IllegalArgumentException("Distribution is not yet implemented");
	}

}
