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
package de.uniol.inf.is.odysseus.p2p.distribution.provider;

import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.clientselection.IClientSelectorFactory;

public interface IDistributionProvider<R> {
	public void initializeService();
	public void finalizeService();
	public void startService();
	public String getDistributionStrategy();
	public void setPeer(IOdysseusPeer peer);
	public void distributePlan(P2PQuery q, R serverResponse);
	public IClientSelectorFactory<?> getClientSelectorFactory();
}
