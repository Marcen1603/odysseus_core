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
package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.strategy;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.IThinPeerBiddingStrategy;
import de.uniol.inf.is.odysseus.p2p.peer.IQueryProvider;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class MaxQueryBiddingStrategyJxtaImpl implements IThinPeerBiddingStrategy {
	// Es soll nur geboten werden, wenn die Anzahl an Anfragen die schon
	// ueberwacht werden und die Anzahl an
	// Gebote die noch offen sind, kleiner 5 sind.
	private static int MAX_QUERYS = 10;
	final private IQueryProvider queryProvider;
	final private List<Lifecycle> interestedLifecycles;
	
	public MaxQueryBiddingStrategyJxtaImpl(IQueryProvider queryProvider) {
		this.queryProvider = queryProvider;
		interestedLifecycles = new ArrayList<Lifecycle>();
		interestedLifecycles.add(Lifecycle.DISTRIBUTION);
		interestedLifecycles.add(Lifecycle.RUNNING);
	}
	
	@Override
	public boolean bidding(P2PQuery q) {

			int i = queryProvider.getQueryCount(interestedLifecycles);
			if (i >= MAX_QUERYS) {
				return false;
			} else {
				return true;
			}
	}

}
