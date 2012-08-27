package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.activator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class StreamEditorContentProvider implements IStructuredContentProvider {

	private Object input = null;

	public StreamEditorContentProvider() {
		super();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInObject,
			Object newInObject) {
		input = newInObject;
		System.out.println(input.toString());
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (input == null)
			return null;

		System.out.println(inputElement.toString());
		
		return null;
	}

}
