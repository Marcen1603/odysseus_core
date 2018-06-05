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

package de.uniol.inf.is.odysseus.pubsub.test.broker.topology;

import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.BrokerTopologyRegistry;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.BusBrokerTopology;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.IBrokerTopology;
import de.uniol.inf.is.odysseus.pubsub.broker.topology.SingleBrokerTopology;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;

/**
 * Tests for BrokerTopologyRegistry
 * 
 * @author ChrisToenjesDeye
 * 
 */
public class BrokerTopologyRegistryTest<T extends IStreamObject<?>> {

	@BeforeTest
	public void initializeRegistry() {
		BrokerTopologyRegistry
				.registerBrokertopologies(new SingleBrokerTopology<T>());
		BrokerTopologyRegistry
				.registerBrokertopologies(new BusBrokerTopology<T>());
	}

	@Test
	public void testCreateMultipleDomains() {
		IBrokerTopology<?> topology1 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		IBrokerTopology<?> topology2 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain2", "");
		Assert.assertEquals("domain1", topology1.getDomain());
		Assert.assertEquals("domain2", topology2.getDomain());

	}

	@Test
	public void testCreateMultipleDomains2() {
		IBrokerTopology<?> topology1 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		IBrokerTopology<?> topology2 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("BusTopology", "domain2", "");
		Assert.assertEquals("domain1", topology1.getDomain());
		Assert.assertEquals("domain2", topology2.getDomain());
	}
	
	@Test
	public void testEqualDomainEqualType(){
		IBrokerTopology<?> topology1 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		IBrokerTopology<?> topology2 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		Assert.assertNotNull(topology1);
		Assert.assertNotNull(topology2);
	}
	
	@Test
	public void testCreateAndGetTopology() {
		IBrokerTopology<?> topology1 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		IBrokerTopology<?> topology2 = BrokerTopologyRegistry
				.getTopologyByDomain("domain1");
		Assert.assertEquals("domain1", topology1.getDomain());
		Assert.assertEquals("domain1", topology2.getDomain());
	}
	
	@Test
	public void testTopologyNotExists() {
		BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		IBrokerTopology<?> topology1 = BrokerTopologyRegistry
				.getTopologyByDomain("domain2");
		Assert.assertNull(topology1);
	}
	
	@Test
	public void testEqualDomainDifferentType() {
		IBrokerTopology<?> topology1 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		IBrokerTopology<?> topology2 = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("BusTopology", "domain1", "");
		Assert.assertNotNull(topology1);
		Assert.assertNull(topology2);
	}
	
	@Test
	public void testSubscribeBeforePublish() {
		IBrokerTopology<?> topology = BrokerTopologyRegistry
				.getTopologyByDomain("domain1");
		Assert.assertNull(topology);
		BrokerTopologyRegistry.putSubscriberIntoPendingList("domain1", new SubscribePO<T>(new ArrayList<IPredicate<? super T>>(), true, new ArrayList<String>(), "domain1"));
		
		topology = BrokerTopologyRegistry
				.getTopologyByDomain("domain1");
		Assert.assertNull(topology);
		
		topology = BrokerTopologyRegistry
				.getTopologyByTypeAndDomain("SingleBroker", "domain1", "");
		Assert.assertNotNull(topology);
		Assert.assertTrue(topology.hasAgents());
		
	}

	@AfterMethod
	public void cleanRegistry() {
		// Topologies need to be removed
		BrokerTopologyRegistry.unregister("domain1");
		BrokerTopologyRegistry.unregister("domain2");
	}

}
