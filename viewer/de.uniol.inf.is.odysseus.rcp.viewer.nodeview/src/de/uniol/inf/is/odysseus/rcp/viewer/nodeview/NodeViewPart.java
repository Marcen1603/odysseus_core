package de.uniol.inf.is.odysseus.rcp.viewer.nodeview;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.view.editor.IGraphViewEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.IGraphView;

public class NodeViewPart extends ViewPart implements ISelectionListener {

	public static final String VIEW_ID = "de.uniol.inf.is.odysseus.rcp.viewer.nodeview.NodeView";

	private TreeViewer treeViewer;
	private boolean synched = false;
	private ISelection selection = null;

	@Override
	public void createPartControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		treeViewer = new TreeViewer(composite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE);
		treeViewer.setContentProvider(new NodeViewContentProvider());
		treeViewer.setLabelProvider(new NodeViewLabelProvider());
		treeViewer.setInput(null);
			
		// auf Editor achten
		getSite().getPage().addPartListener(new IPartListener() {

			@Override
			public void partActivated(IWorkbenchPart part) {
				if( part instanceof IGraphViewEditor ) {
					IGraphView<IPhysicalOperator> graph = ((IGraphViewEditor)part).getGraphView();
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
					IGraphView<IPhysicalOperator> graph = ((IGraphViewEditor)part).getGraphView();
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
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(treeViewer.getTree());
		// Set the MenuManager
		treeViewer.getTree().setMenu(menu);
		getSite().registerContextMenu(menuManager, treeViewer);
		
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}
	
	public TreeViewer getTreeViewer() {
		return treeViewer;
	}
	
	public void setSync( boolean sync ) {
		this.synched = sync;
		if( this.synched ) {
			getSite().setSelectionProvider(treeViewer);
			getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
			treeViewer.setSelection(this.selection);
		} else {
			getSite().setSelectionProvider(null);
			getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		}
	}
	
	public boolean getSync() {
		return synched;
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if( this.synched && part != this )  {
			treeViewer.setSelection(selection);
		}
		
		// einfach speichern..
		if( !this.synched )
			this.selection = selection;
	}
}
