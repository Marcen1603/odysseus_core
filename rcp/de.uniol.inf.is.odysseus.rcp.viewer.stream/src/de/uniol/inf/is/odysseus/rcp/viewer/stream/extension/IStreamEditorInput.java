package de.uniol.inf.is.odysseus.rcp.viewer.stream.extension;

import org.eclipse.ui.IEditorInput;

public interface IStreamEditorInput extends IEditorInput{

	public IStreamConnection<Object> getStreamConnection();
}
