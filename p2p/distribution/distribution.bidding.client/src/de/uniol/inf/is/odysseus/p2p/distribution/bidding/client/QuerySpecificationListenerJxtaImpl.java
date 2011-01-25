package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import net.jxta.discovery.DiscoveryEvent;
import net.jxta.discovery.DiscoveryListener;
import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.protocol.DiscoveryResponseMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p.distribution.client.IQuerySpecificationHandler;
import de.uniol.inf.is.odysseus.p2p.distribution.client.IQuerySpecificationListener;
import de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection.IQuerySelectionStrategy;
import de.uniol.inf.is.odysseus.p2p.jxta.advertisements.QueryExecutionSpezification;
import de.uniol.inf.is.odysseus.p2p.jxta.utils.PeerGroupTool;
import de.uniol.inf.is.odysseus.p2p.peer.ILogListener;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;

public class QuerySpecificationListenerJxtaImpl<S extends QueryExecutionSpezification> implements
		IQuerySpecificationListener<S>, DiscoveryListener {
	
	static Logger logger = LoggerFactory.getLogger(QuerySpecificationListenerJxtaImpl.class);
	
	private List<QueryExecutionSpezification> specifications;
	private IOdysseusPeer peer;
	private IQuerySelectionStrategy selectionStrategy;
	
	public QuerySpecificationListenerJxtaImpl(IOdysseusPeer peer, IQuerySelectionStrategy strategy, ILogListener log) {
		PeerGroupTool.getPeerGroup().getDiscoveryService().addDiscoveryListener(this);
		this.peer = peer;
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
	// This method is called if a query specification is found
	public synchronized void discoveryEvent(DiscoveryEvent ev) {

		DiscoveryResponseMsg res = ev.getResponse();
		Enumeration<Advertisement> en = res.getAdvertisements();
		if (en != null) {
			while (en.hasMoreElements()) {

				Advertisement qESp = en.nextElement();
				if (qESp instanceof QueryExecutionSpezification) {
					QueryExecutionSpezification spec = (QueryExecutionSpezification) qESp;
					boolean createHandler = true;
					logger.debug("QueryExecutionSpezifikation Anfrage: "+spec.getQueryId()+" Subplan:" +spec.getSubplanId());
					
					for(QueryExecutionSpezification s : getSpecifications()) {
						if(s.getSubplanId().equals(spec.getSubplanId()))
						{
							
							createHandler = false;
							break;
						}
					}
					if(createHandler) {
						try {
							getSpecifications().add(spec);
							logger.debug("Erzeuge SpecificationHandler zu: "+spec.getQueryId());
							getQuerySpecificationHandler((S) spec);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					return;
				}
			}

		}

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
		return new QuerySpecificationHandlerJxtaImpl<S>(spec, peer, getSelectionStrategy(), peer.getLog());
	}

	@Override
	public IQuerySelectionStrategy getQuerySelectionStrategy() {
		return this.selectionStrategy;
	}
	

}
