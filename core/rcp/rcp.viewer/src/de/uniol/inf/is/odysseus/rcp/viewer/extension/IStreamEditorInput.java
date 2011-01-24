package de.uniol.inf.is.odysseus.rcp.viewer.extension;

import org.eclipse.ui.IEditorInput;

import de.uniol.inf.is.odysseus.rcp.viewer.model.stream.IStreamConnection;

public interface IStreamEditorInput extends IEditorInput{

	public IStreamConnection<Object> getStreamConnection();
}
