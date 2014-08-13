package de.uniol.inf.is.odysseus.rcp.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class ResumeAllQueriesCommand extends AbstractQueryCommand {

	static final IQueryCommandAction resume = new ResumeQueryCommandAction();
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return execute(getAllQueries(),resume);
	}

}
