package de.uniol.inf.is.odysseus.rcp.editor.text.pql;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class PQLOperatorView extends ViewPart {

	private TreeViewer treeViewer;
	
	@Override
	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent);
		treeViewer.setContentProvider(new PQLOperatorsContentProvider());
		treeViewer.setLabelProvider(new PQLOperatorsLabelProvider());
		
		treeViewer.setInput(determineInput());
	}

	@Override
	public void setFocus() {
		treeViewer.getTree().setFocus();
	}

	private Object determineInput() {
		return PQLEditorTextPlugIn.getOperatorBuilderFactory().getOperatorBuilder();
	}
}
