package de.uniol.inf.is.odysseus.p2p_new.datasrc;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.adv.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class DataSourcePublisher extends RepeatingJobThread implements IDataDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(DataSourcePublisher.class);
	private static final String THREAD_NAME = "Datasource Publisher";
	private static final long PUBLISH_INTERVAL_MILLIS = 60 * 1000;

	private final IDataDictionary dataDictionary;
	private final DiscoveryService discoveryService;
	private final Map<String, SourceAdvertisement> publishedSources = Maps.newHashMap();

	public DataSourcePublisher(IDataDictionary dataDictionary, DiscoveryService discoveryService) {
		super(PUBLISH_INTERVAL_MILLIS, THREAD_NAME);

		this.dataDictionary = Preconditions.checkNotNull(dataDictionary, "DataDictionary must not be null");
		this.discoveryService = Preconditions.checkNotNull(discoveryService, "Discovery Service must not be null");
	}

	@Override
	public void beforeJob() {
		dataDictionary.addListener(this);
	}

	@Override
	public void doJob() {
		for (Entry<String, ILogicalOperator> stream : dataDictionary.getStreams(SessionManagementService.getActiveSession())) {
			Optional<AccessAO> optAccessAO = determineAccessAO(stream.getValue());
			if( optAccessAO.isPresent() ) {
				publishSource(optAccessAO.get(), PUBLISH_INTERVAL_MILLIS - (System.currentTimeMillis() - getLastExecutionTimestamp()));
			} else {
				LOG.error("Could not publish new source since the accessAO is not found from logical operator {}", stream.getValue());
			}
		}
	}

	@Override
	public void afterJob() {
		dataDictionary.removeListener(this);
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		Optional<AccessAO> optAccessAO = determineAccessAO(op);
		if( optAccessAO.isPresent() ) {
			publishSource(optAccessAO.get(), PUBLISH_INTERVAL_MILLIS - (System.currentTimeMillis() - getLastExecutionTimestamp()));
		} else {
			LOG.error("Could not publish new source since the accessAO is not found from logical operator {}", op);
		}
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		Optional<AccessAO> optAccessAO = determineAccessAO(op);
		if( optAccessAO.isPresent() ) {
			unpublishSource(optAccessAO.get());
		} else {
			LOG.error("Could not unpublish existing source since the accessAO is not found from logical operator {}", op);
		}
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// do nothing
	}
	
	public void addSourceAdvertisement( SourceAdvertisement adv ) {
		publishedSources.put(adv.getAccessAO().getSourcename(), adv);
		try {
			discoveryService.publish(adv);
		} catch (IOException ex) {
			LOG.error("Could not publish source {}", adv.getAccessAO(), ex);
		}
	}

	private void publishSource(AccessAO source, long lifetime) {
		LOG.debug("Publishing source {} with lifetime {}", source, lifetime);

		SourceAdvertisement adv = determineSourceAdvertisement(source);

		try {
			discoveryService.publish(adv, lifetime, lifetime);
		} catch (IOException ex) {
			LOG.error("Could not publish source {}", source, ex);
		}
	}

	private void unpublishSource(AccessAO source) {
		LOG.debug("Unpublishing source {}", source);
		
		SourceAdvertisement adv = publishedSources.remove(source.getSourcename());
		try {
			P2PNewPlugIn.getDiscoveryService().flushAdvertisement(adv);
		} catch (IOException ex) {
			LOG.error("Could not flush advertisement {}", adv, ex);
		}
	}

	private SourceAdvertisement determineSourceAdvertisement(AccessAO source) {
		if (publishedSources.containsKey(source.getSourcename())) {
			return publishedSources.get(source.getSourcename());
		}

		SourceAdvertisement adv = (SourceAdvertisement) AdvertisementFactory.newAdvertisement(SourceAdvertisement.getAdvertisementType());
		adv.setAccessAO(new AccessAO(source)); // clean copy
		adv.setID(IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID()));

		publishedSources.put(source.getSourcename(), adv);
		return adv;
	}

	private static Optional<AccessAO> determineAccessAO(ILogicalOperator start) {
		if( start instanceof AccessAO) {
			return Optional.of((AccessAO)start);
		}
		
		for( LogicalSubscription subscription : start.getSubscribedToSource()) {
			Optional<AccessAO> optAcccessAO = determineAccessAO(subscription.getTarget());
			if( optAcccessAO.isPresent() ) {
				return optAcccessAO;
			}
		}
		
		return Optional.absent();
	}
}
