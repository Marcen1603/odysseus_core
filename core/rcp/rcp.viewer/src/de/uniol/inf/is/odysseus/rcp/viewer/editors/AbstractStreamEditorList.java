/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.viewer.editors;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;

public abstract class AbstractStreamEditorList implements IStreamEditorType {

	private TableViewer tableViewer;
	private IStreamEditorInput input;
	private StreamEditorListContentProvider contentProvider;
	private StreamEditorListLabelProvider labelProvider = new StreamEditorListLabelProvider();
	
	public AbstractStreamEditorList( int maxElements ) {
		Preconditions.checkArgument(maxElements > 0, "Maximum elements must be positive.");
		
		contentProvider = new StreamEditorListContentProvider(maxElements);
	}
	
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
	public void dispose() {
	}

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

	@Override
	public void initToolbar(ToolBar toolbar) {
	}
}
