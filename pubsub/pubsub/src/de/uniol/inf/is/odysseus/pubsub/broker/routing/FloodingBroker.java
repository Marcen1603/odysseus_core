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

package de.uniol.inf.is.odysseus.pubsub.broker.routing;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;

public class FloodingBroker <T extends IStreamObject<?>> extends AbstractRoutingBroker<T>{

	private static final String ROUTING_TYPE = "Flooding";
	private List<FloodingBroker<T>> connectedBrokers = new ArrayList<FloodingBroker<T>>();
	
	public FloodingBroker() {
		// needed for OSGi
		super("", "");
	}
	
	public FloodingBroker(String name, String domain) {
		super(name, domain);
	}

	public List<FloodingBroker<T>> getConnectedBrokers() {
		return connectedBrokers;
	}

	@Override
	public String getType() {
		return ROUTING_TYPE;
	}

	@Override
	public IRoutingBroker<T> getInstance(String name, String domain) {
		return new FloodingBroker<T>(name, domain);
	}
	
	
	@Override
	public void routeToSubscribers(T object, PublishPO<T> publisher) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void route() {
		// TODO Route Logic
		
	}

	@Override
	protected void refreshInternalStatus() {
		// TODO Auto-generated method stub
		
	}
}
