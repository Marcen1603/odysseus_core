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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.broker.BrokerSubscription;

/**
 * provides additional functionality for channel based filtering
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ChannelBasedFiltering<T extends IStreamObject<?>> extends
		AbstractTopicBasedFiltering<T> {

	/**
	 * checks if a given subscription contains a given topic (not hierarchical)
	 */
	@Override
	protected boolean hasSubscriptionTopic(Topic topic,
			BrokerSubscription<T> subscription) {
		for (Topic subscriptionTopic : subscription.getTopics()) {
			if (subscriptionTopic.equals(topic)) {
				return true;
			}
		}
		return false;
	}
}
