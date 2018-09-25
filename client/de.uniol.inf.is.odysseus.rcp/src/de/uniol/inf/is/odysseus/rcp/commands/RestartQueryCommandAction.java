package de.uniol.inf.is.odysseus.rcp.commands;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class RestartQueryCommandAction implements IQueryCommandAction{

	@Override
	public void execute(IExecutor executor, Integer qID) {
		executor.stopQuery(qID, OdysseusRCPPlugIn.getActiveSession());
		executor.startQuery(qID, OdysseusRCPPlugIn.getActiveSession());
	}

	@Override
	public String getActionText() {
		return "Restarting";
	}
	
}
