package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;

import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class DropAllStreamsCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -3096747328228230340L;

	public DropAllStreamsCommand(ISession caller) {
		super(caller);
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		List<ViewInformation> views = executor.getStreamsAndViewsInformation(getCaller());
		for (ViewInformation vi:views){
			executor.removeViewOrStream(vi.getName(), getCaller());
		}
	}



}
