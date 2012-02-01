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

package de.uniol.inf.is.odysseus.rcp.views;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.datadictionary.AbstractDataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.usermanagement.IUserManagementListener;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

/**
 * 
 * @author Dennis Geesen Created at: 24.08.2011
 */
public class SinkViewPart extends ViewPart implements IDataDictionaryListener,
		IUserManagementListener {

	private TreeViewer viewer;

	@Override
	public void dispose() {
		IExecutor e = OdysseusRCPPlugIn.getExecutor();
		if (e instanceof IServerExecutor) {
			((IServerExecutor) e).getDataDictionary().removeListener(this);
		}
		super.dispose();
	}

	public TreeViewer getTreeViewer() {
		return viewer;
	}

	public void refresh() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					getTreeViewer().setInput(
							OdysseusRCPPlugIn.getExecutor().getSinks(
									OdysseusRCPPlugIn.getActiveSession()));
					// getTreeViewer().setInput(getDataDictionary().getStreamsAndViews(GlobalState.getActiveUser(OdysseusRCPPlugIn.RCP_USER_TOKEN)));
				} catch (Exception e) {
					getTreeViewer().setInput("NOTHING");
					e.printStackTrace();// ?
				}
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
	public void addedViewDefinition(AbstractDataDictionary sender, String name,
			ILogicalOperator op) {
		refresh();
	}

	@Override
	public void removedViewDefinition(AbstractDataDictionary sender,
			String name, ILogicalOperator op) {
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
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		setTreeViewer(new TreeViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.MULTI));
		getTreeViewer().setContentProvider(new SourcesViewContentProvider());
		getTreeViewer().setLabelProvider(new SourcesViewLabelProvider());
		refresh();
		IExecutor e = OdysseusRCPPlugIn.getExecutor();
		if (e instanceof IServerExecutor) {
			((IServerExecutor) e).getDataDictionary().addListener(this);
		}
		// UserManagement.getUsermanagement().addUserManagementListener(this);
		getSite().setSelectionProvider(getTreeViewer());

		// Contextmenu
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(getTreeViewer()
				.getControl());
		// Set the MenuManager
		getTreeViewer().getControl().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, getTreeViewer());

	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		refresh();
	}

}
