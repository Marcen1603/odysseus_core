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

package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerAdvertisements;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerSubscription;

/**
 * Interface for filtering algorithms
 * 
 * @author ChrisToenjesDeye
 *
 */
public interface IFiltering<T extends IStreamObject<?>> {

	/**
	 * Initialize and optimize Filter for better performance
	 * 
	 * @param subscriptions
	 * @param advertisements
	 */
	void reinitializeFilter(Collection<BrokerSubscription<T>> subscriptions, Collection<BrokerAdvertisements> advertisements);
	
	/**
	 * Returns the subscribers are interested on object
	 * 
	 * @param object
	 * @param publisher
	 */
	List<String> filter(T object, String publisherUid);

	/**
	 * Change reinitialization mode, if set to true, on next filter() 
	 * @param mode
	 */
	void setReinitializationMode(boolean mode);

	/**
	 * Returns if Filter need to be reinitialized
	 * @return
	 */
	boolean needsReinitialization();
}
