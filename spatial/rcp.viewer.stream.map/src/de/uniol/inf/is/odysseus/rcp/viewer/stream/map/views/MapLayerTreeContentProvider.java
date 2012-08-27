package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.views;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.StreamMapEditor;

public class MapLayerTreeContentProvider implements ITreeContentProvider {

	private StreamMapEditor editor;
	
	public MapLayerTreeContentProvider(StreamMapEditor editor) {
		this.editor = editor;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getChildren(Object arg0) {
		return null;
	}

	@Override
	public Object[] getElements(Object arg0) {
		Object objects[] = new Object[1];
		objects[0] = editor.getLayerOrder();	
		return objects;
	}

	@Override
	public Object getParent(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
