/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard;

import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public abstract class AbstractDashboardPart implements IDashboardPart {

	private Configuration configuration;
	private IDashboardPartQueryTextProvider queryTextProvider;
	
	@Override
	public boolean init(Configuration configuration) {
		this.configuration = configuration;
		this.configuration.addListener(this);
		
		return true;
	}

	@Override
	public Configuration getConfiguration() {
		return configuration;
	}
	
	@Override
	public IDashboardPartQueryTextProvider getQueryTextProvider() {
		return queryTextProvider;
	}

	@Override
	public void setQueryTextProvider(IDashboardPartQueryTextProvider provider) {
		this.queryTextProvider = Preconditions.checkNotNull(provider, "QueryTextProvider for DashboardPart must not be null!");
	}

	@Override
	public void onStart(List<IPhysicalOperator> physicalRoots) throws Exception {
	}
	
	@Override
	public void onPause() {
	}
	
	@Override
	public void onUnpause() {
	}
	
	@Override
	public void onStop() {
	}
	
	@Override
	public Map<String, String> onSave() {
		return Maps.newHashMap();
	}
	
	@Override
	public void onLoad(Map<String, String> saved) {
	}
}
