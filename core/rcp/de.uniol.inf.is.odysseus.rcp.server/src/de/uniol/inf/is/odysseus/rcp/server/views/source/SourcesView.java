/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.server.views.source;

import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionEvent;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionListener;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementListener;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.server.views.OperatorViewContentProvider;
import de.uniol.inf.is.odysseus.rcp.server.views.OperatorViewLabelProvider;

public class SourcesView extends ViewPart implements IDataDictionaryListener, IUserManagementListener, ISessionListener {

	private static final Logger LOG = LoggerFactory.getLogger(SourcesView.class);

	private Composite parent;
	private TreeViewer viewer;
	private StackLayout stackLayout;
	private Label label;
	
	volatile boolean isRefreshing;
	private boolean refreshEnabled = true;

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;
		
		stackLayout = new StackLayout();
		parent.setLayout(stackLayout);

		setTreeViewer(new TreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI));
		getTreeViewer().setContentProvider(new OperatorViewContentProvider());
		getTreeViewer().setLabelProvider(new OperatorViewLabelProvider("source"));
		refresh();
		if (OdysseusRCPPlugIn.getExecutor() instanceof IServerExecutor) {
			((IServerExecutor) OdysseusRCPPlugIn.getExecutor()).getDataDictionary(OdysseusRCPPlugIn.getActiveSession().getTenant()).addListener(this);
			UserManagementProvider.getSessionmanagement().subscribe(this);
		}
		// UserManagement.getInstance().addUserManagementListener(this);
		getSite().setSelectionProvider(getTreeViewer());

		// Contextmenu
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(getTreeViewer().getControl());
		// Set the MenuManager
		getTreeViewer().getControl().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, getTreeViewer());
		
		label = new Label(parent, SWT.NONE);
		label.setText("No sources available");
		
		stackLayout.topControl = label;
		parent.layout();
	}

	@Override
	public void dispose() {
		if (OdysseusRCPPlugIn.getExecutor() instanceof IServerExecutor) {
			((IServerExecutor) OdysseusRCPPlugIn.getExecutor()).getDataDictionary(OdysseusRCPPlugIn.getActiveSession().getTenant()).removeListener(this);
		}
		super.dispose();
	}

	@Override
	public void setFocus() {
		getTreeViewer().getControl().setFocus();
	}

	public TreeViewer getTreeViewer() {
		return viewer;
	}

	public final void setSorting(boolean doSorting) {
		if (doSorting) {
			viewer.setSorter(new ViewerSorter());
		} else {
			viewer.setSorter(null);
		}
	}

	public final boolean isSorting() {
		return viewer.getSorter() != null;
	}

	public void refresh() {

		if (refreshEnabled) {
			if (isRefreshing) {
				return;
			}
			isRefreshing = true;
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					try {
						isRefreshing = false;
						if( !getTreeViewer().getTree().isDisposed() ) {
							Set<Entry<Resource, ILogicalOperator>> streamsAndViews = OdysseusRCPPlugIn.getExecutor().getStreamsAndViews(OdysseusRCPPlugIn.getActiveSession());
							getTreeViewer().setInput(streamsAndViews);
							
							if( !streamsAndViews.isEmpty() ) {
								stackLayout.topControl = getTreeViewer().getTree();
							} else {
								stackLayout.topControl = label;
							}
							parent.layout();
						}
					} catch (Exception e) {
						LOG.error("Exception during setting input for treeViewer in sourcesView", e);
					}
				}

			});
		}
	}

	protected void setTreeViewer(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		refresh();
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op) {
		refresh();
	}

	@Override
	public void usersChangedEvent() {
		refresh();
	}

	@Override
	public void roleChangedEvent() {
		refresh();
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		refresh();
	}

	public boolean isRefreshEnabled() {
		return refreshEnabled;
	}

	public void setRefreshEnabled(boolean refreshEnabled) {
		this.refreshEnabled = refreshEnabled;
	}
	
	@Override
	public void sessionEventOccured(ISessionEvent event) {
		refresh();
	}

}
