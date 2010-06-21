package de.uniol.inf.is.odysseus.rcp.viewer.stream.list;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorType;

public class StreamEditorList implements IStreamEditorType {

	private TableViewer tableViewer;
	private IStreamEditorInput input;
	private StreamTableContentProvider contentProvider = new StreamTableContentProvider();
	private StreamTableLabelProvider labelProvider = new StreamTableLabelProvider();

	@Override
	public void createPartControl(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.NONE);
		tableViewer.setContentProvider(contentProvider);
		tableViewer.setLabelProvider(labelProvider);
		tableViewer.setInput(input);
	}

	@Override
	public void dispose() {}

	@Override
	public void init(IEditorPart editorPart, IStreamEditorInput editorInput) {
		this.input = editorInput;
	}

	@Override
	public void setFocus() {
		tableViewer.getControl().setFocus();
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		contentProvider.addElement(element.toString());
		if (PlatformUI.getWorkbench().getDisplay().isDisposed())
			return;

		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if(!tableViewer.getTable().isDisposed())
					tableViewer.refresh();
			}

		});
	}

}
