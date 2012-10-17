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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;

public class StreamEditor extends EditorPart {

	private IStreamEditorInput input;
	private IStreamEditorType editorType;
	private ToolBar toolBar;
	private Composite parent;
	
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
		this.parent = parent;
		
		GridLayout layout = new GridLayout();
		parent.setLayout(layout);
		
		createToolBar();
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		editorType.createPartControl(composite);
		
		input.getStreamConnection().connect();
	}
	
	protected final ToolItem createToolBarButton( Image  img) {
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(img);
		return item;
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

	private void createToolBar() {
		if( toolBar == null ) {
			toolBar = new ToolBar( getParent(), SWT.BORDER);
			fillToolBar( toolBar );
		}
	}
	
	protected final Composite getParent() {
		return parent;
	}
	
	protected void fillToolBar( ToolBar bar ) {
		final ToolItem button = createToolBarButton(OdysseusRCPViewerPlugIn.getImageManager().get("stopStream"));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( input.getStreamConnection().isConnected()) {
					input.getStreamConnection().disconnect();
					button.setImage(OdysseusRCPViewerPlugIn.getImageManager().get("startStream"));
				} else {
					input.getStreamConnection().connect();
					button.setImage(OdysseusRCPViewerPlugIn.getImageManager().get("stopStream"));
				}
			}
		});
		
		editorType.initToolbar(bar);
	}
}
