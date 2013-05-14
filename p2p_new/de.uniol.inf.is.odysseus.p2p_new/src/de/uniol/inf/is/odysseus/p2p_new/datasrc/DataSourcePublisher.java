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
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
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
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		doJobImpl(op);
	}

	@Override
	public void afterJob() {
		dataDictionary.removeListener(this);
	}

	@Override
	public void beforeJob() {
		dataDictionary.addListener(this);
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// do nothing
	}

	@Override
	public void doJob() {
		for (final Entry<String, ILogicalOperator> stream : dataDictionary.getStreams(SessionManagementService.getActiveSession())) {
			doJobImpl(stream.getValue());
		}
	}

	public void publishSource(SourceAdvertisement adv) {
		tryPublishSource(adv, PUBLISH_INTERVAL_MILLIS);
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		final Optional<AccessAO> optAccessAO = determineAccessAO(op);
		if (optAccessAO.isPresent()) {
			unpublishSource(optAccessAO.get());
		} else {
			LOG.error("Could not unpublish existing source since the accessAO is not found from logical operator {}", op);
		}
	}

	private SourceAdvertisement determineSourceAdvertisement(AccessAO source) {
		if (publishedSources.containsKey(source.getSourcename())) {
			return publishedSources.get(source.getSourcename());
		}

		final SourceAdvertisement adv = (SourceAdvertisement) AdvertisementFactory.newAdvertisement(SourceAdvertisement.getAdvertisementType());
		adv.setAccessAO(new AccessAO(source)); // clean copy
		adv.setID(IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID()));

		return adv;
	}

	private void doJobImpl(ILogicalOperator operator) {
		final Optional<AccessAO> optAccessAO = determineAccessAO(operator);
		if (optAccessAO.isPresent()) {
			if (isPublishable(optAccessAO.get())) {
				publishSource(optAccessAO.get(), PUBLISH_INTERVAL_MILLIS - (System.currentTimeMillis() - getLastExecutionTimestamp()));
			}
		} else {
			LOG.error("Could not publish new source since the accessAO is not found from logical operator {}", operator);
		}
	}

	private void publishSource(AccessAO source, long lifetime) {
		final SourceAdvertisement adv = determineSourceAdvertisement(source);
		tryPublishSource(adv, lifetime);
	}

	private void tryPublishSource(SourceAdvertisement adv, long lifetime) {
		LOG.debug("Publishing source {} with lifetime {} ms", adv.getAccessAO().getSourcename(), lifetime);
		try {
			discoveryService.publish(adv, lifetime, lifetime);
			publishedSources.put(adv.getAccessAO().getSourcename(), adv);
		} catch (final IOException ex) {
			LOG.error("Could not publish source {}", adv.getAccessAO().getSourcename(), ex);
		}
	}

	private void unpublishSource(AccessAO source) {
		LOG.debug("Unpublishing source {}", source);

		final SourceAdvertisement adv = publishedSources.remove(source.getSourcename());
		try {
			if( adv != null ) {
				P2PNewPlugIn.getDiscoveryService().flushAdvertisement(adv);
			}
		} catch (final IOException ex) {
			LOG.error("Could not flush advertisement {}", adv, ex);
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

	private static boolean isPublishable(AccessAO accessAO) {
		if (accessAO.getTransportHandler().equalsIgnoreCase("file")) {
			return false;
		}

		final String publicFlag = accessAO.getOptionsMap().get("public");
		if (!Strings.isNullOrEmpty(publicFlag)) {
			if ("false".equalsIgnoreCase(publicFlag)) {
				return false;
			}
		}

		return true;
	}

}
