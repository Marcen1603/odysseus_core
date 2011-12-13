package de.uniol.inf.is.odysseus.nexmark;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import de.uniol.inf.is.odysseus.nexmark.simulation.NexmarkServer;

public class NexmarkApplication implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		String[] args = (String[]) context.getArguments().get(
				IApplicationContext.APPLICATION_ARGS);
		NexmarkServer.main(args);
		return null;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

}
