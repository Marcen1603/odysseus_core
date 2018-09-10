package de.uniol.inf.is.odysseus.memstore.mdastore.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.usermanagement.IUserManagementWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStoreManager;

public class DropMDAStoreCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -6904375022579536023L;
	private static final Logger LOG = LoggerFactory
			.getLogger(DropMDAStoreCommand.class);
	private static final InfoService INFO = InfoServiceFactory
			.getInfoService(DropMDAStoreCommand.class);

	private String name;

	public DropMDAStoreCommand(ISession caller, String name) {
		super(caller);
		this.name = name;
	}

	@Override
	public void execute(IDataDictionaryWritable dd, IUserManagementWritable um,
			IServerExecutor executor) {
		if (!MDAStoreManager.exists(name)) {
			INFO.warning("Drop store: Store with name " + name + " not defined");
		}
		MDAStoreManager.remove(name);
		LOG.info("MDAStore " + name + " removed");
	}

}
