package de.uniol.inf.is.odysseus.badast;

/**
 * Interface for publish subscribe systems that can be used by BaDaSt.
 * 
 * @author Michael Brand
 *
 */
public interface IPubSubSystem extends Runnable {

	/**
	 * See {@link Thread#interrupt()}
	 */
	public void interrupt();

	/**
	 * Creates a new instance.
	 */
	public IPubSubSystem newInstance();

}