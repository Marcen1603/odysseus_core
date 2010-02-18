package de.uniol.inf.is.odysseus.action.demoapp;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	public static void main(String[] args) {
		Application app = new Application();
		app.start(null);
	}
	private Display display;

	private Shell shell;

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) {
		this.display = new Display();
		this.shell = AuctionMonitor.getInstance().runApplication(display);
		while(!shell.isDisposed()){
			if(! display.readAndDispatch()) {
		        display.sleep();
		    }
		}
		display.dispose();
		return IApplication.EXIT_OK;
	}
	
	public void stop() {
		if (this.shell != null && !this.shell.isDisposed()){
			this.display.syncExec(new Runnable() {
				@Override
				public void run() {
					Application.this.shell.close();
				}
			});
			this.display.dispose();
		}
	}
}
