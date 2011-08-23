/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.p2p.distribution.client;

import de.uniol.inf.is.odysseus.p2p.peer.AbstractOdysseusPeer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;

public abstract class AbstractDistributionClient implements IDistributionClient {

	private AbstractOdysseusPeer peer;
	static private Logger logger = LoggerFactory
			.getLogger(AbstractDistributionClient.class);;

	public AbstractOdysseusPeer getPeer() {
		return peer;
	}

//	@Override
//	public abstract String getDistributionStrategy();

	@Override
	public void setPeer(IOdysseusPeer peer) {
		this.peer = (AbstractOdysseusPeer) peer;
	}

	@Override
	public abstract void initializeService();

	@Override
	public abstract void startService();

	public Logger getLogger() {
		return logger;
	}

	@Override
	public abstract IQuerySpecificationListener<?> getQuerySpecificationListener();

}
