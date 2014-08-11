package de.uniol.inf.is.odysseus.rcp.commands;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public abstract class AbstractStartQueryCommand extends AbstractQueryCommand {
	
	@Override
	void execute(IExecutor executor, Integer qID) {
		executor.startQuery(qID, OdysseusRCPPlugIn.getActiveSession());
	}
	
}
