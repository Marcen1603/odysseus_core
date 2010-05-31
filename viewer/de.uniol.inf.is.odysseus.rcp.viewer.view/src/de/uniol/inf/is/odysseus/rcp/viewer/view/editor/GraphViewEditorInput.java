package de.uniol.inf.is.odysseus.rcp.viewer.view.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.impl.DefaultGraphView;

public class GraphViewEditorInput extends DefaultGraphView<IPhysicalOperator> implements IEditorInput {

	public GraphViewEditorInput(IGraphModel<IPhysicalOperator> data) {
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

	@SuppressWarnings("unchecked")
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
