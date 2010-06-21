package de.uniol.inf.is.odysseus.rcp.viewer.stream.list;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class StreamTableContentProvider implements IStructuredContentProvider {

	private List<String> elements = new LinkedList<String>();
	
	@Override
	public Object[] getElements(Object inputElement) {
		return elements.toArray();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public void addElement(String element) {
		elements.add(0, element);
	}

}
