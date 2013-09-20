/*******************************************************************************
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.editor.graph.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.editparts.factories.GraphEditPartFactory;
import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.Graph;

/**
 * @author DGeesen
 * 
 */
public class OperatorGraphEditor extends GraphicalEditorWithFlyoutPalette {

	private Graph graph;
	private DefaultEditDomain editDomain;	

	public OperatorGraphEditor() {
		editDomain = new DefaultEditDomain(this);
		setEditDomain(editDomain);
		graph = new Graph();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		initGraphEditorListener();

	}

	private void initGraphEditorListener() {
		// editDomain.getCommandStack().addCommandStackListener(new GraphEditorListener(actionRegistry));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	@Override
	protected void configureGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new GraphEditPartFactory());
		viewer.setContents(graph);
	}

	// /**
	// * @param form
	// */
	// private void createPaletteViewer(Composite parent) {
	// PaletteViewer viewer = new PaletteViewer();
	// viewer.createControl(parent);
	// editDomain.setPaletteViewer(viewer);
	// editDomain.setPaletteRoot(GraphPalette.createGraphPalette());
	//
	// }
	//
	//
	// /**
	// * @param form
	// */
	// private void createGraphViewer(Composite parent) {
	// ScrollingGraphicalViewer viewer = new ScrollingGraphicalViewer();
	// viewer.createControl(parent);
	// viewer.setRootEditPart(new ScalableFreeformRootEditPart());
	// viewer.getControl().setBackground(ColorConstants.white);
	// viewer.setEditPartFactory(new GraphEditPartFactory());
	//
	// viewer.setContents(graph);
	// editDomain.addViewer(viewer);
	//
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot()
	 */
	@Override
	protected PaletteRoot getPaletteRoot() {
		return GraphPalette.createGraphPalette();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {

	}

	public EditDomain getCurrentEditDomain() {
		return this.editDomain;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * @return
	 */
	public ActionRegistry getCurrentActionRegistry() {
		return getActionRegistry();
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		updateActions(getSelectionActions());
	}
	
}
