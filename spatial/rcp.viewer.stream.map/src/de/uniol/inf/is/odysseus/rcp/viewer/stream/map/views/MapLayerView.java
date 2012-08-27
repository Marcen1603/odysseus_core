package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views;

import java.util.LinkedList;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.ILayer;


public class MapLayerView extends ViewPart{
	
	private StreamMapEditor editor;
	private LinkedList<ILayer> layerOrder;
	private TreeViewer treeViewer;
	
	
	public MapLayerView(StreamMapEditor editor){
		super();
		this.editor = editor;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		new MapLayerTreeContentProvider(editor);
		Label label = new Label(parent, SWT.None);
		label.setText("Test View");
		
		//getSite().setSelectionProvider();
		
	}

	@Override
	public void setFocus() {

	}


}
