package de.uniol.inf.is.odysseus.monitoring;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPublisher<T> implements IPublisher<T> {

	protected List<ISubscriber<T>> subscribers = new ArrayList<ISubscriber<T>>();

	public void subscribe(ISubscriber<T> subscriber) {
		synchronized (this.subscribers) {
			this.subscribers.add(subscriber);
		}
	}

	public void unsubscribe(ISubscriber<T> subscriber) {
		synchronized (this.subscribers) {
			this.subscribers.remove(subscriber);
		}
	}

	public void notifySubscribers(T value) {
		synchronized (subscribers) {
			for (ISubscriber<T> subscriber : this.subscribers) {
				subscriber.update(this, value);
			}	
		}
	}

	public int subscribtionCount() {
		return this.subscribers.size();
	}
}
