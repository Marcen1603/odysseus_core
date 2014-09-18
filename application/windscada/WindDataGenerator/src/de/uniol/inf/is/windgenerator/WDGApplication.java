package de.uniol.inf.is.windgenerator;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class WDGApplication implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		context.applicationRunning();
	    return IApplicationContext.EXIT_ASYNC_RESULT;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
