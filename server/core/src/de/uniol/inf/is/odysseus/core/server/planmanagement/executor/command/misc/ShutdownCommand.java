package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.misc;


import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UpdatePermission;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

public class ShutdownCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = 3608389546098450348L;

	public ShutdownCommand(ISession caller) {
		super(caller);
	}

	@Override
	public void execute(IDataDictionaryWritable dd,
			IUserManagementWritable um, IServerExecutor executor) {
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(getCaller(), UpdatePermission.UPDATE,
				UpdatePermission.objectURI)) {
			System.exit(0);
		}else {
			throw new PermissionException("User is not allowed to shutdown the system!");
		}
	}


}
