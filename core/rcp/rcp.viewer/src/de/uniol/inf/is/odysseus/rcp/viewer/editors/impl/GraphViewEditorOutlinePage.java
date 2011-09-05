/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.viewer.editors.impl;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class GraphViewEditorOutlinePage extends ContentOutlinePage implements ISelectionListener {

	private PhysicalGraphEditorInput input;

	public GraphViewEditorOutlinePage(PhysicalGraphEditorInput input) {
		this.input = input;
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);

		TreeViewer viewer = getTreeViewer();
		viewer.setContentProvider(new GraphOutlineContentProvider());
		viewer.setLabelProvider(new GraphOutlineLabelProvider());
		viewer.addSelectionChangedListener(this);
		viewer.setInput(input.getGraphView());

		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);

		MenuManager manager = new MenuManager(OdysseusRCPViewerPlugIn.OUTLINE_CONTEXT_MENU_ID, OdysseusRCPViewerPlugIn.OUTLINE_CONTEXT_MENU_ID);
		manager.setRemoveAllWhenShown(true);
		Menu menu = manager.createContextMenu(viewer.getControl());
		viewer.getTree().setMenu(menu);

		IPageSite site = getSite();
		site.registerContextMenu(OdysseusRCPViewerPlugIn.OUTLINE_CONTEXT_MENU_ID, manager, viewer);
	}

	@Override
	public void makeContributions(IMenuManager menuManager, IToolBarManager toolBarManager, IStatusLineManager statusLineManager) {
		toolBarManager.add(new Action() {
			@Override
			public void run() {
				refresh();
			}

			@Override
			public String getToolTipText() {
				return "Refresh All";
			}

			@Override
			public ImageDescriptor getImageDescriptor() {
				return OdysseusRCPViewerPlugIn.getImageDescriptor("icons/refresh.gif");
			}
		});
	}

	public void refresh() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					getTreeViewer().refresh();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		});
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof ContentOutline)
			return;

		if (selection instanceof IStructuredSelection) {
			if (((IStructuredSelection) selection).getFirstElement() instanceof IOdysseusNodeView || ((IStructuredSelection) selection).getFirstElement() instanceof IOdysseusGraphView) {

				getTreeViewer().setSelection(selection);
			}
		}
	}
}
