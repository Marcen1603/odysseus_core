package de.uniol.inf.is.odysseus.monitoring;

public interface ISubscriber<T> {
	public void update(IPublisher<T> publisher, T value);
}
