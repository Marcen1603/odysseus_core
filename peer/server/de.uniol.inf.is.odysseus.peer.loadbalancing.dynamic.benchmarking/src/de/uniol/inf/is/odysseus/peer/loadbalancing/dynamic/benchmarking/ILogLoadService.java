package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.benchmarking;

/**
 * Provides Methods to start logging of peer load in csv file.
 * @author Carsten Cordes
 *
 */
public interface ILogLoadService {
	/**
	 * Start Logging of Load in Csv file.
	 */
	public void startLogging();
	/**
	 * Stop Logging of Load in csv file.
	 */
	public void stopLogging();
}
