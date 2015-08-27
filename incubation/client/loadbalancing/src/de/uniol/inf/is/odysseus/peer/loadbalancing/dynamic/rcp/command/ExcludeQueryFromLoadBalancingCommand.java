package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.rcp.command;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.IExcludedQueriesRegistry;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.rcp.Activator;
import de.uniol.inf.is.odysseus.rcp.commands.AbstractQueryCommand;

public class ExcludeQueryFromLoadBalancingCommand extends AbstractQueryCommand implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<Integer> selectedQueries  = getSelecteddQueries(event);
		IExcludedQueriesRegistry reg = Activator.getExcludedQueryRegistry();
		if(!(reg==null)) {
				
			for(Integer queryID : selectedQueries) {
				reg.excludeQueryIdFromLoadBalancing(queryID);
			}
		}
		return null;
	}

}
