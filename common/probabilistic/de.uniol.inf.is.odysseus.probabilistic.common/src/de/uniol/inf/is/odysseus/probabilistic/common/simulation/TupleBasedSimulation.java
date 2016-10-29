package de.uniol.inf.is.odysseus.probabilistic.common.simulation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.EnumeratedRealDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

/**
 * 
 * Abstract class, which operates with Tuple objects
 * 
 * @author Christoph Schröer
 *
 * @param <H> 
 * @param <T>
 * @param <M>
 */
abstract public class TupleBasedSimulation<H extends Tuple<M>, M extends ITimeInterval>
		implements ISimulation<H, Double> {

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

		RealDistribution discreteDistribution = new EnumeratedRealDistribution(inputData, inputProbabilities);

		return discreteDistribution;
	}

}
