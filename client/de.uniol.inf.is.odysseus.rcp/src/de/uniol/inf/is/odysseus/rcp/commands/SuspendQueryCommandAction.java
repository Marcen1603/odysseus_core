package de.uniol.inf.is.odysseus.rcp.commands;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class SuspendQueryCommandAction implements IQueryCommandAction {

	@Override
	public void execute(IExecutor executor, Integer qID) {
		executor.suspendQuery(qID, OdysseusRCPPlugIn.getActiveSession());
	}

	@Override
	public String getActionText() {
		return "Suspending";
	}
	
}
