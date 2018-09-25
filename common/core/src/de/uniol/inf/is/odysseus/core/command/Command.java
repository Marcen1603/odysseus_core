package de.uniol.inf.is.odysseus.core.command;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public abstract class Command
{
	private ISession session;
	private IExecutor executor;
	
	public IExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(IExecutor executor) {
		this.executor = executor;
	}	
	
	public void setSession(ISession session) {
		this.session = session;
	}
	
	public ISession getSession() {
		return session;
	}
	
	public abstract boolean run();	
}
