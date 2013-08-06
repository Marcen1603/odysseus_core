package de.uniol.inf.is.odysseus.product.server.starter;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class OdysseusServerApplication implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		// application always terminates successfully
		// the products needs a "-noExit" in the launch configuration to keep alive after this application shuts down
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		// nothing to do
	}
	

}
