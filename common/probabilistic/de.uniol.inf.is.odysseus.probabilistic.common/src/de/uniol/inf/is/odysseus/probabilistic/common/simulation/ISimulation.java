package de.uniol.inf.is.odysseus.probabilistic.common.simulation;

import java.util.List;

import org.apache.commons.math3.distribution.RealDistribution;

/**
 * 
 * This interface describes a simulation. A Simulation creates a set of data,
 * which describes a discrete distribution.
 * 
 * Each value of the simulation set is calculated by a simulation function.
 * 
 * @author Christoph Schröer
 *
 * @param <H>
 *            Type of historical data to operate with
 * @param <T>
 *            Type of simulated data
 */
public interface ISimulation<H, T> {

	/**
	 * This method starts the simulation.
	 */
	public void simulate();

	public void setSimulationFunction(ISimulationFunction<H, T> simulationFunction);

	/**
	 * Complete list with simulated data.
	 * 
	 * @return
	 */
	public List<T> getSimulatedData();

	/**
	 * A Simulation can also return a distribution with discrete values.
	 * 
	 * @return
	 */
	public RealDistribution getEnumeratedDistribution();

}
