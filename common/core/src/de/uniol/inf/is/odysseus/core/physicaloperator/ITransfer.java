package de.uniol.inf.is.odysseus.core.physicaloperator;


public interface ITransfer<T> {
	
	/**
	 * Calls {@link ISink#process(T)} on all subscribed {@link ISink sinks}.
	 * 
	 * @param object
	 *            the parameter for processNext.
	 */
	public void transfer(T object, int sourceOutPort);

	public void transfer(T object);

//	/**
//	 * Same as above, but for transfering a batch of elements.
//	 */
//	public void transfer(Collection<T> object, int sourceOutPort);
//
//	public void transfer(Collection<T> object);
	
	public void sendPunctuation(IPunctuation punctuation);

	public void sendPunctuation(IPunctuation punctuation, int outPort);

}
