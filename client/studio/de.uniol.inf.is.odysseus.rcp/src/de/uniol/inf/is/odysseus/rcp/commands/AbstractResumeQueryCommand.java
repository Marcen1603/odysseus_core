package de.uniol.inf.is.odysseus.rcp.commands;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;

abstract public class AbstractResumeQueryCommand extends AbstractQueryCommand {

	@Override
	void execute(IExecutor executor, Integer qID) {
		executor.resumeQuery(qID, OdysseusRCPPlugIn.getActiveSession());
	}

}
