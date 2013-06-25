package de.uniol.inf.is.odysseus.pubsub.observer;

import java.util.Observable;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;

public class PublisherObservable<T extends IStreamObject<?>> extends Observable{

	private PublishPO<T> publisher;

	public PublisherObservable(PublishPO<T> publisher){
		this.publisher = publisher;	
	}
	
	public PublishPO<T> getPublisher() {
		return publisher;
	}
	
	public void setElement(T object){
		super.setChanged();
		super.notifyObservers(object);
	}

}
