package de.uniol.inf.is.odysseus.core.server.application;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class ServerApplication implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		System.out.println("Odysseus started");
		synchronized(this){
			this.wait();
		}
		System.out.println("Odysseus terminated");
		return null;
	}

	@Override
	public void stop() {
		synchronized (this) {
			notifyAll();
		}
	}

}
