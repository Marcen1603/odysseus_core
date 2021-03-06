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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.rcp.viewer.OdysseusRCPViewerPlugIn;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorInput;
import de.uniol.inf.is.odysseus.rcp.viewer.extension.IStreamEditorType;

public abstract class AbstractStreamEditorList implements IStreamEditorType {

	static Logger LOG = LoggerFactory.getLogger(AbstractStreamEditorList.class);

	private static final int REFRESH_INTERVAL_MILLIS = 1000;

	private Text text;

	private int receivedElements;
	private final int maxElements;
	private boolean showHeartbeats = false;
	private StreamEditor editor;

	private List<String> pendingElements = Lists.newLinkedList();

	public AbstractStreamEditorList(int maxElements) {
		this.maxElements = maxElements;
	}

	@Override
	public void init(StreamEditor editorPart, IStreamEditorInput editorInput) {
		this.editor = editorPart;
	}

	@Override
	public void createPartControl(Composite parent) {
		boolean wrap = false;
		if (wrap) {
			text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		}else {
			text = new Text(parent, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		}
		text.setEditable(false);
		text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		// Thread, womit die Liste jeder Sekunde
		// automatisch aktualisiert wird.
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					Display disp = PlatformUI.getWorkbench().getDisplay();
					if (disp.isDisposed()) {
						return;
					}

					disp.asyncExec(new Runnable() {

						@Override
						public void run() {
							if (!text.isDisposed()) {
								refreshText();
							}
						}

					});

					waiting();
				}
			}

		});

		t.setName("StreamList Updater");
		t.start();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void setFocus() {
		text.setFocus();
	}

	@Override
	public void streamElementReceived(IPhysicalOperator senderOperator, Object element, int port) {
		synchronized (pendingElements) {
			pendingElements.add(element != null ? element.toString() : "null");
			if (!isInfinite() && pendingElements.size() > maxElements) {
				pendingElements.remove(0);
			}
		}
	}

	@Override
	public void punctuationElementReceived(IPhysicalOperator senderOperator, IPunctuation punctuation, int port) {
		synchronized (pendingElements) {
			if (!punctuation.isHeartbeat() || showHeartbeats) {
				pendingElements.add("Punctuation: " + punctuation);
				if (!isInfinite() && pendingElements.size() > maxElements) {
					pendingElements.remove(0);
				}
			}
		}
	}

	@Override
	public void initToolbar(ToolBar toolbar) {
		final ToolItem showHeartbeatsToolbarItem = new ToolItem(toolbar, SWT.CHECK);
        showHeartbeatsToolbarItem.setImage(OdysseusRCPViewerPlugIn.getImageManager().get("heart"));
        // showHeartbeatsToolbarItem.setText("\u2764");
		showHeartbeatsToolbarItem.setToolTipText("Toogle heartbeat printing");
		showHeartbeatsToolbarItem.setSelection(showHeartbeats);
		showHeartbeatsToolbarItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showHeartbeats = showHeartbeatsToolbarItem.getSelection();
			}
		});
	}

	private void refreshText() {
		synchronized (pendingElements) {
			if (pendingElements.isEmpty()) {
				return;
			}

			for (String element : pendingElements) {
				text.append(element + "\n");
				receivedElements++;
				if (!isInfinite() && receivedElements > maxElements) {
					String txt = text.getText();
					int pos = txt.indexOf("\n");
					txt = txt.substring(pos + 1);
					text.setText(txt);
					receivedElements--;
				}
			}
			text.setSelection(text.getCharCount());
			pendingElements.clear();
			editor.activateIfNeeded();
		}
	}

	private boolean isInfinite() {
		return maxElements < 0;
	}

	private static void waiting() {
		try {
			Thread.sleep(REFRESH_INTERVAL_MILLIS);
		} catch (InterruptedException e) {
		}
	}
}
