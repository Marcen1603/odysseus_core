package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.outline.StreamMapEditorOutlineLabelProvider;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.outline.StreamMapEditorOutlineTreeContentProvider;


public class MapLayerView extends ViewPart{
	
//	private StreamMapEditor editor;
//	private LinkedList<ILayer> layerOrder;
//	private TreeViewer treeViewer;
	
	@Override
	public void createPartControl(Composite parent) {
		
		Label label = new Label(parent, SWT.None);
		label.setText("Test View");
		
	}

	@Override
	public void setFocus() {

	}


}
