package de.uniol.inf.is.odysseus.test.runner;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.test.ITestComponent;

public class Tester implements IApplication {

	Logger logger = LoggerFactory.getLogger(Tester.class);
	private boolean goOne = true;

	@Override
	public Object start(IApplicationContext context) throws Exception {
		final String[] args = (String[]) context.getArguments().get("application.args");

		context.applicationRunning();
		final BundleContext ctx = Activator.getBundleContext();

		new Thread() {
			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				@SuppressWarnings("rawtypes")
				ServiceTracker t = new ServiceTracker(ctx, ITestComponent.class.getName(), null);
				t.open();
				ITestComponent testComponent = null;
				try {
					while (testComponent == null) {
						// System.out.println("Wait for TestComponent Service");
						testComponent = (ITestComponent) t.waitForService(1000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				testComponent.startTesting(args);
				goOne = false;
			}
		}.start();

		while (goOne) {
			synchronized (this) {
				wait(10000);
			}
		}

		return null;
	}

	@Override
	public void stop() {

	}
}
