package de.uniol.inf.is.odysseus.rcp.viewer.nodeview.impl;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.rcp.viewer.nodeview.INodeViewPart;
import de.uniol.inf.is.odysseus.rcp.viewer.view.editor.IGraphViewEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusGraphView;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IOdysseusNodeView;

public class NodeViewPart extends ViewPart implements INodeViewPart, ISelectionListener {

	private TreeViewer treeViewer;
	private boolean synched = false;
	private ISelection selection = null;
	private MenuManager menuManager = new MenuManager();
	private Menu contextMenu;

	@Override
	public void createPartControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		treeViewer = new TreeViewer(composite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE);
		treeViewer.setContentProvider(new NodeViewContentProvider());
		treeViewer.setLabelProvider(new NodeViewLabelProvider());
		treeViewer.setInput(null);
			
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		getSite().setSelectionProvider(treeViewer);
		
		// auf Editor achten
		getSite().getPage().addPartListener(new IPartListener() {

			@Override
			public void partActivated(IWorkbenchPart part) {
				if( part instanceof IGraphViewEditor ) {
					IOdysseusGraphView graph = ((IGraphViewEditor)part).getGraphView();
					if( treeViewer.getInput() != graph ) {
						treeViewer.setInput(graph);
						treeViewer.refresh();
					}
				}
			}

			@Override
			public void partBroughtToTop(IWorkbenchPart part) {}

			@Override
			public void partClosed(IWorkbenchPart part) {
				if( part instanceof IGraphViewEditor ) {
					IOdysseusGraphView graph = ((IGraphViewEditor)part).getGraphView();
					if( treeViewer.getInput() == graph ) {
						treeViewer.setInput(null);
						treeViewer.refresh();
					}
				}
			}

			@Override
			public void partDeactivated(IWorkbenchPart part) {
			}

			@Override
			public void partOpened(IWorkbenchPart part){}
			
		});

		// Contextmenu
		contextMenu = menuManager.createContextMenu(treeViewer.getTree());
		// Set the MenuManager
		treeViewer.getTree().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, treeViewer);
		
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}
	
	@Override
	public TreeViewer getTreeViewer() {
		return treeViewer;
	}
	
	@Override
	public void setSync( boolean sync ) {
		this.synched = sync;
		if( this.synched ) {
			treeViewer.setSelection(this.selection);
		} else {
		}
	}
	
	@Override
	public MenuManager getContextMenu() {
		return menuManager;
	}
	
	@Override
	public boolean getSync() {
		return synched;
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if( selection instanceof IStructuredSelection ) {
			if( ((IStructuredSelection) selection).getFirstElement() instanceof IOdysseusNodeView ||
				((IStructuredSelection) selection).getFirstElement() instanceof IOdysseusGraphView) {
				
				if( this.synched && part != this )  {
					treeViewer.setSelection(selection);
				}
				
				// einfach speichern..
				if( !this.synched )
					this.selection = selection;
				
			}
		}
	}
}
