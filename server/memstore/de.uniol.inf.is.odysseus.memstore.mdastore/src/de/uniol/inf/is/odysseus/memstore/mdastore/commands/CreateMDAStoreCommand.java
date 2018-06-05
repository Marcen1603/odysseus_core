package de.uniol.inf.is.odysseus.memstore.mdastore.commands;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStore;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStoreManager;

public class CreateMDAStoreCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = 3301766838992507006L;

	private static final Logger LOG = LoggerFactory.getLogger(CreateMDAStoreCommand.class);

	private String name;
	private List<List<Double>> values;

	public CreateMDAStoreCommand(ISession caller, String name, List<List<Double>> values) {
		super(caller);
		this.name = name;
		this.values = values;
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um, IServerExecutor executor) {
		if (MDAStoreManager.exists(name)) {
			throw new IllegalArgumentException("Store with name " + name + " already defined. Remove first.");
		}
		MDAStore store = MDAStoreManager.create(name);
		LOG.info("MDAStore " + name + " created");
		if (values != null) {
			store.initialize(values);
			LOG.info("MDAStore " + name + " initialized");
		}
	}

}
