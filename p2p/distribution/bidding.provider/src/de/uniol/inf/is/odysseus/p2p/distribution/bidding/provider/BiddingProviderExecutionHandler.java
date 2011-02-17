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
package de.uniol.inf.is.odysseus.p2p.distribution.bidding.provider;

import net.jxta.protocol.PipeAdvertisement;
import de.uniol.inf.is.odysseus.p2p.distribution.provider.AbstractDistributionProvider;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class BiddingProviderExecutionHandler
		extends
		AbstractExecutionHandler<AbstractDistributionProvider<? super PipeAdvertisement>> {

	public BiddingProviderExecutionHandler(Lifecycle lifecycle,
			AbstractDistributionProvider<? super PipeAdvertisement> function,
			IOdysseusPeer peer) {
		super(lifecycle, function, peer);
	}

	public BiddingProviderExecutionHandler(
			BiddingProviderExecutionHandler biddingProviderExecutionHandler) {
		super(biddingProviderExecutionHandler);
	}

	@Override
	public void run() {
		if (getExecutionListenerCallback() != null) {
			getFunction().setCallback(getExecutionListenerCallback());
			getFunction().distributePlan(
					getExecutionListenerCallback().getQuery(),
					(PipeAdvertisement) getPeer().getServerResponseAddress());
		}
	}

	@Override
	public synchronized BiddingProviderExecutionHandler clone() {
		return new BiddingProviderExecutionHandler(this);
	}

}
