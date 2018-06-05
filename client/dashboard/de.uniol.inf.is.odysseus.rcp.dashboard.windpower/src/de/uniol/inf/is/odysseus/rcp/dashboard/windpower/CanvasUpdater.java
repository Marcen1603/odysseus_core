package de.uniol.inf.is.odysseus.rcp.dashboard.windpower;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

/**
 * Runnable that can redraw in defined intervals
 * 
 * 
 */
public class CanvasUpdater extends Thread {

	private final Display display;
	private final Canvas canvas;
	private final int milliseconds;

	private boolean isRunning = true;

	/**
	 * Constructor
	 * 
	 * @param canvas
	 *            the canvas to update
	 * @param intervalMillis
	 *            the interval to update the canvas
	 */
	public CanvasUpdater(Canvas canvas, int intervalMillis) {
		this.display = canvas.getDisplay();
		this.canvas = canvas;
		this.milliseconds = intervalMillis;

		setName("Wind Turbine Canvas updater");
		setDaemon(true);
	}

	/**
	 * Stops the runnable looping
	 */
	public void stopRunning() {
		isRunning = false;
	}

	@Override
	public void run() {
		while (isRunning && !display.isDisposed()) {
			if (!canvas.isDisposed()) {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						if (!canvas.isDisposed()) {
							canvas.redraw();
						}
					}
				});

				trySleep(milliseconds);
			}
		}
	}

	/**
	 * Lets the Thread sleep for specified time
	 * 
	 * @param milliseconds
	 *            time to sleep
	 */
	private static void trySleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
		}
	}
}
