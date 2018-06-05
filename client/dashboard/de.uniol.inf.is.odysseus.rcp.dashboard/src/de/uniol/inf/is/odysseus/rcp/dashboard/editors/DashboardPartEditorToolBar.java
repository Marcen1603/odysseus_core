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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.dashboard.DashboardPlugIn;
import de.uniol.inf.is.odysseus.rcp.dashboard.controller.DashboardPartController;

public class DashboardPartEditorToolBar {

	private final ToolBar toolBar;
	private final ToolItem startItem;
	private final ToolItem stopItem;
	private final ToolItem pauseItem;
	private final ToolItem resumeItem;

	private final DashboardPartEditor editor;

	public DashboardPartEditorToolBar(Composite presentationTab, final DashboardPartEditor editor) {
		Preconditions.checkNotNull(presentationTab, "PartController must not be null!");
		this.editor = Preconditions.checkNotNull(editor, "PartController must not be null!");

		final DashboardPartController partController = editor.getDashboardPartController();

		toolBar = new ToolBar(presentationTab, SWT.WRAP | SWT.RIGHT);

		startItem = createStartToolItem(partController);
		stopItem = createStopToolItem(partController);
		pauseItem = createPauseToolItem(partController);
		resumeItem = createResumeToolItem(partController);
	}

	private ToolItem createResumeToolItem(final DashboardPartController partController) {
		ToolItem resumeItem = createToolItem(toolBar, "Resume", DashboardPlugIn.getImageManager().get("resume"));
		resumeItem.setEnabled(false);
		resumeItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				partController.unpause();
				setStatusToResumed();
			}

		});
		return resumeItem;
	}

	private ToolItem createPauseToolItem(final DashboardPartController partController) {
		ToolItem pauseItem = createToolItem(toolBar, "Pause", DashboardPlugIn.getImageManager().get("pause"));
		pauseItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				partController.pause();
				setStatusToPaused();
			}

		});
		return pauseItem;
	}

	private ToolItem createStopToolItem(final DashboardPartController partController) {
		ToolItem stopItem = createToolItem(toolBar, "Stop", DashboardPlugIn.getImageManager().get("stop"));
		stopItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				partController.stop();
				setStatusToStopped();
			}
		});
		return stopItem;
	}

	private ToolItem createStartToolItem(final DashboardPartController partController) {
		ToolItem startItem = createToolItem(toolBar, "Start", DashboardPlugIn.getImageManager().get("start"));
		startItem.setEnabled(false);
		startItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					partController.start();
				} catch (final Exception ex) {
					throw new RuntimeException("Could not start DashboardPart", ex);
				}

				setStatusToStarted();
			}
		});
		return startItem;
	}

	public ToolBar getToolBar() {
		return toolBar;
	}

	public void setStatusToPaused() {
		startItem.setEnabled(false);
		stopItem.setEnabled(false);
		pauseItem.setEnabled(false);
		resumeItem.setEnabled(true);
		
		editor.setPartNameSuffix("Paused");
	}

	public void setStatusToResumed() {
		startItem.setEnabled(false);
		stopItem.setEnabled(true);
		pauseItem.setEnabled(true);
		resumeItem.setEnabled(false);
		
		editor.setPartNameSuffix(null);
	}

	public void setStatusToStarted() {
		startItem.setEnabled(false);
		stopItem.setEnabled(true);
		pauseItem.setEnabled(true);
		resumeItem.setEnabled(false);
		
		editor.setPartNameSuffix(null);
	}

	public void setStatusToStopped() {
		startItem.setEnabled(true);
		stopItem.setEnabled(false);
		pauseItem.setEnabled(false);
		resumeItem.setEnabled(false);
		
		editor.setPartNameSuffix("Stopped");
	}

	private static ToolItem createToolItem(ToolBar tb, String toolTip, Image image) {
		final ToolItem toolItem = new ToolItem(tb, SWT.PUSH);
		toolItem.setImage(image);
		toolItem.setToolTipText(toolTip);
		toolItem.setWidth(100);
		return toolItem;
	}
}
