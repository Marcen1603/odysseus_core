package de.uniol.inf.is.odysseus.rcp.commands;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

abstract public class AbstractSuspendQueryCommand extends AbstractQueryCommand {

	@Override
	void execute(IExecutor executor, Integer qID) {
		executor.suspendQuery(qID, OdysseusRCPPlugIn.getActiveSession());
	}

}
