package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public abstract class AbstractQueryCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -4791642748562443506L;

	private final Resource queryName;
	
	private Optional<Integer> queryId = Optional.absent();

	public AbstractQueryCommand(ISession caller, Resource qName) {
		super(caller);
		this.queryName = qName;
	}

	public Resource getQueryName() {
		return this.queryName;
	}
	
	public Optional<Integer> getQueryId() {
		return this.queryId;
	}
	
	public void setQueryId(int id) {
		this.queryId = Optional.of(id);
	}

}