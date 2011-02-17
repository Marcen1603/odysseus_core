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
package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client;



import de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.messagehandler.QueryBidResponseHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.distribution.client.AbstractDistributionClient;
import de.uniol.inf.is.odysseus.p2p.distribution.client.IQuerySpecificationListener;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;

public class BiddingClient extends AbstractDistributionClient {
	private IMessageHandler queryResultHandler;
	private IQuerySpecificationListener<QueryExecutionSpezification> listener;

	public BiddingClient() {
	}
	
	@Override
	public String getDistributionStrategy() {
		return "BiddingClient";
	}

	@Override
	public void initializeService() {
		initQueryResultHandler();
		
	}

	private void startQuerySpezificationListener(){
		this.listener = new QuerySpecificationListenerJxtaImpl<QueryExecutionSpezification>(getPeer(), getQuerySelectionStrategy(), getPeer().getLog());
	}

	private void initQueryResultHandler() {
		this.queryResultHandler = new QueryBidResponseHandlerJxtaImpl(getPeer(), getPeer().getLog());
		getPeer().registerMessageHandler(this.queryResultHandler);
		
	}

	@Override
	public void startService() {
		startQuerySpezificationListener();
		Thread t = new Thread(getQuerySpecificationListener());
		t.start();
	}

	@Override
	public IQuerySpecificationListener<QueryExecutionSpezification> getQuerySpecificationListener() {
		return listener;
	}


}
