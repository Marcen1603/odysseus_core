package de.uniol.inf.is.odysseus.monitoring;

public interface IPublisher<T> {
	public void subscribe(ISubscriber<T> subscriber);
	public void unsubscribe(ISubscriber<T> subscriber);
	public void notifySubscribers(T value);
	public int subscribtionCount();
}
