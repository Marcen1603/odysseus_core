package de.uniol.inf.is.odysseus.rcp.viewer.extension;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.rcp.viewer.editors.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.model.stream.IStreamElementListener;


public interface IStreamEditorType extends IStreamElementListener<Object> {

	public void init(StreamEditor editorPart, IStreamEditorInput editorInput);

	public void createPartControl(Composite parent);

	public void setFocus();

	public void dispose();

}
