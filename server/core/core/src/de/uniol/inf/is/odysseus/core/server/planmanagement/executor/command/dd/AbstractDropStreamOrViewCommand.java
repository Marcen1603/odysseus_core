package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;


import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class AbstractDropStreamOrViewCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = 640941802555170500L;
	
	private String name;
	private boolean ifExits;

	public AbstractDropStreamOrViewCommand(String name, boolean ifExits,
			ISession caller) {
		super(caller);
		this.name = name;
		this.ifExits = ifExits;
	}

	@Override
	public void execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {
		if (ifExits) {
			if (dd.containsViewOrStream(name, getCaller())) {
				dropViewOrStream(dd);
			}
		} else {
			dropViewOrStream(dd);
		}
	}

	private void dropViewOrStream(IDataDictionaryWritable dd) {
		dd.removeViewOrStream(name, getCaller());
	}
	
	public String getName() {
		return name;
	}

}
