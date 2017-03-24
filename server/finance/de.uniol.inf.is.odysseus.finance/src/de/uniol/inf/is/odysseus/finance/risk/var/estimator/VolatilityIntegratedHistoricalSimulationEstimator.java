package de.uniol.inf.is.odysseus.finance.risk.var.estimator;

import java.util.List;

import org.apache.commons.math3.distribution.RealDistribution;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.finance.risk.var.model.HistoricalSimulationVaRForecaster;
import de.uniol.inf.is.odysseus.finance.simulation.VolatilityIntegratedSimulationFunction;
import de.uniol.inf.is.odysseus.probabilistic.common.simulation.HistoricalSimulation;
import de.uniol.inf.is.odysseus.probabilistic.common.simulation.ISimulation;
import de.uniol.inf.is.odysseus.probabilistic.common.simulation.ISimulationFunction;
import de.uniol.inf.is.odysseus.timeseries.IHasEstimationData;

/**
 * This class represents a estimator by historical simulation with a volatility
 * integrated simulation function.
 * 
 * @author Christoph Schröer
 *
 */
public class VolatilityIntegratedHistoricalSimulationEstimator extends HistoricalSimulationEstimator
		implements IHasEstimationData<Tuple<ITimeInterval>> {

	public static final String NAME = "VolatilityIntegratedHistoricalSimulationEstimator";

	private int returnIndex;

	private int volatilityIndex;

	public VolatilityIntegratedHistoricalSimulationEstimator(int returnIndex, int volatilityIndex) {
		super();
		this.returnIndex = returnIndex;
		this.volatilityIndex = volatilityIndex;
	}

	@Override
	public void estimateModel() {

		ISimulation<Tuple<ITimeInterval>, Double> numericalSimulator = new HistoricalSimulation(this.historicalData);

		// get newest estimated Volatility
		Tuple<ITimeInterval> newestTuple = null;
		if(this.historicalData.size() > 0){
			newestTuple = this.historicalData.get(this.historicalData.size() - 1);
		}

		if (newestTuple != null) {
			double volatilityT1 = newestTuple.getAttribute(this.volatilityIndex);

			ISimulationFunction<Tuple<ITimeInterval>, Double> simulationFunction = new VolatilityIntegratedSimulationFunction(
					this.returnIndex, this.volatilityIndex, volatilityT1);
			numericalSimulator.setSimulationFunction(simulationFunction);
			numericalSimulator.simulate();

			RealDistribution distribution = numericalSimulator.getEnumeratedDistribution();
			// Adding the simulated values to forecaster
			List<Double> sample = numericalSimulator.getSimulatedData();
			HistoricalSimulationVaRForecaster histVarForecaster = new HistoricalSimulationVaRForecaster();
			histVarForecaster.setSample(sample);
			this.varForecaster = histVarForecaster;
			this.varForecaster.setDistribution(distribution);
			this.isEstimated = true;
		} else {
			this.isEstimated = true;
		}
	}
}
