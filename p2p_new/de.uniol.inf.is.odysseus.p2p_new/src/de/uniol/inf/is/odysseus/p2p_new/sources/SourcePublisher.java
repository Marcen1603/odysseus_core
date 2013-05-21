package de.uniol.inf.is.odysseus.p2p_new.sources;

import java.io.IOException;
import java.util.Map;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.service.DataDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;

public class SourcePublisher {

	private static final Logger LOG = LoggerFactory.getLogger(SourcePublisher.class);
	private static SourcePublisher instance;
	
	private Map<String, StreamAdvertisement> advertisedSources = Maps.newHashMap();
	private Map<String, ViewAdvertisement> publishedViews = Maps.newHashMap();
	
	private SourcePublisher() {
		
	}
	
	public static SourcePublisher getInstance() {
		if( instance == null ) {
			instance = new SourcePublisher();
		}
		return instance;
	}
	
	public void publish(String viewName, String queryBuildConfigurationName, ISession caller) throws PeerException {
		if( publishedViews.containsKey(viewName)) {
			return;
		}
		
		final ILogicalOperator view = DataDictionaryService.get().getView(viewName, caller);
		
		if( view != null ) {
			
			final PipeID pipeID = IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID());

			final JxtaSenderAO jxtaSender = new JxtaSenderAO();
			jxtaSender.setName(viewName + "_Send");
			jxtaSender.setPipeID(pipeID.toString());
			view.subscribeSink(jxtaSender, 0, 0, view.getOutputSchema());
			
			IServerExecutor executor = ServerExecutorService.get();
			Integer queryID = executor.addQuery(jxtaSender, caller, queryBuildConfigurationName);
			IPhysicalQuery physicalQuery = executor.getExecutionPlan().getQueryById(queryID);
			ILogicalQuery logicalQuery = physicalQuery.getLogicalQuery();
			logicalQuery.setName(viewName);
			logicalQuery.setParserId("P2P");
			logicalQuery.setUser(SessionManagementService.getActiveSession());
			
			ViewAdvertisement viewAdvertisement = (ViewAdvertisement)AdvertisementFactory.newAdvertisement(ViewAdvertisement.getAdvertisementType());
			viewAdvertisement.setID(IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID()));
			viewAdvertisement.setOutputSchema(view.getOutputSchema());
			viewAdvertisement.setPipeID(pipeID);
			viewAdvertisement.setViewName(viewName);
			
			try {
				P2PNewPlugIn.getDiscoveryService().publish(viewAdvertisement);
				publishedViews.put(viewName, viewAdvertisement);
			} catch (IOException e) {
				throw new PeerException("Could not publish view '" + viewName + "'", e);
			}			
		} else {
			throw new PeerException("Could not find view '" + view + "'");
		}
	}
	
	public void unpublish( String viewName ) {
		final ViewAdvertisement adv = publishedViews.get(viewName);
		if( adv != null ) {
			try {
				P2PNewPlugIn.getDiscoveryService().flushAdvertisement(adv);
				publishedViews.remove(adv);
			} catch (IOException e) {
				LOG.error("Could not unadvertise stream '{}'", viewName, e);
			}
		}
	}

	public void unpublishAll() {
		for( String publishedViewName : publishedViews.keySet() ) {
			unpublish(publishedViewName);
		}
	}
	
	public void advertise(String sourceName, ISession caller) throws PeerException {
		if( advertisedSources.containsKey(sourceName)) {
			return;
		}
		
		final ILogicalOperator stream = DataDictionaryService.get().getStreamForTransformation(sourceName, caller);
		Optional<AccessAO> optAccessAO = determineAccessAO(stream);
		if (optAccessAO.isPresent()) {
			final StreamAdvertisement adv = determineStreamAdvertisement(optAccessAO.get());
			
			try {
				P2PNewPlugIn.getDiscoveryService().publish(adv);
				advertisedSources.put(adv.getAccessAO().getSourcename(), adv);
			} catch (final IOException ex) {
				throw new PeerException("Could not advertise stream '" + adv.getAccessAO().getSourcename() + "'", ex);
			}

		} else {
			throw new PeerException("Could not find stream '" + stream + "'");
		}
	}
	
	public void unadvertise( String sourceName ) {
		final StreamAdvertisement adv = advertisedSources.get(sourceName);
		if( adv != null ) {
			try {
				P2PNewPlugIn.getDiscoveryService().flushAdvertisement(adv);
				advertisedSources.remove(adv);
			} catch (IOException e) {
				LOG.error("Could not unadvertise stream '{}'", sourceName, e);
			}
		}
	}
	
	public void unadvertiseAll() {
		for( String advertisedSourceName : advertisedSources.keySet() ) {
			unadvertise(advertisedSourceName);
		}
	}
	
	private static Optional<AccessAO> determineAccessAO(ILogicalOperator start) {
		if (start instanceof AccessAO) {
			return Optional.of((AccessAO) start);
		}

		for (final LogicalSubscription subscription : start.getSubscribedToSource()) {
			final Optional<AccessAO> optAcccessAO = determineAccessAO(subscription.getTarget());
			if (optAcccessAO.isPresent()) {
				return optAcccessAO;
			}
		}

		return Optional.absent();
	}
	
	private StreamAdvertisement determineStreamAdvertisement(AccessAO source) {
		if (advertisedSources.containsKey(source.getSourcename())) {
			return advertisedSources.get(source.getSourcename());
		}

		final StreamAdvertisement adv = (StreamAdvertisement) AdvertisementFactory.newAdvertisement(StreamAdvertisement.getAdvertisementType());
		adv.setAccessAO(new AccessAO(source)); // clean copy
		adv.setID(IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID()));

		return adv;
	}

}
