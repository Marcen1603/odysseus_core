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
package de.uniol.inf.is.odysseus.rcp.viewer.editors.impl;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class GraphViewEditorOutlinePage extends ContentOutlinePage implements ISelectionListener {

	private static final Logger LOG = LoggerFactory.getLogger(GraphViewEditorOutlinePage.class);

	private static GraphViewEditorOutlinePage instance;

	private final PhysicalGraphEditorInput input;
	
	private TreeViewer viewer;

	public GraphViewEditorOutlinePage(PhysicalGraphEditorInput input) {
		this.input = input;
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);

		viewer = getTreeViewer();
		viewer.setContentProvider(new GraphOutlineContentProvider());
		viewer.setLabelProvider(new GraphOutlineLabelProvider());
		viewer.addSelectionChangedListener(this);
		viewer.setInput(input.getGraphView());

		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);

		final MenuManager manager = new MenuManager(OdysseusRCPViewerPlugIn.OUTLINE_CONTEXT_MENU_ID, OdysseusRCPViewerPlugIn.OUTLINE_CONTEXT_MENU_ID);
		manager.setRemoveAllWhenShown(true);
		final Menu menu = manager.createContextMenu(viewer.getControl());
		viewer.getTree().setMenu(menu);

		final IPageSite site = getSite();
		site.registerContextMenu(OdysseusRCPViewerPlugIn.OUTLINE_CONTEXT_MENU_ID, manager, viewer);
		
		instance = this;
	}

	@Override
	public void dispose() {
		instance = null;

		super.dispose();
	}
	
	public final void setSorting( boolean doSorting ) {
		if( doSorting ) {
			viewer.setSorter(new ViewerSorter());
		} else {
			viewer.setSorter(null);
		}
	}
	
	public final boolean isSorting() {
		return viewer.getSorter() != null;
	}

	@Override
	public void makeContributions(IMenuManager menuManager, IToolBarManager toolBarManager, IStatusLineManager statusLineManager) {
		toolBarManager.add(new Action() {
			@Override
			public ImageDescriptor getImageDescriptor() {
				return OdysseusRCPViewerPlugIn.getImageDescriptor("icons/refresh.gif");
			}

			@Override
			public String getToolTipText() {
				return "Refresh";
			}

			@Override
			public void run() {
				refresh();
			}
		});
		
		toolBarManager.add(new Action() {
			
			@Override
			public int getStyle() {
				return IAction.AS_CHECK_BOX;
			}
			
			@Override
			public ImageDescriptor getImageDescriptor() {
				return OdysseusRCPViewerPlugIn.getImageDescriptor("icons/sortAlpha.gif");
			}

			@Override
			public String getToolTipText() {
				return "Sort alphabetically";
			}

			@Override
			public void run() {
				setSorting(!isSorting());
				setChecked(isSorting());
			}
		});

		toolBarManager.add(new Action() {
			@Override
			public ImageDescriptor getImageDescriptor() {
				return OdysseusRCPViewerPlugIn.getImageDescriptor("icons/expandall.gif");
			}

			@Override
			public String getToolTipText() {
				return "Expand all";
			}

			@Override
			public void run() {
				viewer.expandAll();
			}
		});

		toolBarManager.add(new Action() {
			@Override
			public ImageDescriptor getImageDescriptor() {
				return OdysseusRCPViewerPlugIn.getImageDescriptor("icons/collapseall.gif");
			}

			@Override
			public String getToolTipText() {
				return "Collapse all";
			}

			@Override
			public void run() {
				viewer.collapseAll();
			}
		});
	}

	public void refresh() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					getTreeViewer().refresh();
				} catch (final Exception ex) {
					LOG.error("Could not refresh tree viewer in outline", ex);
				}
			}

		});
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (part instanceof ContentOutline) {
			return;
		}

		if (selection instanceof IStructuredSelection) {
			final Object selectedObject = ((IStructuredSelection) selection).getFirstElement();
			if (selectedObject instanceof IOdysseusNodeView || selectedObject instanceof IOdysseusGraphView) {
				getTreeViewer().setSelection(selection);
			} else if (selectedObject instanceof IPhysicalOperator) {
				final Optional<IOdysseusNodeView> optNodeView = findNodeView((IPhysicalOperator) selectedObject, input.getGraphView());
				if (optNodeView.isPresent()) {
					getTreeViewer().setSelection(new StructuredSelection(optNodeView.get()));
				}
			}
		}
	}

	public static Optional<GraphViewEditorOutlinePage> getInstance() {
		return Optional.fromNullable(instance);
	}

	private static Optional<IOdysseusNodeView> findNodeView(IPhysicalOperator selectedObject, IOdysseusGraphView graph) {
		for (final INodeView<IPhysicalOperator> nodeView : graph.getViewedNodes()) {
			if (nodeView.getModelNode() != null && nodeView.getModelNode().getContent() != null) {
				if (nodeView.getModelNode().getContent().equals(selectedObject)) {
					return Optional.of((IOdysseusNodeView) nodeView);
				}
			}
		}
		return Optional.absent();
	}
}
