package de.uniol.inf.is.odysseus.p2p_new.datasrc;

import java.io.IOException;
import java.util.Map.Entry;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.adv.SourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.service.SessionManagementService;
import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;

public class DataSourcePublisherThread extends RepeatingJobThread implements IDataDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(DataSourcePublisherThread.class);
	private static final String THREAD_NAME = "Datasource Publisher";
	private static final long PUBLISH_INTERVAL_MILLIS = 60 * 1000;

	private final IDataDictionary dataDictionary;
	private final DiscoveryService discoveryService;

	public DataSourcePublisherThread(IDataDictionary dataDictionary, DiscoveryService discoveryService) {
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
			publishSourceName(stream.getKey(), PUBLISH_INTERVAL_MILLIS);
		}
	}

	@Override
	public void afterJob() {
		dataDictionary.removeListener(this);
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		publishSourceName(name, PUBLISH_INTERVAL_MILLIS - (System.currentTimeMillis() - getLastExecutionTimestamp()));
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		// do nothing
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// do nothing
	}

	private void publishSourceName(String srcName, long lifetime) {
		LOG.debug("Publishing source {}", srcName);

		SourceAdvertisement adv = (SourceAdvertisement) AdvertisementFactory.newAdvertisement(SourceAdvertisement.getAdvertisementType());
		adv.setSourceName(srcName);
		adv.setID(IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID()));

		try {
			discoveryService.publish(adv, lifetime, lifetime);
		} catch (IOException ex) {
			LOG.error("Could not publish source {}", srcName, ex);
		}

	}
}
