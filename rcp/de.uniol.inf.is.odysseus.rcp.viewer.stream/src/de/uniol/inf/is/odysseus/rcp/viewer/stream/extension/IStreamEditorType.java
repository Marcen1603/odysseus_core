package de.uniol.inf.is.odysseus.rcp.viewer.stream.extension;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;


public interface IStreamEditorType extends IStreamElementListener<Object> {

	public void init(IEditorPart editorPart, IStreamEditorInput editorInput);

	public void createPartControl(Composite parent);

	public void setFocus();

	public void dispose();

}
