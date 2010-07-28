package de.uniol.inf.is.odysseus.rcp.viewer.stream.extension;

import org.eclipse.ui.IEditorInput;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.INodeModel;

public interface IStreamEditorInput extends IEditorInput{

	public INodeModel<IPhysicalOperator> getNodeModel();
	public IStreamConnection<Object> getStreamConnection();
}
