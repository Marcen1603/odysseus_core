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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public abstract class AbstractDashboardPart implements IDashboardPart {

	private IDashboardPartQueryTextProvider queryTextProvider;
	private List<IDashboardPartListener> listener = new ArrayList<>();
	private String sinkNames;

	@Override
	public IDashboardPartQueryTextProvider getQueryTextProvider() {
		return queryTextProvider;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void onLoad(Map<String, String> saved) {
	}

	@Override
	public void onPause() {
	}

	@Override
	public Map<String, String> onSave() {
		return Maps.newHashMap();
	}

	@Override
	public void onStart(List<IPhysicalOperator> physicalRoots) throws Exception {
	}

	@Override
	public void onStop() {
	}

	@Override
	public void onUnpause() {
	}

	@Override
	public void setQueryTextProvider(IDashboardPartQueryTextProvider provider) {
		this.queryTextProvider = Preconditions.checkNotNull(provider, "QueryTextProvider for DashboardPart must not be null!");
	}
	
	@Override
	public void addListener(IDashboardPartListener listener) {
		this.listener.add(listener);
	}
	
	@Override
	public void removeListener(IDashboardPartListener listener) {
		this.listener.remove(listener);
	}
	
	@Override
	public String getSinkNames() {
		return sinkNames;
	}
	
	public void setSinkNames(String sinkNames) {
		this.sinkNames = sinkNames;
	};
}
