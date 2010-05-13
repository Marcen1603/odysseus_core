package de.uniol.inf.is.odysseus.rcp.viewer.view.views;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.rcp.viewer.model.create.IModelManager;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IGraphModel;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusNodeModel;

public class ModelLabelProvider implements ILabelProvider  {

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if( element instanceof IModelManager<?>) return "ModelManager";
		if( element instanceof IGraphModel<?>) return "Graph";
		if( element instanceof IOdysseusNodeModel) return ((IOdysseusNodeModel)element).getName();
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {}

	@Override
	public void dispose() {}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {}

}
