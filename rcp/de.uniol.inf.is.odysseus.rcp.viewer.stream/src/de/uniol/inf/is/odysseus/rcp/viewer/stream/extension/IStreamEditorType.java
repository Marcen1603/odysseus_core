package de.uniol.inf.is.odysseus.rcp.viewer.stream.extension;

import org.eclipse.swt.widgets.Composite;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.editor.StreamEditor;


public interface IStreamEditorType extends IStreamElementListener<Object> {

	public void init(StreamEditor editorPart, IStreamEditorInput editorInput);

	public void createPartControl(Composite parent);

	public void setFocus();

	public void dispose();

}
