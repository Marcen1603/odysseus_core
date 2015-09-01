package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public abstract class AbstractQueryCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -4791642748562443506L;

	private final String queryName;

	public AbstractQueryCommand(ISession caller, String qName) {
		super(caller);
		this.queryName = qName;
	}

	public String getQueryName() {
		return this.queryName;
	}

}