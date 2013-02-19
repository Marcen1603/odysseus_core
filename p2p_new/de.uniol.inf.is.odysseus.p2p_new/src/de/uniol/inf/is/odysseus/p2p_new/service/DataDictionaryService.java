package de.uniol.inf.is.odysseus.p2p_new.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.impl.DataSourcePublisherThread;

public class DataDictionaryService {

	private static final Logger LOG = LoggerFactory.getLogger(DataDictionaryService.class);

	private static IDataDictionary dataDictionary;
	private static DataSourcePublisherThread dataSourcePublisherThread;

	public final void bindDataDictionary(IDataDictionary dd) {
		dataDictionary = dd;
		dataSourcePublisherThread = new DataSourcePublisherThread(dataDictionary, P2PNewPlugIn.getDiscoveryService());
		dataSourcePublisherThread.start();

		LOG.info("Data dictionary bound: {}", dd);
	}

	public final void unbindDataDictionary(IDataDictionary dd) {
		if (dd == dataDictionary) {
			dataSourcePublisherThread.stopRunning();
			dataDictionary = null;

			LOG.info("Data dictionary unbound: {}", dd);
		}
	}

	public static IDataDictionary getDataDictionary() {
		return dataDictionary;
	}

}
