package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;
import de.uniol.inf.is.odysseus.p2p.distribution.client.IQuerySpecificationHandler;
import de.uniol.inf.is.odysseus.p2p.distribution.client.IQuerySpecificationListener;
import de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection.IQuerySelectionStrategy;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;

public class QuerySpecificationListenerJxtaImpl<S extends QueryExecutionSpezification> implements
		IQuerySpecificationListener<S>, DiscoveryListener {
	private List<QueryExecutionSpezification> specifications;
	private AbstractPeer aPeer;
	private IQuerySelectionStrategy selectionStrategy;
	
	public QuerySpecificationListenerJxtaImpl(AbstractPeer aPeer, IQuerySelectionStrategy strategy) {
		PeerGroupTool.getPeerGroup().getDiscoveryService().addDiscoveryListener(this);
		this.aPeer = aPeer;
		this.selectionStrategy = strategy;
		this.specifications = new ArrayList<QueryExecutionSpezification>();
	}

	protected List<QueryExecutionSpezification> getSpecifications() {
		return specifications;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
			}
			PeerGroupTool.getPeerGroup().getDiscoveryService().getRemoteAdvertisements(null, DiscoveryService.ADV,
							"type", "QueryExecutionAdv", 6, null);
		}
	}

	@Override
	public synchronized void discoveryEvent(DiscoveryEvent ev) {


		DiscoveryResponseMsg res = ev.getResponse();
		Enumeration<Advertisement> en = res.getAdvertisements();
		if (en != null) {
			while (en.hasMoreElements()) {

				S temp2 = (S) en.nextElement();
				if (temp2 instanceof QueryExecutionSpezification) {
					QueryExecutionSpezification spec = temp2;
					boolean createHandler = true;
					System.out.println("QueryExecutionSpezifikation Anfrage: "+spec.getQueryId()+" Subplan:" +spec.getSubplanId());
					for(QueryExecutionSpezification s : getSpecifications()) {
						if(s.getSubplanId().equals(spec.getSubplanId()))
						{
							
							createHandler = false;
							break;
						}
					}
//					if(!this.qes.containsKey(((QueryExecutionSpezification)temp2).getID().toString())) {
//						this.qes.put(((QueryExecutionSpezification)temp2).getID().toString(), ((QueryExecutionSpezification)temp2));
					
//					new QuerySpecificationHandlerJxtaImpl<S>(temp2, getaPeer(), getSelectionStrategy()) ;
					if(createHandler) {
						try {
							getSpecifications().add(spec);
							System.out.println("Erzeuge SpecificationHandler zu: "+spec.getQueryId());
							getQuerySpecificationHandler((S) spec);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
//					}
				} else {
					return;
				}
			}

		}

	}
	
	public AbstractPeer getaPeer() {
		return aPeer;
	}
	
	public IQuerySelectionStrategy getSelectionStrategy() {
		return selectionStrategy;
	}

	@Override
	public void startListener() {
		Thread t = new Thread(this);
		t.start();
		
	}

	@Override
	public IQuerySpecificationHandler<S> getQuerySpecificationHandler(S spec) {
		return new QuerySpecificationHandlerJxtaImpl<S>(spec, getaPeer(), getSelectionStrategy(), specifications);
	}

	@Override
	public IQuerySelectionStrategy getQuerySelectionStrategy() {
		return this.selectionStrategy;
	}
	

}
