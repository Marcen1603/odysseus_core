package de.uniol.inf.is.odysseus.recovery.querystate;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionarySinkListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateStreamCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateViewCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropStreamCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropViewCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * Class to listen to changes within an {@code IDataDictionary}, to create
 * {@link IExecutorCommands} for new sources, removed sources, new sinks and
 * removed sinks, and to call {@code QueryStateRecoveryComponent} to store them.
 * 
 * @author Michael Brand
 *
 */
public class DataDictionaryListener implements IDataDictionaryListener, IDataDictionarySinkListener {

	@Override
	public void addedSinkDefinition(IDataDictionary sender, String name, ILogicalOperator op, ISession caller) {
		// FIXME Can not serialize such a CreateSinkCommand
//		IExecutorCommand command = new CreateSinkCommand(name, op, caller);
//		QueryStateRecoveryComponent.backupExecutorCommand(command, System.currentTimeMillis());
	}

	@Override
	public void removedSinkDefinition(IDataDictionary sender, String name, ILogicalOperator op, ISession caller) {
		// FIXME Can not serialize such a CreateSinkCommand
//		IExecutorCommand command = new DropSinkCommand(name, true, caller);
//		QueryStateRecoveryComponent.backupExecutorCommand(command, System.currentTimeMillis());
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op, boolean isView,
			ISession session) {
		// name is the full qualified resource name,which is not allowed for
		// create commands!
		String sourceName = new Resource(name).getResourceName();
		IExecutorCommand command;
		if (isView) {
			command = new CreateViewCommand(sourceName, op, session);
		} else {
			command = new CreateStreamCommand(sourceName, op, session);
		}
		QueryStateRecoveryComponent.backupExecutorCommand(command, System.currentTimeMillis());
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op, boolean isView,
			ISession session) {
		IExecutorCommand command;
		if (isView) {
			command = new DropViewCommand(name, true, session);
		} else {
			command = new DropStreamCommand(name, true, session);
		}
		QueryStateRecoveryComponent.backupExecutorCommand(command, System.currentTimeMillis());
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// Nothing to do.
	}

}