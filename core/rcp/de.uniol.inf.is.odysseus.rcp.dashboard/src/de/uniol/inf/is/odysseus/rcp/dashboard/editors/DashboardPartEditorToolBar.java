/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.dashboard.controller.DashboardPartController;

public class DashboardPartEditorToolBar {

	private final ToolBar toolBar;
	private final ToolItem startStopItem;
	private final ToolItem pauseUnpauseItem;

	private final DashboardPartEditor editor;

	public DashboardPartEditorToolBar(Composite presentationTab, final DashboardPartEditor editor) {

		Preconditions.checkNotNull(presentationTab, "PartController must not be null!");
		this.editor = Preconditions.checkNotNull(editor, "PartController must not be null!");

		final DashboardPartController partController = editor.getDashboardPartController();

		toolBar = new ToolBar(presentationTab, SWT.WRAP | SWT.RIGHT);

		startStopItem = createToolItem(toolBar, "Stop");
		startStopItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (startStopItem.getText().equals("Stop")) {
					partController.stop();
					setStatusToStopped();
				} else {
					try {
						partController.start();
					} catch (final Exception ex) {
						throw new RuntimeException("Could not start DashboardPart", ex);
					}

					setStatusToStarted();
				}
			}
		});

		pauseUnpauseItem = createToolItem(toolBar, "Pause");
		pauseUnpauseItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (pauseUnpauseItem.getText().equals("Pause")) {
					partController.pause();
					setStatusToPaused();
				} else {
					partController.unpause();
					setStatusToResumed();
				}
			}

		});
	}

	public ToolBar getToolBar() {
		return toolBar;
	}

	public void setStatusToPaused() {
		pauseUnpauseItem.setText("Res.");
		editor.setPartNameSuffix("Paused");
	}

	public void setStatusToResumed() {
		pauseUnpauseItem.setText("Pause");
		editor.setPartNameSuffix(null);
	}

	public void setStatusToStarted() {
		startStopItem.setText("Stop");
		editor.setPartNameSuffix(null);
	}

	public void setStatusToStopped() {
		startStopItem.setText("Start");
		editor.setPartNameSuffix("Stopped");
	}

	private static ToolItem createToolItem(ToolBar tb, String title) {
		final ToolItem toolItem = new ToolItem(tb, SWT.PUSH);
		toolItem.setText(title);
		toolItem.setWidth(100);
		return toolItem;
	}
}
