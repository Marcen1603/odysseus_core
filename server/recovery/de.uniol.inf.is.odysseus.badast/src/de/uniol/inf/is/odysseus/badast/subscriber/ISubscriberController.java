package de.uniol.inf.is.odysseus.badast.subscriber;

/**
 * Consumes data, which is stored on a publish subscribe system. <br />
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
	 * Creates a new subscriber controller.
	 * 
	 * @param stream
	 *            Stream to read from (source name).
	 * @param partition
	 *            Partition to read from (default 0).
	 * @param subscriber
	 *            The subscriber to be updated while reading.
	 * @param offset
	 *            The offset from where to read.
	 */
	public ISubscriberController newInstance(String stream, int partition, ISubscriber subscriber, long offset);

	/**
	 * Starts the consumption.
	 */
	public void start();

	/**
	 * Stops the consumption.
	 */
	public void interrupt();

}