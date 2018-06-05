package de.uniol.inf.is.odysseus.incubation.crypt.client;

/**
 * Receiver if any keys, which may be loaded out of a vault
 * 
 * @author MarkMilster
 *
 */
public interface IKeyReceiver {

	/**
	 * Returns the receiver of the information of the key. In many cases, this
	 * is an object, which uses the key for crypting. This class only handles
	 * the distribution.
	 * 
	 * @return The Receiver
	 */
	public Object getReceiver();

}
