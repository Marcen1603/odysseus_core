package de.uniol.inf.is.odysseus.rcp.commands;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class StartQueryCommandAction implements IQueryCommandAction {

	@Override
	public void execute(IExecutor executor, Integer qID) {
		executor.startQuery(qID, OdysseusRCPPlugIn.getActiveSession());
	}

	@Override
	public String getActionText() {
		return "Starting";
	}
}
