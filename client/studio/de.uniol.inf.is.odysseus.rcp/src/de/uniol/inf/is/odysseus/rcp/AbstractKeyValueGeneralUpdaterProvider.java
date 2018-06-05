package de.uniol.inf.is.odysseus.rcp;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public abstract class AbstractKeyValueGeneralUpdaterProvider extends AbstractKeyValueGeneralProvider {

	private static final int UPDATE_INTERVAL_MILLIS = 1000;

	private Thread updateThread;
	private boolean updateRunning = true;

	@Override
	public void createPartControl(Composite parent, final IPhysicalOperator operator) {
		super.createPartControl(parent, operator);

		updateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (updateRunning) {
					Display disp = PlatformUI.getWorkbench().getDisplay();
					if (!disp.isDisposed()) {
						disp.asyncExec(new Runnable() {
							@Override
							public void run() {
								if (updateRunning) {
									refresh();
								}
							}
						});
					}
					trySleep();
				}
			}
		});
		updateThread.setDaemon(true);
		updateThread.setName("Operator detail info updater of operator '" + operator.getClass().getSimpleName() + "'");
		updateThread.start();
	}

	@Override
	public void dispose() {
		updateRunning = false;

		super.dispose();
	}

	private static void trySleep() {
		try {
			Thread.sleep(UPDATE_INTERVAL_MILLIS);
		} catch (InterruptedException e) {
		}
	}

}
