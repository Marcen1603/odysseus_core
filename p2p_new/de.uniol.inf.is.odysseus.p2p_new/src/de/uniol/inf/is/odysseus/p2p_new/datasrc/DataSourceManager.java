package de.uniol.inf.is.odysseus.p2p_new.datasrc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.Advertisement;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.adv.SourceAdvertisement;

public class DataSourceManager implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(DataSourceManager.class);
	
	private DataSourcePublisherThread dataSourcePublisherThread;
	
	@Override
	public void advertisementOccured(IAdvertisementManager sender, Advertisement adv) {
		SourceAdvertisement srcAdv = (SourceAdvertisement)adv;
		
		LOG.debug("Got source advertisement of source {}", srcAdv.getSourceName());
	}

	@Override
	public boolean isSelected(Advertisement advertisement) {
		return (advertisement instanceof SourceAdvertisement);
	}
	
	// called by OSGi-DS
	public void bindAdvertisementManager( IAdvertisementManager manager ) {
		manager.addAdvertisementListener(this);
		
		LOG.debug("Bound AdvertisementManager {}", manager);
	}
	
	// called by OSGi-DS
	public void unbindAdvertisementManager( IAdvertisementManager manager ) {
		manager.removeAdvertisementListener(this);
		
		LOG.debug("Unbound AdvertisementManager {}", manager);
	}
	
	// called by OSGi-DS
	public void bindDataDictionary( IDataDictionary dd ) {
		dataSourcePublisherThread = new DataSourcePublisherThread(dd, P2PNewPlugIn.getDiscoveryService());
		dataSourcePublisherThread.start();
		
		LOG.debug("Bound DataDictionary {}", dd);
	}
	
	// called by OSGi-DS
	public void unbindDataDictionary( IDataDictionary dd ) {
		dataSourcePublisherThread.stopRunning();
		
		LOG.debug("Unbound DataDictionary {}", dd);
	}
}
