package de.uniol.inf.is.odysseus.peer.smarthome.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionaryListener;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;

public class P2PDictionaryListener implements IP2PDictionaryListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);

	@Override
	public void sourceAdded(IP2PDictionary sender,
			SourceAdvertisement advertisement) {
		LOG.debug("sourceAdded");
	}

	@Override
	public void sourceRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement) {
		LOG.debug("sourceRemoved");
	}

	@Override
	public void sourceImported(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceImported sourceName:" + sourceName);

		if (advertisement.isLocal()) {
			LOG.debug("advertisement.isLocal()");
			return;
		}

	}

	@Override
	public void sourceImportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceImportRemoved sourceName:" + sourceName+" and stopQuery");

		SmartHomeServerPlugIn.removeSourceNeededForImport(sourceName);
		LogicExecutor.getInstance().stopQuery(sourceName);
		LogicExecutor.getInstance().stopQuery(sourceName+"_query");
		
		if (LogicExecutor.getInstance().isSourceNameUsingByLogicRule(sourceName)) {
			// TODO: Anfragen wieder entfernen:

		}
	}

	@Override
	public void sourceExported(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceExported: " + sourceName);
	}

	@Override
	public void sourceExportRemoved(IP2PDictionary sender,
			SourceAdvertisement advertisement, String sourceName) {
		LOG.debug("sourceExportRemoved: " + sourceName);
	}
}
