package de.uniol.inf.is.odysseus.rcp.viewer.view.editor.impl;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.impl.OdysseusGraphView;

public class GraphViewEditorInput extends OdysseusGraphView implements IEditorInput {

	public GraphViewEditorInput(IOdysseusGraphModel data) {
		super(data);
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return getModelGraph().getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return getModelGraph().getName();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		if (obj instanceof GraphViewEditorInput) {
			return getModelGraph().equals(((GraphViewEditorInput)obj).getModelGraph());
		}
		return false;	
	}
	
	@Override
	public int hashCode() {
		return getModelGraph().hashCode();
	}
}
