package de.uniol.inf.is.odysseus.core.physicaloperator;


public interface ITransfer<T> {
	
	/**
	 * Calls {@link ISink#process(T)} on all subscribed {@link ISink sinks}.
	 * 
	 * @param object
	 *            the parameter for processNext.
	 */
	void transfer(T object, int sourceOutPort);

	void transfer(T object);
	
	void sendPunctuation(IPunctuation punctuation);

	void sendPunctuation(IPunctuation punctuation, int outPort);
	
	String getName();

}
