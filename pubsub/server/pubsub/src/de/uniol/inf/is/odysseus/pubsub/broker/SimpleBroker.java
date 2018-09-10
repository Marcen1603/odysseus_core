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

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * This class provides the functionality of a simple broker with no routing
 * needed in single broker or bus broker topology
 * 
 * Hint: All functions are in abstract broker, because they are also needed in
 * other brokers
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class SimpleBroker<T extends IStreamObject<?>> extends AbstractBroker<T> {

	public SimpleBroker(String name, String domain) {
		super(name, domain);
	}

	public SimpleBroker(String name, SimpleBroker<T> copy) {
		// Copy data
		super(name, copy.getDomain());
		getAdvertisements().putAll(copy.getAdvertisements());

		// Initialize Broker
		refreshInternalStatus();
	}

}
