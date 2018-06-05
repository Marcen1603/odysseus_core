package de.uniol.inf.is.odysseus.rcp;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;

public abstract class AbstractKeyValueUpdaterProvider<T extends IPhysicalOperator> extends AbstractKeyValueProvider<T> {

	private static final int UPDATE_INTERVAL_MILLIS = 500;
	
	private Thread updateThread;
	private boolean updateRunning = true;

	@Override
	public void createPartControl(Composite parent, final T operator) {
		super.createPartControl(parent, operator);
		
		updateThread = new Thread( new Runnable() {
			@Override
			public void run() {
				while( updateRunning ) {
					Display disp = PlatformUI.getWorkbench().getDisplay();
					if( !disp.isDisposed() ) {
						disp.asyncExec(new Runnable() {
							@Override
							public void run() {
								refresh();
							}
						});
					}
					trySleep();		
				}
			}
		});
		updateThread.setDaemon(true);
		updateThread.setName("Operator detail info updater");
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
