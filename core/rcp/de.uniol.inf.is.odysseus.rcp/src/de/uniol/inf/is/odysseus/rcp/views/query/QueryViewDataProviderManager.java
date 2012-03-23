package de.uniol.inf.is.odysseus.rcp.views.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryViewDataProviderManager {

	private static final Logger LOG = LoggerFactory.getLogger(QueryViewDataProviderManager.class);
	
	private static IQueryViewDataProvider dataProvider;
	
	public void bindQueryViewDataProvider(IQueryViewDataProvider provider) {
		if (dataProvider != null) {
			LOG.error("More than one dataProvider for QueryView provided");
		}
		dataProvider = provider;
		LOG.debug("DataProvider for QueryView bound: " + provider);
	}

	public void unbindQueryViewDataProvider(IQueryViewDataProvider provider) {
		if (dataProvider == provider) {
			dataProvider = null;
			LOG.debug("DataProvider for QueryView unbound: " + provider);
		} else {
			LOG.error("Tried to unbound not used DataProvider "+ provider + " for QueryView");
			LOG.error("Nothing unbound.");
		}
	}

	public static IQueryViewDataProvider getQueryViewDataProvider() {
		return dataProvider;
	}
	
	public static boolean hasQueryViewDataProvider() {
		return getQueryViewDataProvider() != null;
	}
}
