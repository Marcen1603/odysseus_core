package de.uniol.inf.is.odysseus.rcp.viewer.stream.list;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.editor.StreamEditor;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.extension.IStreamEditorType;

public class StreamEditorList implements IStreamEditorType {

	private TableViewer tableViewer;
	private IStreamEditorInput input;
	private StreamListContentProvider contentProvider = new StreamListContentProvider();
	private StreamListLabelProvider labelProvider = new StreamListLabelProvider();

	@Override
	public void createPartControl(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.BORDER);
		tableViewer.setContentProvider(contentProvider);
		tableViewer.setLabelProvider(labelProvider);
		tableViewer.setInput(input);

		// Thread, womit die Liste jeder Sekunde
		// automatisch aktualisiert wird.
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					if (PlatformUI.getWorkbench().getDisplay().isDisposed())
						return;

					PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							if (!tableViewer.getTable().isDisposed())
								tableViewer.refresh();
						}

					});

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		});
		
		t.start();
	}

	@Override
	public void dispose() {}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		this.input = editorInput;
	}

	@Override
	public void setFocus() {
		tableViewer.getControl().setFocus();
	}

	@Override
	public void streamElementRecieved(Object element, int port) {
		contentProvider.addElement(element.toString());
	}

	@Override
	public void punctuationElementRecieved(PointInTime point, int port) {
		contentProvider.addElement("PUNCTUATION: " + point);
	}

}
