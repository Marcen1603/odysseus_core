package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.physicaloperator.base.access.Router;

public class StartRouterCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			if (!Router.getInstanceWithOutStarting().isAlive()){
				Router.getInstanceWithOutStarting().start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
