package de.uniol.inf.is.odysseus.rcp.commands;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryFunction;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.util.SelectionProvider;

public class PartialQueryCommand extends AbstractQueryCommand {

	static final IQueryCommandAction partial = new PartialQueryCommandAction();
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<Integer> selectedObjs = SelectionProvider.getSelection(event);
		
		for( Integer selectedObj : selectedObjs ) {
			if( !QueryState.isAllowed(OdysseusRCPPlugIn.getExecutor().getQueryState(selectedObj), QueryFunction.PARTIAL) ) {
				return null;
			}
		}
		
		return execute(selectedObjs,partial);
	}

}
