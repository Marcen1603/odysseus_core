package de.uniol.inf.is.odysseus.core.server.console;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;


public class OdysseusServerApplication implements IApplication{

	boolean run = true;
	
	@Override
	public Object start(IApplicationContext context) throws Exception {
		synchronized(this){
			while (run){
				this.wait(10000);
			}
		}
		return null;
	}

	@Override
	public void stop() {
		synchronized(this){
			run = false;
			this.notifyAll();
		}
	}

}
