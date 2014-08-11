package de.uniol.inf.is.odysseus.rcp.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class RestartAllQueriesCommand extends AbstractRestartQueryCommand {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return execute(getAllQueries(), "Restarting ");
	}

}
