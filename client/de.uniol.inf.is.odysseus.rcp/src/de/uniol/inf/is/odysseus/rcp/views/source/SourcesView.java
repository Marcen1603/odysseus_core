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
package de.uniol.inf.is.odysseus.rcp.views.source;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.views.OperatorDragListener;
import de.uniol.inf.is.odysseus.rcp.views.ResourceInformationContentProvider;
import de.uniol.inf.is.odysseus.rcp.views.ResourceInformationLabelProvider;

public class SourcesView extends ViewPart implements IUpdateEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(SourcesView.class);
	private static final InfoService INFO = InfoServiceFactory.getInfoService(SourcesView.class);

	private Composite parent;
	private TreeViewer viewer;
	private StackLayout stackLayout;
	private Label label;

	volatile AtomicBoolean isRefreshing = new AtomicBoolean();
	private boolean refreshEnabled = true;

	@Override
	public void createPartControl(Composite parent) {
		try {
			this.parent = parent;

			stackLayout = new StackLayout();
			parent.setLayout(stackLayout);

			setTreeViewer(new TreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI));
			getTreeViewer().setContentProvider(new ResourceInformationContentProvider());
			getTreeViewer().setLabelProvider(new ResourceInformationLabelProvider("source"));

			int operations = DND.DROP_MOVE;
			Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer.getTransfer() };
			getTreeViewer().addDragSupport(operations, transferTypes,
					new OperatorDragListener(getTreeViewer(), "STREAM"));

			refresh();
			registerForDatadictionaryEvents();
			OdysseusRCPPlugIn.getExecutor().addUpdateEventListener(this, IUpdateEventListener.SESSION, null);
			OdysseusRCPPlugIn.getExecutor().addUpdateEventListener(this, IUpdateEventListener.QUERY,
					OdysseusRCPPlugIn.getActiveSession());
			LOG.trace("Registered update event listener for sources View");

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

			LOG.trace("SourcesView created");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void registerForDatadictionaryEvents() {
		if (OdysseusRCPPlugIn.getActiveSession() != null) {
			OdysseusRCPPlugIn.getExecutor().addUpdateEventListener(this, IUpdateEventListener.DATADICTIONARY,
					OdysseusRCPPlugIn.getActiveSession());
		}
	}

	@Override
	public void dispose() {
		OdysseusRCPPlugIn.getExecutor().removeUpdateEventListener(this, IUpdateEventListener.DATADICTIONARY,
				OdysseusRCPPlugIn.getActiveSession());
		OdysseusRCPPlugIn.getExecutor().removeUpdateEventListener(this, IUpdateEventListener.SESSION, null);
		LOG.trace("Deregistered update event listener for sources View");
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
		LOG.trace("Trying to refresh sources view..");
		if (refreshEnabled) {
			if (isRefreshing.get()) {
				LOG.trace("Already refreshing... abort");
				return;
			}

			if (PlatformUI.getWorkbench().getDisplay().isDisposed()) {
				LOG.trace("Sources View is disposed... abort");
				return;
			}

			isRefreshing.set(true);
			LOG.trace("Begin refreshing async");

			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					try {
						LOG.trace("Begin real refreshing");
						isRefreshing.set(false);

						if (!getTreeViewer().getTree().isDisposed()) {
							List<ViewInformation> streamsAndViews = OdysseusRCPPlugIn.getExecutor()
									.getStreamsAndViewsInformation(OdysseusRCPPlugIn.getActiveSession());
							if (streamsAndViews != null) {
								LOG.trace("Got {} streams and Views.", streamsAndViews.size());
								getTreeViewer().setInput(streamsAndViews);
								if (!streamsAndViews.isEmpty()) {
									stackLayout.topControl = getTreeViewer().getTree();
								}
								setPartName("Sources (" + streamsAndViews.size() + ")");
							} else {
								LOG.trace("Got no streams and views");
								stackLayout.topControl = label;
								setPartName("Sources (0)");
							}
							parent.layout();

							LOG.trace("Doing refresh now!");
							getTreeViewer().refresh();
						} else {
							LOG.trace("View is disposed!");
						}
						LOG.trace("Refresh finished");
					} catch (Exception e) {
						LOG.error("Exception during setting input for treeViewer in sourcesView", e);
						INFO.error("Exception during setting input for treeViewer in sourcesView", e);
					}
				}

			});
		} else {
			LOG.trace("Refresh is disabled");
		}
	}

	protected void setTreeViewer(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void eventOccured(String type) {
		LOG.trace("Got update event " + type);
		if (type == IUpdateEventListener.SESSION) {
			registerForDatadictionaryEvents();
		}
		refresh();
	}

	public boolean isRefreshEnabled() {
		return refreshEnabled;
	}

	public void setRefreshEnabled(boolean refreshEnabled) {
		this.refreshEnabled = refreshEnabled;
		LOG.trace("Refresh enabled = {}", refreshEnabled);
		refresh();
	}

	public void switchRefreshEnabled() {
		refreshEnabled = !refreshEnabled;
		LOG.trace("Switch Refresh enabled to {}", refreshEnabled);

		refresh();
	}

}
