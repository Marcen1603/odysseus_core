package de.uniol.inf.is.odysseus.p2p_new.sources;

import java.io.IOException;
import java.util.Map;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.PeerException;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.P2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.service.DataDictionaryService;

public class SourcePublisher {

	private static final Logger LOG = LoggerFactory.getLogger(SourcePublisher.class);
	private static SourcePublisher instance;
	
	private Map<String, StreamAdvertisement> advertisedSources = Maps.newHashMap();
	
	private SourcePublisher() {
		
	}
	
	public static SourcePublisher getInstance() {
		if( instance == null ) {
			instance = new SourcePublisher();
		}
		return instance;
	}

	public void unpublishAll() {
		for( ViewAdvertisement viewAdv : P2PDictionary.getInstance().getViews() ) {
			try {
				if( viewAdv.isLocal() ) {
					P2PNewPlugIn.getDiscoveryService().flushAdvertisement(viewAdv);
					P2PDictionary.getInstance().removeView(viewAdv);
				}
			} catch (IOException e) {
				LOG.error("Could not unadvertise stream '{}'", viewAdv.getViewName(), e);
			}	
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
