package de.uniol.inf.is.odysseus.rcp.commands;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public class RemoveQueryCommandAction implements IQueryCommandAction {
	
	public static final String actionText = "Removing";
	
	@Override
	public void execute(IExecutor executor, Integer qID) {
		executor.removeQuery(qID, OdysseusRCPPlugIn.getActiveSession());		
	}
	
	@Override
	public String getActionText() {
		return actionText;
	}

}
