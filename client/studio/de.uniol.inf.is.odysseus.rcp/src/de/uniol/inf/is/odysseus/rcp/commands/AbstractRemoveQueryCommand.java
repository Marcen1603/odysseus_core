package de.uniol.inf.is.odysseus.rcp.commands;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

public abstract class AbstractRemoveQueryCommand extends AbstractQueryCommand {
	
	@Override
	void execute(IExecutor executor, Integer qID) {
		executor.removeQuery(qID, OdysseusRCPPlugIn.getActiveSession());		
	}

}
