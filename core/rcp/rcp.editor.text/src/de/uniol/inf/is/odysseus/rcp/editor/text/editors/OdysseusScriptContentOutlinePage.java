package de.uniol.inf.is.odysseus.rcp.editor.text.editors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

public class OdysseusScriptContentOutlinePage extends ContentOutlinePage {

	private String queryText;
	
	public OdysseusScriptContentOutlinePage( String queryText ) {
		super();
		this.queryText = queryText;
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		
		TreeViewer treeViewer = getTreeViewer();
		
		treeViewer.setContentProvider(new OdysseusScriptContentProvider());
		treeViewer.setLabelProvider(new OdysseusScriptLabelProvider());
		treeViewer.addSelectionChangedListener(this);
		treeViewer.setInput(new StringTreeRoot(queryText));
	}
}
