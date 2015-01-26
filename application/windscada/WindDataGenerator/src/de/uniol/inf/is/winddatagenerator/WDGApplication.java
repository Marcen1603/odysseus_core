package de.uniol.inf.is.winddatagenerator;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * standard application implementation for the WindDataGenerator
 * 
 * @author Dennis Nowak
 * 
 */
public class WDGApplication implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		context.applicationRunning();
		return IApplicationContext.EXIT_ASYNC_RESULT;
	}

	@Override
	public void stop() {

	}

}
