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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.editors.IGraphViewEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.position.impl.SugiyamaPositioner;
import de.uniol.inf.is.odysseus.rcp.viewer.select.ISelectListener;
import de.uniol.inf.is.odysseus.rcp.viewer.select.ISelector;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.render.SWTRenderManager;
import de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol.SWTSymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.symbol.ISymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.IOdysseusNodeView;

public class GraphViewEditor extends EditorPart implements IGraphViewEditor, ISelectListener<INodeView<IPhysicalOperator>>, ISelectionProvider, ISelectionListener {

	private static final ISymbolElementFactory<IPhysicalOperator> SYMBOL_FACTORY = new SWTSymbolElementFactory<IPhysicalOperator>();


	private PhysicalGraphEditorInput input;
	private SWTRenderManager<IPhysicalOperator> renderManager;
	private GraphViewEditorOutlinePage outlinePage;
	private QueryIDChecker checker;

	private final Collection<ISelectionChangedListener> listeners = new ArrayList<ISelectionChangedListener>();

	public GraphViewEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, final IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		this.input = ((PhysicalGraphEditorInput) input);
		setPartName(this.input.getName());
		if( this.input.hasQueryID() ) {
			checker = new QueryIDChecker(this, this.input);
			checker.start();
		}
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {

		Composite canvasComposite = new Composite(parent, SWT.BORDER);
		canvasComposite.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
		canvasComposite.setLayout(new FillLayout());

		renderManager = new SWTRenderManager<IPhysicalOperator>(canvasComposite, new SugiyamaPositioner(SYMBOL_FACTORY));
		renderManager.setDisplayedGraph(input.getGraphView());
		renderManager.resetPositions();
		renderManager.getSelector().addSelectListener(this);
		
		getSite().setSelectionProvider(this);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		
		// Contextmen� registrieren
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(renderManager.getCanvas());
		// Set the MenuManager
		renderManager.getCanvas().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, this);

	}

	@Override
	public void dispose() {
		if( checker != null ) {
			checker.stopRunning();
			checker = null;
		}
		
		getSite().setSelectionProvider(null);
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		renderManager.dispose();
		super.dispose();
	}

	@Override
	public void setFocus() {
		if (renderManager != null)
			renderManager.getCanvas().setFocus();
	}

	@Override
	public void selectObject(ISelector<INodeView<IPhysicalOperator>> sender, Collection<? extends INodeView<IPhysicalOperator>> selected) {
		setSelection(createSelection());
	}

	@Override
	public void unselectObject(ISelector<INodeView<IPhysicalOperator>> sender, Collection<? extends INodeView<IPhysicalOperator>> unselected) {
		setSelection(createSelection());
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	@Override
	public ISelection getSelection() {
		return createSelection();
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void setSelection(ISelection selection) {
		for (ISelectionChangedListener l : listeners) {
			if (l != null)
				l.selectionChanged(new SelectionChangedEvent(this, selection));
		}

	}

	private ISelection createSelection() {
		Collection<INodeView<IPhysicalOperator>> selected = renderManager.getSelector().getSelected();
		return new StructuredSelection(selected.toArray());
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {

		if (part != this) {
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection structSelection = (IStructuredSelection) selection;
				List<?> selectedObjects = structSelection.toList();
				Collection<IOdysseusNodeView> selectedNodes = new ArrayList<IOdysseusNodeView>();
				for (Object obj : selectedObjects) {
					if (obj instanceof IOdysseusNodeView) {
						IOdysseusNodeView nodeView = (IOdysseusNodeView) obj;
						selectedNodes.add(nodeView);
					} else if( obj instanceof IPhysicalOperator ) {
						IPhysicalOperator op = (IPhysicalOperator) obj;
						for( INodeView<IPhysicalOperator> nodeView : getGraphView().getViewedNodes()) {
							if(nodeView.getModelNode() != null && nodeView.getModelNode().getContent() != null && nodeView.getModelNode().getContent().equals(op)) {
								selectedNodes.add((IOdysseusNodeView) nodeView);
							}
						}
					}
				}
				renderManager.getSelector().unselectAll();
				renderManager.getSelector().select(selectedNodes);
			}
		}
	}

	@Override
	public IOdysseusGraphModel getGraphModel() {
		return input.getGraphModel();
	}

	@Override
	public IOdysseusGraphView getGraphView() {
		return input.getGraphView();
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			if (outlinePage == null) {
				outlinePage = new GraphViewEditorOutlinePage(this.input);
			}
			return outlinePage;
		}
		return super.getAdapter(adapter);
	}
	
	public void restructureGraph() {
		renderManager.resetPositions();
	}
	
	public void center(IOdysseusNodeView view ) {
		renderManager.center(view);
	}
	
	public void refresh() {
		renderManager.refreshView();
	}
}
