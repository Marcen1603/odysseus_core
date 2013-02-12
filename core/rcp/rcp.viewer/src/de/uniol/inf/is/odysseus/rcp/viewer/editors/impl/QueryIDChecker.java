package de.uniol.inf.is.odysseus.rcp.viewer.editors.impl;

import org.eclipse.ui.PlatformUI;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

final class QueryIDChecker extends Thread {

	private static final int CHECK_INTERVAL_MILLIS = 500;
	
	private final GraphViewEditor editor;
	private final PhysicalGraphEditorInput input;
	
	private boolean isRunning;

	public QueryIDChecker(GraphViewEditor editor, PhysicalGraphEditorInput input) {
		this.editor = Preconditions.checkNotNull(editor, "Editor to check query id must not be null!");
		this.input = Preconditions.checkNotNull(input, "Input to check query id must not be null!");
		Preconditions.checkArgument(input.hasQueryID(), "QueryID to check not specified");
		
		setDaemon(true);
		setName("Checker for query " + input.getQueryID());
	}

	public void run() {
		isRunning = true;
		while (isRunning) {

			trySleep();

			if (!OdysseusRCPPlugIn.getExecutor().getLogicalQueryIds().contains(input.getQueryID())) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						editor.getSite().getWorkbenchWindow().getActivePage().closeEditor(editor, false);
					}
				});
				isRunning = false;
			}

		}
	}

	public void stopRunning() {
		isRunning = false;
	}

	private static void trySleep() {
		try {
			Thread.sleep(CHECK_INTERVAL_MILLIS);
		} catch (InterruptedException ex) {
		}
	};
}
