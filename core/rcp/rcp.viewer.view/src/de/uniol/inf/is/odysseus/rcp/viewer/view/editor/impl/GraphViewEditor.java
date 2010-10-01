package de.uniol.inf.is.odysseus.rcp.viewer.view.editor.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.editor.IGraphViewEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.INodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusNodeView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.position.impl.SugiyamaPositioner;
import de.uniol.inf.is.odysseus.rcp.viewer.view.select.ISelectListener;
import de.uniol.inf.is.odysseus.rcp.viewer.view.select.ISelector;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.render.SWTRenderManager;
import de.uniol.inf.is.odysseus.rcp.viewer.view.swt.symbol.SWTSymbolElementFactory;
import de.uniol.inf.is.odysseus.rcp.viewer.view.symbol.ISymbolElementFactory;

public class GraphViewEditor extends EditorPart implements IGraphViewEditor, ISelectListener<INodeView<IPhysicalOperator>>, ISelectionProvider, ISelectionListener {

	public static final String EDITOR_ID = "de.uniol.inf.is.odysseus.rcp.viewer.view.GraphEditor";
	private static final ISymbolElementFactory<IPhysicalOperator> SYMBOL_FACTORY = new SWTSymbolElementFactory<IPhysicalOperator>();


	private PhysicalGraphEditorInput input;
	private SWTRenderManager<IPhysicalOperator> renderManager;
	private GraphViewEditorOutlinePage outlinePage;

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
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		this.input = ((PhysicalGraphEditorInput) input);
		setPartName(this.input.getName());
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
		canvasComposite.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		canvasComposite.setLayout(new FillLayout());

		renderManager = new SWTRenderManager<IPhysicalOperator>(canvasComposite, new SugiyamaPositioner(SYMBOL_FACTORY));
		renderManager.setDisplayedGraph(input.getGraphView());
		renderManager.resetPositions();
		renderManager.getSelector().addSelectListener(this);

		getSite().setSelectionProvider(this);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
	}

	@Override
	public void dispose() {
		getSite().setSelectionProvider(null);
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
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

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (IContentOutlinePage.class.equals(adapter)) {
			if (outlinePage == null) {
				outlinePage = new GraphViewEditorOutlinePage(this.input);
			}
			return outlinePage;
		}
		return super.getAdapter(adapter);
	}
}
