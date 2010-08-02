package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.physicaloperator.base.access.Router;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionWindow;


public class StopRouterCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			if (Router.getInstanceWithOutStarting().isAlive()){
				Router.getInstanceWithOutStarting().stopRouting();
			}
		} catch (IOException e) {
			new ExceptionWindow(e);
			e.printStackTrace();
		}
		return null;	
	}

}
