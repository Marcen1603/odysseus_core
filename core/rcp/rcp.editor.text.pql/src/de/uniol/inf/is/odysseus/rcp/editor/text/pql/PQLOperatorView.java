package de.uniol.inf.is.odysseus.rcp.editor.text.pql;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class PQLOperatorView extends ViewPart {

	private TreeViewer treeViewer;
	private boolean showOptionalParameters = true;
	private PQLOperatorsContentProvider contentProvider;
	
	@Override
	public void createPartControl(Composite parent) {
		contentProvider = new PQLOperatorsContentProvider();
		
		treeViewer = new TreeViewer(parent);
		treeViewer.setContentProvider(contentProvider);
		treeViewer.setLabelProvider(new PQLOperatorsLabelProvider());
		
		treeViewer.setInput(determineInput());
	}

	@Override
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}
	
	public void refresh() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				treeViewer.refresh();
			}
		});
	}
	
	public void expandAll() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				treeViewer.expandAll();
			}
		});
		
	}
	
	public void collapseAll() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				treeViewer.collapseAll();
			}
			
		});
	}
	
	public void toggleShowOptionalParameters() {
		showOptionalParameters = !showOptionalParameters;
		contentProvider.showOptionalParameters(showOptionalParameters);
		refresh();
	}

	private Object determineInput() {
		return PQLEditorTextPlugIn.getOperatorBuilderFactory().getOperatorBuilder();
	}
}
