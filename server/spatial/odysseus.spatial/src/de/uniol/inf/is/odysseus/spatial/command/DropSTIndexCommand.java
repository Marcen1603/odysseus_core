package de.uniol.inf.is.odysseus.spatial.command;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.spatial.datastructures.spatiotemporal.SpatioTemporalDataStructureProvider;

public class DropSTIndexCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -571862020102465316L;

	private String name;

	public DropSTIndexCommand(String name, ISession caller) {
		super(caller);
		this.name = name;
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		boolean removed = SpatioTemporalDataStructureProvider.getInstance().removeDataStructure(this.name);

		if (!removed) {
			throw new QueryParseException("Index with name " + this.name + " does not exist.");
		}

	}

}
