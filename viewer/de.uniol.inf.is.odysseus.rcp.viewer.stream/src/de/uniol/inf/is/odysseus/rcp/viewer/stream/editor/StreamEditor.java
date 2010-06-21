package de.uniol.inf.is.odysseus.rcp.viewer.stream.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorType;

public class StreamEditor extends EditorPart {

	private IStreamEditorInput input;
	private IStreamEditorType editorType;
	
	public StreamEditor() {}

	@Override
	public void doSave(IProgressMonitor monitor) {}

	@Override
	public void doSaveAs() {}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		this.input = ((IStreamEditorInput) input);
		setPartName(this.input.getName());
		
		this.editorType = ((StreamEditorInput)this.input).getEditorType();
		this.editorType.init(this, this.input);
		
		this.input.getStreamConnection().addStreamElementListener(this.editorType);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		
		editorType.createPartControl(composite);
		
		input.getStreamConnection().connect();
	}

	@Override
	public void setFocus() {
		editorType.setFocus();
	}
	
	@Override
	public void dispose() {
		editorType.dispose();
		this.input.getStreamConnection().removeStreamElementListener(this.editorType);
		input.getStreamConnection().disconnect();
	}
	
	public IStreamEditorInput getInput() {
		return input;
	}

}
