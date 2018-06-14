package de.uniol.inf.is.odysseus.badast.subscriber;

import java.nio.ByteBuffer;

/**
 * Interface for classes, which want to be fed with data consumed from a publish subscribe system (stored data stream elements).
 * 
 * @author Michael Brand
 *
 */
public interface ISubscriber {

	/**
	 * Called, if a new message is consumed.
	 * 
	 * @param message
	 *            The message as {@link ByteBuffer}.
	 * @param offset
	 *            The offset of the message.
	 * @throws Throwable
	 *             if any error occurs.
	 */
	public void onNewMessage(ByteBuffer message, long offset) throws Throwable;

}