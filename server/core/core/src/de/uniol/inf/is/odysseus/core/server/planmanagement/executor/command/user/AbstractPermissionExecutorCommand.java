package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.user;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.PermissionFactory;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

abstract public class AbstractPermissionExecutorCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = 198941493600444933L;

	public AbstractPermissionExecutorCommand(ISession caller) {
		super(caller);
	}

	protected List<IPermission> determinePermissions(List<String> rights) {
		List<IPermission> operations = new ArrayList<>();
		for (String r : rights) {
			IPermission action = PermissionFactory.valueOf(r);
			if (action != null) {
				operations.add(action);
			} else {
				throw new QueryParseException("Right " + r + " not defined.");
			}
		}
		return operations;
	}
	
}
