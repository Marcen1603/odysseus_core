package de.uniol.inf.is.odysseus.recovery.incomingelements;

/**
 * Consumes data, which is stored on a publish subscribe syste,. <br />
 * <br />
 * In the context of this recovery component, this are stored elements of a
 * certain data stream (source name = topic). <br />
 * <br />
 * Create a new subscriber and call {@link #start()} afterwards. <br />
 * <br />
 * Based on example from
 * https://cwiki.apache.org/confluence/display/KAFKA/0.8.0+SimpleConsumer+
 * Example
 * 
 * @author Michael Brand
 *
 */
public interface ISubscriberController {

	/**
	 * Starts the consumption.
	 */
	public void start();

	/**
	 * Stops the consumption.
	 */
	public void interrupt();

}