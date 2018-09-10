/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a joinPlan of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.pubsub.observer;

import java.util.Observable;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * This class provides the observable functionality for the publish operator.
 * The class is needed because PublishPO cannot extend Observable (multi
 * inheritance is not possible in Java)
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class PublisherObservable<T extends IStreamObject<?>> extends Observable {

	private String publisherUid;

	public PublisherObservable(String publisherUid) {
		this.publisherUid = publisherUid;
	}

	public String getPublisherUid() {
		return publisherUid;
	}

	/**
	 * method for notifying all Observers
	 * @param object
	 */
	public void setElement(T object) {
		super.setChanged();
		super.notifyObservers(object);
	}

}
