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



import de.uniol.inf.is.odysseus.ac.IAdmissionControl;
import de.uniol.inf.is.odysseus.p2p.ac.bid.IP2PBidGenerator;
import de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.messagehandler.QueryBidResponseHandlerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.distribution.client.AbstractDistributionClient;
import de.uniol.inf.is.odysseus.p2p.distribution.client.IQuerySpecificationListener;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;

public class BiddingClient extends AbstractDistributionClient {

//	private IQuerySelectionStrategy querySelectionStrategy;
	private IMessageHandler queryResultHandler;
	
	private IQuerySpecificationListener<QueryExecutionSpezification> listener;
	private static IExecutor executor;
	private static IAdmissionControl admissionControl;
	private static IP2PBidGenerator bidGenerator;

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
		this.listener = new QuerySpecificationListenerJxtaImpl<QueryExecutionSpezification>(getPeer(), getPeer().getLog());
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

//	public void bindQuerySelectionStrategy(IQuerySelectionStrategy selection) {
//		getLogger().info("Binding Query Selection Strategy " + selection);
//		this.querySelectionStrategy = selection;
//	}
//
//	public void unbindQuerySelectionStrategy(IQuerySelectionStrategy selection) {
//		if (this.querySelectionStrategy == selection) {
//			getLogger().info("Unbinding Query Selection Strategy " + selection);
//			this.querySelectionStrategy = null;
//		}
//	}

//	@Override
//	public IQuerySelectionStrategy getQuerySelectionStrategy() {
//		return this.querySelectionStrategy;
//	}
	
	public void bindExecutor( IExecutor ex ) {
		executor = ex;
		System.err.println("Executor bound");
	}
	
	public void unbindExecutor( IExecutor ex ) {
		if( executor == ex ) {
			executor = null;
			System.err.println("Executor unbound");
		}
	}
	
	public static IExecutor getExecutor() {
		return executor;
	}
	
	public void bindAdmissionControl( IAdmissionControl ac ) {
		admissionControl = ac;
		System.err.println("AC bound");
	}
	
	public void unbindAdmissionControl( IAdmissionControl ac ) {
		if( admissionControl == ac ) {
			admissionControl = null;
			System.err.println("AC unbound");
		}
	}
	
	public static IAdmissionControl getAdmissionControl() {
		return admissionControl;
	}
	
	public void bindBidGenerator( IP2PBidGenerator gen ) {
		bidGenerator = gen;
		System.err.println("BidGenerator bound");
	}
	
	public void unbindBidGenerator( IP2PBidGenerator gen ) {
		if( bidGenerator == gen ) {
			bidGenerator = null;
			System.err.println("BidGenerator unbound");
		}
	}
	
	public static IP2PBidGenerator getBidGenerator() {
		return bidGenerator;
	}

}
