package de.uniol.inf.is.odysseus.peer.loadbalancing.active.rcp.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

public class LockQueryCommand extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.err.println("LOCK QUERY");
		
		return null;
	}

}
