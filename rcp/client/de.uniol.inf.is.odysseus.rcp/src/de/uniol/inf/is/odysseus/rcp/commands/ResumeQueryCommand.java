package de.uniol.inf.is.odysseus.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;

public class ResumeQueryCommand extends AbstractQueryCommand {

	static final IQueryCommandAction resume = new ResumeQueryCommandAction();
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<Integer> selectedObj = SelectionProvider.getSelection(event);
		return execute(selectedObj,resume);
	}

}
