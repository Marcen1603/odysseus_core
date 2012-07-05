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
	
	public DashboardPartEditorToolBar( Composite presentationTab, final DashboardPartEditor editor ) {
		
		Preconditions.checkNotNull(presentationTab, "PartController must not be null!");
		this.editor = Preconditions.checkNotNull(editor, "PartController must not be null!");
		
		final DashboardPartController partController = editor.getDashboardPartController();
		
		toolBar = new ToolBar(presentationTab, SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		
		startStopItem = createToolItem(toolBar, "Stop");
		startStopItem.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( startStopItem.getText().equals("Stop")) {
					partController.stop();
					setStatusToStopped();
				} else {
					try {
						partController.start();			
					} catch (Exception ex) {
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
				if(pauseUnpauseItem.getText().equals("Pause")) {
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
	
	public void setStatusToStarted() {
		startStopItem.setText("Stop");		
		editor.setPartNameSuffix(null);
	}
	
	public void setStatusToStopped() {
		startStopItem.setText("Start");
		editor.setPartNameSuffix("Stopped");		
	}
	
	public void setStatusToPaused() {
		pauseUnpauseItem.setText("Res.");
		editor.setPartNameSuffix("Paused");
	}
	
	public void setStatusToResumed() {
		pauseUnpauseItem.setText("Pause");
		editor.setPartNameSuffix(null);
	}
	
	private static ToolItem createToolItem(ToolBar tb, String title) {
		ToolItem toolItem = new ToolItem(tb, SWT.PUSH);
		toolItem.setText(title);
		toolItem.setWidth(100);
		return toolItem;
	}
}
