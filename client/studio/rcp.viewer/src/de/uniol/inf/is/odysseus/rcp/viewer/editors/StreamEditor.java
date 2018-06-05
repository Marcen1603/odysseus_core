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
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;

public class StreamEditor extends EditorPart {

	private static final Logger LOG = LoggerFactory.getLogger(StreamEditor.class);
	
	private IStreamEditorInput input;
	private IStreamEditorType editorType;
	private ToolBar toolBar;
	private Composite parent;

	private boolean autoActivate = false;
	private boolean onActivating = false;

	public StreamEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		this.input = ((IStreamEditorInput) input);
		setPartName(this.input.getName());

		this.editorType = ((StreamEditorInput) this.input).getEditorType();
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

	@Override
	public void setFocus() {
		editorType.setFocus();

	}

	@Override
	public void dispose() {
		editorType.dispose();
		this.input.getStreamConnection().removeStreamElementListener(this.editorType);
		
		input.getStreamConnection().disconnect();
		
		removeManagedQuery();
	}

	private void removeManagedQuery() {
		if( input.getManagedQueryID().isPresent() ) {
			int queryIDToRemove = input.getManagedQueryID().get();
			try {
				OdysseusRCPViewerPlugIn.getExecutor().removeQuery(queryIDToRemove, OdysseusRCPPlugIn.getActiveSession());
			} catch ( Throwable t ) {
				LOG.error("Could not remove query " + queryIDToRemove, t);
			}
		}
	}

	public IStreamEditorInput getInput() {
		return input;
	}

	public void activateIfNeeded() {
		if (!autoActivate || onActivating) {
			return;
		}

		onActivating = true;
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchPage page = getSite().getPage();
				page.activate(StreamEditor.this);
				onActivating = false;
			}
		});

	}

	protected void fillToolBar(ToolBar bar) {
		final ToolItem startButton = createToolBarButton(bar, OdysseusRCPViewerPlugIn.getImageManager().get("startStream"));
		final ToolItem stopButton = createToolBarButton(bar, OdysseusRCPViewerPlugIn.getImageManager().get("stopStream"));

		startButton.setToolTipText("Start");
		startButton.setEnabled(false);
		startButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				input.getStreamConnection().connect();
				startButton.setEnabled(false);
				stopButton.setEnabled(true);
			}
		});
		stopButton.setToolTipText("Stop");
		stopButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				input.getStreamConnection().disconnect();
				stopButton.setEnabled(false);
				startButton.setEnabled(true);
			}
		});
		final ToolItem autoShowButton = new ToolItem(bar, SWT.CHECK);
		autoShowButton.setImage(OdysseusRCPViewerPlugIn.getImageManager().get("autoFocusActivate"));
		autoShowButton.setToolTipText("Show on changes");
		autoShowButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				autoActivate = !autoActivate;
				autoShowButton.setSelection(autoActivate);
			}
		});

		editorType.initToolbar(bar);
	}

	protected final Composite getParent() {
		return parent;
	}

	private void createToolBar() {
		if (toolBar == null) {
			toolBar = new ToolBar(getParent(), SWT.BORDER);
			fillToolBar(toolBar);
		}
	}

	private static final ToolItem createToolBarButton(ToolBar tb, Image img) {
		ToolItem item = new ToolItem(tb, SWT.PUSH);
		item.setImage(img);
		return item;
	}
}
