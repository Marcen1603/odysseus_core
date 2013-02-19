package de.uniol.inf.is.odysseus.p2p_new.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.jxta.content.Content;
import net.jxta.content.ContentID;
import net.jxta.content.ContentShare;
import net.jxta.discovery.DiscoveryService;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;

public class DataSourcePublisherThread extends RepeatingJobThread implements IDataDictionaryListener {

	private static final Logger LOG = LoggerFactory.getLogger(DataSourcePublisherThread.class);
	private static final String THREAD_NAME = "Datasource Publisher";
	private static final long PUBLISH_INTERVAL_MILLIS = 60 * 1000;

	private final IDataDictionary dataDictionary;
	private final DiscoveryService discoveryService;
	private final Map<String, ContentID> sharedContentMap = Maps.newHashMap();

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
		for (Entry<String, ILogicalOperator> stream : dataDictionary.getStreams(P2PNewPlugIn.getActiveSession())) {
			publishSourceName(determineContentID(stream.getKey()), stream.getKey(), PUBLISH_INTERVAL_MILLIS);
		}	
	}

	@Override
	public void afterJob() {
		dataDictionary.removeListener(this);
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		ContentID contentID = IDFactory.newContentID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID(), false);
		sharedContentMap.put(name, contentID);

		publishSourceName(contentID, name, PUBLISH_INTERVAL_MILLIS - (System.currentTimeMillis() - getLastExecutionTimestamp()));
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		if (sharedContentMap.containsKey(name)) {

			P2PNewPlugIn.getContentService().unshareContent(sharedContentMap.get(name));
			sharedContentMap.remove(name);
		}
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// do nothing
	}

	private ContentID determineContentID(String name) {
		ContentID contentID = null;
		if (sharedContentMap.containsKey(name)) {
			contentID = sharedContentMap.get(name);
		} else {
			contentID = IDFactory.newContentID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID(), false);
			sharedContentMap.put(name, contentID);
		}
		return contentID;
	}

	private void publishSourceName(ContentID contentID, String srcName, long lifetime) {
		LOG.debug("Publish source {}", srcName);
		
		Content viewContent = new Content(contentID, null, new StringMessageElement(null, srcName, null));
		List<ContentShare> shares = P2PNewPlugIn.getContentService().shareContent(viewContent);
		try {
			for (ContentShare share : shares) {
				discoveryService.publish(share.getContentShareAdvertisement(), lifetime, lifetime);
			}

		} catch (IOException ex) {
			LOG.error("Could not publish content for data source {}", srcName, ex);
			P2PNewPlugIn.getContentService().unshareContent(contentID);
		}
	}
}
