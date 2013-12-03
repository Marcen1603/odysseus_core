/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
