package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.activator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamEditorContentProvider implements IStructuredContentProvider {

	static Logger LOG = LoggerFactory.getLogger(StreamEditorContentProvider.class);
	
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
		LOG.debug(input.toString());
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (input == null)
			return null;

		LOG.debug(inputElement.toString());
		
		return null;
	}

}
