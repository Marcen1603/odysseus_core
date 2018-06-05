package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;


import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.util.OSGI;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CreateTenantCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = 3942783181328876548L;
	
	private String tenantname;

	public CreateTenantCommand(String tenantname, ISession caller) {
		super(caller);
		this.tenantname = tenantname;
	}

	@Override
	public void execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {
		OSGI.get(UserManagementProvider.class).createNewTenant(tenantname, getCaller());
	}

}
