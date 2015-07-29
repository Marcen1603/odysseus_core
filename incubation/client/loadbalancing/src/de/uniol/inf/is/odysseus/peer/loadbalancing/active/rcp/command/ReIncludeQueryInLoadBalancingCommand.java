package de.uniol.inf.is.odysseus.peer.loadbalancing.active.rcp.command;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingController;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.rcp.Activator;
import de.uniol.inf.is.odysseus.rcp.commands.AbstractQueryCommand;

public class ReIncludeQueryInLoadBalancingCommand extends AbstractQueryCommand implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<Integer> selectedQueries  = getSelecteddQueries(event);
		ILoadBalancingController controller = Activator.getLoadBalancingController();
		if(!(controller==null)) {
				
			for(Integer queryID : selectedQueries) {
				controller.removeExcludedQueryID(queryID);
			}
		}
		return null;
	}

}
