package de.uniol.inf.is.odysseus.probabilistic.common.simulation;

/**
 * 
 * A simulation function creates a value for a simulation (@link
 * {@link ISimulation}).
 * 
 * @author Christoph Schröer
 *
 * @param <H>
 *            type of the historical data to operate with in the function
 * @param <R>
 *            Return type of the function.
 */
public interface ISimulationFunction<H, R> {

	/**
	 * Function creates a value.
	 * 
	 * @param value
	 * @return
	 */
	public R getValue(H historicalValue);

}
