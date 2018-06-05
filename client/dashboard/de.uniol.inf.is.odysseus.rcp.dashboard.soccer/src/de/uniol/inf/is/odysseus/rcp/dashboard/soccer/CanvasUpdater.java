package de.uniol.inf.is.odysseus.rcp.dashboard.soccer;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

public class CanvasUpdater extends Thread {
		
	private final Display display;
	private final Canvas canvas;
	private final int milliseconds;
	
	private boolean isRunning = true;

	public CanvasUpdater(Canvas canvas, int intervalMillis) {
		this.display = canvas.getDisplay();
		this.canvas = canvas;
		this.milliseconds = intervalMillis;
		
		setName("Soccer Canvas updater");
		setDaemon(true);
	}

	public void stopRunning() {
		isRunning = false;
	}

	@Override
	public void run() {
		while( isRunning && !display.isDisposed()) {
			if (!canvas.isDisposed() ) {
				display.asyncExec( new Runnable() {
					@Override
					public void run() {
						if( !canvas.isDisposed() ) {
							canvas.redraw();
						}
					}
				});
				
				trySleep(milliseconds);
			}
		}
	}

	private static void trySleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
		}
	}
}
