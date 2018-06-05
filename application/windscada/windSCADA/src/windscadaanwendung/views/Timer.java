package windscadaanwendung.views;

import java.util.Observable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * A simple timer that notifies observers after a certain amount of
 * milliseconds. Therefore it extends the class Observable
 * 
 * @author Dennis Nowak
 * 
 */
public class Timer extends Observable implements Runnable {

	private final int milliseconds;

	private boolean isRunning = true;

	/**
	 * Creates instance of class Timer
	 * 
	 * @param intervalMillis
	 *            the time between two updates of the obervers
	 */
	public Timer(int intervalMillis) {
		this.milliseconds = intervalMillis;
	}

	/**
	 * Lets the runnable stop looping
	 */
	public void stopRunning() {
		isRunning = false;
	}

	@Override
	public void run() {
		while (isRunning) {
			trySleep(milliseconds);
			this.setChanged();
			Display display = PlatformUI.getWorkbench().getDisplay();
			display.asyncExec(new Runnable() {
				@Override
				public void run() {
					notifyObservers(milliseconds);
				}
			});

		}
	}

	/**
	 * Lets the Thrad sleep a defined amount of milliseconds
	 * 
	 * @param milliseconds
	 *            the time that a thread should slepp
	 */
	private static void trySleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
		}
	}
}
