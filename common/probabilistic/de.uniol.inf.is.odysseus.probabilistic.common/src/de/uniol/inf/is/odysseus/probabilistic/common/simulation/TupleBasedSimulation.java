package de.uniol.inf.is.odysseus.probabilistic.common.simulation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.EnumeratedRealDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/**
 * 
 * Abstract class, which operates with Tuple objects
 * 
 * @author Christoph Schröer
 *
 * @param <H> Type of the historical data
 * @param <M> Type of the metadata
 */
abstract public class TupleBasedSimulation<H extends Tuple<M>, M extends ITimeInterval>
		implements ISimulation<H, Double> {

	protected static Logger logger = LoggerFactory.getLogger(TupleBasedSimulation.class);

	protected List<Double> simulatedData = new ArrayList<>();

	protected ISimulationFunction<H, Double> simulationFunction;

	@Override
	public void setSimulationFunction(ISimulationFunction<H, Double> simulationFunction) {
		this.simulationFunction = simulationFunction;

	}

	@Override
	public List<Double> getSimulatedData() {
		return this.simulatedData;
	}

	@Override
	public RealDistribution getEnumeratedDistribution() {

		double[] inputData = new double[this.simulatedData.size()];
		double[] inputProbabilities = new double[this.simulatedData.size()];

		Double probability = 1.0 / this.simulatedData.size();

		int index = 0;
		for (Double simulatedTuple : this.simulatedData) {
			// double k0 = (double)
			// simulatedTuple.getAttribute(simulatedTuple.getAttributes().length
			// - 1); // default

			inputData[index] = simulatedTuple;
			inputProbabilities[index] = probability;
			index++;
		}

		RealDistribution discreteDistribution = null;
		discreteDistribution = new EnumeratedRealDistribution(inputData, inputProbabilities);

		return discreteDistribution;
	}

}
