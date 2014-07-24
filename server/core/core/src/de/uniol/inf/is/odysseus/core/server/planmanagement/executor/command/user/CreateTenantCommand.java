package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CreateTenantCommand extends AbstractExecutorCommand {

	private String tenantname;

	public CreateTenantCommand(String tenantname, ISession caller) {
		super(caller);
		this.tenantname = tenantname;
	}

	@Override
	public Collection<Integer> execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {
		UserManagementProvider.createNewTenant(tenantname, getCaller());
		return getEmptyCollection();
	}

}
