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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Optional;
import java.util.Objects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;

public abstract class AbstractDashboardPart implements IDashboardPart {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractDashboardPart.class);

	protected IDashboardPartQueryTextProvider queryTextProvider;
	private List<IDashboardPartListener> listeners = new ArrayList<>();
	private String sinkNames;
	private boolean sinkSynced = false;
	private Map<String, String> contextMap = Maps.newHashMap();

	private IDashboardActionBarContributor editorActionBarContributor;
	private IWorkbenchPart workbenchpart;
	private IFile dashboardPartFile;
	private IProject containingProject;
	private boolean isStarted;
	private boolean isLocked;

	@Override
	public IDashboardPartQueryTextProvider getQueryTextProvider() {
		return queryTextProvider;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void onLoad(Map<String, String> saved) {
	}

	@Override
	public void onLoadXML(Document document, Element xmlElement) {
	}

	@Override
	public void onPause() {
	}

	@Override
	public Map<String, String> onSave() {
		return Maps.newHashMap();
	}

	@Override
	public void onSaveXML(Document document, Element xmlElement) {
	}

	@Override
	public void init(IFile dashboardFile, IProject containingProject, IWorkbenchPart containingPart) {
		this.workbenchpart = containingPart;
		this.dashboardPartFile = dashboardFile;
		this.containingProject = containingProject;
	}

	@Override
	public void setProject(IProject project) {
		this.containingProject = project;
	}

	@Override
	public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception {
		isStarted = true;
	}

	@Override
	public void onStop() {
		isStarted = false;
	}

	@Override
	public void onUnpause() {
	}

	public final boolean isStarted() {
		return isStarted;
	}

	@Override
	public void onLock() {
		isLocked = true;
	}

	@Override
	public void onUnlock() {
		isLocked = false;
	}

	public final boolean isLocked() {
		return isLocked;
	}

	@Override
	public void setQueryTextProvider(IDashboardPartQueryTextProvider provider) {
		this.queryTextProvider = Objects.requireNonNull(provider, "QueryTextProvider for DashboardPart must not be null!");
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

	public final IWorkbenchPart getWorkbenchPart(){
		return this.workbenchpart;
	}

	public final IFile getDashboardPartFile() {
		return dashboardPartFile;
	}

	public final IProject getProject() {
		return containingProject;
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
	public IDashboardActionBarContributor getEditorActionBarContributor() {
		return editorActionBarContributor;
	}

	public void setEditorActionBarContributor(IDashboardActionBarContributor editorActionBarContributor){
		this.editorActionBarContributor = editorActionBarContributor;
	}

	@Override
	public Point getPreferredSize() {
		return new Point(500, 300);
	}

	@Override
	public boolean isSinkSynchronized() {
		return sinkSynced;
	}

	@Override
	public void setSinkSynchronized(boolean doSync) {
		sinkSynced = doSync;
	}

	@Override
	public void addContext( String key, String value ) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "key for context map must not be null or empty!");
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(value), "value for context map must not be null or empty!");

		contextMap.put(key,  value);
	}

	@Override
	public Optional<String> getContextValue( String key ) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "key for context map must not be null or empty!");

		return Optional.fromNullable(contextMap.get(key));
	}

	@Override
	public ImmutableCollection<String> getContextKeys() {
		return ImmutableSet.copyOf(contextMap.keySet());
	}

	@Override
	public void removeContext( String key ) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "key for context map must not be null or empty!");
		contextMap.remove(key);
	}

	@Override
	public void streamElementReceived(IPhysicalOperator senderOperator,
			IStreamObject<?> element, int port) {
		// Just for migration cases
		streamElementRecieved(senderOperator,element, port);
	}

	@Deprecated
	protected void streamElementRecieved(IPhysicalOperator senderOperator,
			IStreamObject<?> element, int port) {
	}

	@Override
	public void punctuationElementReceived(IPhysicalOperator senderOperator,
			IPunctuation point, int port) {
		// Just for migration cases
		punctuationElementRecieved(senderOperator, point, port);
	}

	@Deprecated
	protected void punctuationElementRecieved(IPhysicalOperator senderOperator,
			IPunctuation point, int port) {
	}

}
