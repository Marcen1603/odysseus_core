package de.uniol.inf.is.odysseus.core.physicaloperator;

import de.uniol.inf.is.odysseus.core.IHasName;

public interface ITransfer<T> extends IHasName{

	/**
	 * Calls {@link ISink#process(T)} on all subscribed {@link ISink sinks}.
	 *
	 * @param object
	 *            the parameter for processNext.
	 */
	void transfer(T object, int sourceOutPort);

	void transfer(T object);

	void propagateDone();

	boolean isDone();

	void sendPunctuation(IPunctuation punctuation);

	void sendPunctuation(IPunctuation punctuation, int outPort);

	@Override
	String getName();

}
