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
/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.rcp.views.sink;

import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.TreeViewer;
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

import de.uniol.inf.is.odysseus.core.planmanagement.SinkInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IUpdateEventListener;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.views.OperatorDragListener;
import de.uniol.inf.is.odysseus.rcp.views.ResourceInformationContentProvider;
import de.uniol.inf.is.odysseus.rcp.views.ResourceInformationLabelProvider;

/**
 * 
 * @author Dennis Geesen Created at: 24.08.2011
 */
public class SinkView extends ViewPart implements IUpdateEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(SinkView.class);
	private TreeViewer viewer;
	private StackLayout stackLayout;
	private Composite parent;
	private Label label;

	volatile boolean isRefreshing = false;

	@Override
	public void dispose() {
		IExecutor e = OdysseusRCPPlugIn.getExecutor();
		e.removeUpdateEventListener(this, IUpdateEventListener.DATADICTIONARY,
				OdysseusRCPPlugIn.getActiveSession());
		e.removeUpdateEventListener(this, IUpdateEventListener.SESSION, null);

		super.dispose();
	}

	public TreeViewer getTreeViewer() {
		return viewer;
	}

	public void refresh() {
		if (isRefreshing || PlatformUI.getWorkbench().getDisplay().isDisposed()) {
			return;
		}
		isRefreshing = true;
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					if (!getTreeViewer().getTree().isDisposed()
							&& !PlatformUI.getWorkbench().getDisplay()
									.isDisposed()) {
						List<SinkInformation> sinks = OdysseusRCPPlugIn
								.getExecutor().getSinks(
										OdysseusRCPPlugIn.getActiveSession());

						if (sinks != null) {
							getTreeViewer().setInput(sinks);

							if (!sinks.isEmpty()) {
								stackLayout.topControl = getTreeViewer()
										.getTree();
								setPartName("Sinks (" + sinks.size() + ")");
							} else {
								stackLayout.topControl = label;
								setPartName("Sinks (0)");
							}
						}
						parent.layout();
					}
				} catch (Exception e) {
					LOG.error(
							"Exception during setting input for treeViewer in sinkView",
							e);
				}
				isRefreshing = false;
			}

		});

	}

	protected void setTreeViewer(TreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void setFocus() {
		getTreeViewer().getControl().setFocus();
	}

	@Override
	public void eventOccured(String type) {
		if (type == IUpdateEventListener.DATADICTIONARY) {
			refresh();
		}else if (type == IUpdateEventListener.SESSION){
			// TODO:
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parent = parent;

		stackLayout = new StackLayout();
		parent.setLayout(stackLayout);

		setTreeViewer(new TreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.MULTI));
		getTreeViewer().setContentProvider(
				new ResourceInformationContentProvider());
		getTreeViewer().setLabelProvider(
				new ResourceInformationLabelProvider("sink"));

		int operations = DND.DROP_MOVE;
		Transfer[] transferTypes = new Transfer[] { LocalSelectionTransfer
				.getTransfer() };
		getTreeViewer().addDragSupport(operations, transferTypes,
				new OperatorDragListener(getTreeViewer(), "SINK"));

		refresh();
		IExecutor e = OdysseusRCPPlugIn.getExecutor();
		e.addUpdateEventListener(this, IUpdateEventListener.DATADICTIONARY,
				OdysseusRCPPlugIn.getActiveSession());
		e.addUpdateEventListener(this, IUpdateEventListener.SESSION, null);
		// UserManagement.getUsermanagement().addUserManagementListener(this);
		getSite().setSelectionProvider(getTreeViewer());

		// Contextmenu
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(getTreeViewer()
				.getControl());
		// Set the MenuManager
		getTreeViewer().getControl().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, getTreeViewer());

		label = new Label(parent, SWT.NONE);
		label.setText("No sinks available");

		stackLayout.topControl = label;
		parent.layout();

	}

}
