/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.application.storing.view.views;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.IEvaluationService;

import de.uniol.inf.is.odysseus.application.storing.controller.IRecordingListener;
import de.uniol.inf.is.odysseus.application.storing.controller.RecordingController;


public class RecordingView extends ViewPart implements IRecordingListener, ISelectionChangedListener {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "de.uniol.inf.is.odysseus.application.storing.view.views.RecordingView";
	
	public static final String PROPERTY_NAMESPACE = "de.uniol.inf.is.odysseus.application.storing.view.views";

	
	public static final String PROPERTY_IS_RECORDING_STARTABLE = "isRecordingStartable";
	public static final String PROPERTY_IS_RECORDING_STOPPABLE= "isRecordingStoppable";
	public static final String PROPERTY_IS_RECORDING_PAUSABLE = "isRecordingPauseable";
	public static final String PROPERTY_IS_DELETABLE = "isDeleteable";
	public static final String PROPERTY_IS_PLAYING_STARTABLE = "isPlayingStartable";
	public static final String PROPERTY_IS_PLAYING_PAUSABLE = "isPlayingPauseable";
	public static final String PROPERTY_IS_PLAYING_STOPPABLE = "isPlayingStoppable";

	private TableViewer viewer;

	/**
	 * The constructor.
	 */
	public RecordingView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
    public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());

		// Contextmenu
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(getTreeViewer().getControl());
		// Set the MenuManager
		getTreeViewer().getControl().setMenu(contextMenu);
		getSite().registerContextMenu(menuManager, getTreeViewer());
		getSite().setSelectionProvider(getTreeViewer());
		RecordingController.getInstance().addListener(this);
		getSite().getSelectionProvider().addSelectionChangedListener(this);
	}

	public TableViewer getTreeViewer() {
		return this.viewer;
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
    public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	@Override
	public void dispose() {
		RecordingController.getInstance().removeListener(this);
		RecordingController.getInstance().dispose();
		super.dispose();
	}
	
	
	private void refresh() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				try {
					
					viewer.refresh(); 
					IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					IEvaluationService evaluationService = (IEvaluationService) window.getService(IEvaluationService.class);
					if (evaluationService != null) {
						evaluationService.requestEvaluation(PROPERTY_NAMESPACE + "." + PROPERTY_IS_DELETABLE);
						evaluationService.requestEvaluation(PROPERTY_NAMESPACE + "." + PROPERTY_IS_RECORDING_PAUSABLE);	
						evaluationService.requestEvaluation(PROPERTY_NAMESPACE + "." + PROPERTY_IS_RECORDING_STARTABLE);	
						evaluationService.requestEvaluation(PROPERTY_NAMESPACE + "." + PROPERTY_IS_RECORDING_STOPPABLE);
						evaluationService.requestEvaluation(PROPERTY_NAMESPACE + "." + PROPERTY_IS_PLAYING_PAUSABLE);
						evaluationService.requestEvaluation(PROPERTY_NAMESPACE + "." + PROPERTY_IS_PLAYING_STARTABLE);
						evaluationService.requestEvaluation(PROPERTY_NAMESPACE + "." + PROPERTY_IS_PLAYING_STOPPABLE);
					}
				} catch (Exception e) {
					viewer.setInput("Refresh failed");					
				}
			}

		});
		
	}
	@Override
	public void recordingChanged() {
		refresh();		
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		refresh();		
	}
}
