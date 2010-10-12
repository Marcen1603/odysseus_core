package de.uniol.inf.is.odysseus.monitoring;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPublisher<T> implements IPublisher<T> {

	protected List<ISubscriber<T>> subscribers = new ArrayList<ISubscriber<T>>();

	@Override
	public void subscribe(ISubscriber<T> subscriber) {
		synchronized (this.subscribers) {
			this.subscribers.add(subscriber);
		}
	}

	@Override
	public void unsubscribe(ISubscriber<T> subscriber) {
		synchronized (this.subscribers) {
			this.subscribers.remove(subscriber);
		}
	}

	@Override
	public void notifySubscribers(T value) {
		synchronized (subscribers) {
			for (ISubscriber<T> subscriber : this.subscribers) {
				subscriber.update(this, value);
			}	
		}
	}

	@Override
	public int subscribtionCount() {
		return this.subscribers.size();
	}
}
