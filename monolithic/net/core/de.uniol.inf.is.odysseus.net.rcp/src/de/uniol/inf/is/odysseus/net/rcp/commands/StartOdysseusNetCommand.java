package de.uniol.inf.is.odysseus.net.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uniol.inf.is.odysseus.net.IOdysseusNetStartupManager;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.rcp.OdysseusNetRCPPlugIn;

public class StartOdysseusNetCommand extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			IOdysseusNetStartupManager manager = OdysseusNetRCPPlugIn.getOdysseusNetStartupManager();
			if( manager.isStarted() ) {
				manager.stop();
			} else {
				manager.start();
			}
			
			return null;
			
		} catch (OdysseusNetException e) {
			throw new ExecutionException("Could not start OdysseusNet", e);
		}
	}

}
