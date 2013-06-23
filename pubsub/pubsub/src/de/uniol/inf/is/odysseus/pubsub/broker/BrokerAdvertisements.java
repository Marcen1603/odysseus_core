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

package de.uniol.inf.is.odysseus.pubsub.broker;

import java.util.List;

import de.uniol.inf.is.odysseus.pubsub.broker.filter.Topic;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;

public class BrokerAdvertisements {

	private PublishPO<?> publisher;

	private List<Topic> topics;

	public BrokerAdvertisements(PublishPO<?> publisher, List<Topic> topics){
		this.publisher = publisher;
		this.topics = topics;
	}
	
	public BrokerAdvertisements(PublishPO<?> publisher){
		this.publisher = publisher;
		this.topics = publisher.getTopics();
	}
	
	public boolean hasTopics(){
		if (!topics.isEmpty())
			return true;
		else
			return false;
	}
	
	public PublishPO<?> getPublisher() {
		return publisher;
	}

	public void setPublisher(PublishPO<?> publisher) {
		this.publisher = publisher;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

}
