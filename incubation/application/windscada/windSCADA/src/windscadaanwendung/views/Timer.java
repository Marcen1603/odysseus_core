package windscadaanwendung.views;

import java.util.Observable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;


public class Timer extends Observable implements Runnable {

	private final int milliseconds;

	private boolean isRunning = true;

	public Timer(int intervalMillis) {
		this.milliseconds = intervalMillis;
	}

	public void stopRunning() {
		isRunning = false;
	}

	@Override
	public void run() {
		while (isRunning) {
			trySleep(milliseconds);
			this.setChanged();
			Display display = PlatformUI.getWorkbench().getDisplay();
			display.asyncExec( new Runnable() {
				@Override
				public void run() {
					notifyObservers(milliseconds);
				}
			});
			
		}
	}

	private static void trySleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
		}
	}
}
