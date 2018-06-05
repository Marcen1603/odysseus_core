package de.uniol.inf.is.odysseus.rcp.commands;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;

public interface IQueryCommandAction {

	void execute(IExecutor executor, Integer qID);
	String getActionText();

}
