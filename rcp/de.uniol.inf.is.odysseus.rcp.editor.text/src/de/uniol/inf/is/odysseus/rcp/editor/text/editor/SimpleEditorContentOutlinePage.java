package de.uniol.inf.is.odysseus.rcp.editor.text.editor;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

public class SimpleEditorContentOutlinePage extends ContentOutlinePage {

	private String queryText;
	
	public SimpleEditorContentOutlinePage( String queryText ) {
		super();
		this.queryText = queryText;
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		
		TreeViewer treeViewer = getTreeViewer();
		
		treeViewer.setContentProvider(new SimpleContentProvider());
		treeViewer.setLabelProvider(new SimpleLabelProvider());
		treeViewer.addSelectionChangedListener(this);
		treeViewer.setInput(new StringTreeRoot(queryText));
	}
}
