package de.uniol.inf.is.windgenerator;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;
import de.uniol.inf.is.winddatagenerator.WindTurbineDataProvider;


public class WDGApplication implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		context.applicationRunning();
		StreamServer server = new StreamServer(54321, new WindTurbineDataProvider());
	    server.start();
	    return IApplicationContext.EXIT_ASYNC_RESULT;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
