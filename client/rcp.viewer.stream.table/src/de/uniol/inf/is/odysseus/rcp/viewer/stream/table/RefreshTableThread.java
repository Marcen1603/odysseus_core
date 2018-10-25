package de.uniol.inf.is.odysseus.rcp.viewer.stream.table;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import java.util.Objects;

class RefreshTableThread extends Thread {

	private static final int REFRESH_INTERVAL_MILLIS = 1000;
	
	private final TableViewer viewer;
	
	private boolean isRunning;
	
	public RefreshTableThread( TableViewer viewer ) {
		Objects.requireNonNull(viewer, "TableViewer to refresh must not be null!");
		// Preconditions.checkArgument(!viewer.getTable().isDisposed(), "Tableviewer to refresh must not be disposed already");
		
		this.viewer = viewer;
		setName("TableViewer refresh");
	}
	
	@Override
	public void run() {
		isRunning = true;
		while (isRunning) {
			Display disp = PlatformUI.getWorkbench().getDisplay();
			if (disp.isDisposed()) {
				return;
			}

			disp.asyncExec(new Runnable() {
				@Override
				public void run() {
					if (!viewer.getTable().isDisposed()) {
						viewer.refresh();
					} else {
						isRunning = false;
					}
				}

			});
			waiting();
		}
	}
	
	public void stopRunning() {
		isRunning = false;
	}
	
	private static void waiting() {
		try {
			Thread.sleep(REFRESH_INTERVAL_MILLIS);
		} catch (InterruptedException e) {
		}
	}
}
