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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.IWorkbenchPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public abstract class AbstractDashboardPart implements IDashboardPart {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDashboardPart.class);
	
	private IDashboardPartQueryTextProvider queryTextProvider;
	private List<IDashboardPartListener> listeners = new ArrayList<>();
	private String sinkNames;

	private IWorkbenchPart workbenchpart;

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
	public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception {
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
		synchronized(listeners) {
			this.listeners.add(listener);
		}
	}
	
	@Override
	public void removeListener(IDashboardPartListener listener) {
		synchronized(listeners) {
			this.listeners.remove(listener);
		}
	}
	
	protected final void fireChangeEvent() {
		synchronized( listeners ) {
			for( IDashboardPartListener listener : listeners ) {
				try {
					listener.dashboardPartChanged(this);
				} catch( Throwable t ) {
					LOG.error("Exception during invoking dashboard part listener");
				}
			}
		}
	}
	
	@Override
	public String getSinkNames() {
		return sinkNames;
	}
	
	@Override
	public void setSinkNames(String sinkNames) {
		this.sinkNames = sinkNames;
	}
	
	@Override
	public void setWorkbenchPart(IWorkbenchPart workbenchpart){
		this.workbenchpart = workbenchpart;
	}
	
	@Override
	public IWorkbenchPart getWorkbenchPart(){
		return this.workbenchpart;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPart#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class<?> adapter) {	
		return null;
	}
}
