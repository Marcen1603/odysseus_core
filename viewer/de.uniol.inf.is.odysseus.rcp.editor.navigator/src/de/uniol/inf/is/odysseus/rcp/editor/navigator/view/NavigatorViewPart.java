package de.uniol.inf.is.odysseus.rcp.editor.navigator.view;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ViewPart;

public class NavigatorViewPart extends ViewPart {

	private TreeViewer treeViewer;
	
	public NavigatorViewPart() {
	}

	@Override
	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.SINGLE);
		treeViewer.setContentProvider(new NavigatorContentProvider());
		treeViewer.setLabelProvider(new NavigatorLabelProvider());
		treeViewer.setInput(getWorkspace().getRoot());
		
		getSite().setSelectionProvider(treeViewer);
				
		getWorkspace().addResourceChangeListener(new IResourceChangeListener() {

			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						treeViewer.refresh();
					}
				});
			}
			
		});
		
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				ITreeSelection selection = (ITreeSelection)treeViewer.getSelection();
				Object element = selection.getFirstElement();
				if( element instanceof IContainer ) {
					TreePath[] treePath = selection.getPaths();
					if( !treeViewer.getExpandedState(treePath[0]) )
						treeViewer.expandToLevel(treePath[0], 1);
					else
						treeViewer.collapseToLevel(treePath[0], 1);
				} else if( element instanceof IFile ) {
					FileEditorInput input = new FileEditorInput((IFile)element);
					try {
						PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getActivePage().openEditor(input, "de.uniol.inf.is.odysseus.rcp.LogicalPlanEditor", true);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}
	
	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	public IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
}
