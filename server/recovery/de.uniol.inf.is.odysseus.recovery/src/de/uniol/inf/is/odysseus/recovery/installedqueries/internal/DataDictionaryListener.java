package de.uniol.inf.is.odysseus.recovery.installedqueries.internal;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionarySinkListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AbstractCreateStreamOrViewCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AbstractDropStreamOrViewCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateStreamCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateViewCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropSinkCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropStreamCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropViewCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.recovery.installedqueries.IInstalledQueriesHandler;

/**
 * Class to listen to changes within an {@code IDataDictionary}, to create
 * {@link IExecutorCommands} for new sources, removed sources, new sinks and
 * removed sinks, and to call {@code QueryStateRecoveryComponent} to store them.
 * 
 * @author Michael Brand
 *
 */
public class DataDictionaryListener implements IDataDictionaryListener, IDataDictionarySinkListener {

	/**
	 * The {@link IInstalledQueriesHandler} that handles the backup.
	 */
	private IInstalledQueriesHandler handler;

	/**
	 * Creates a new listener.
	 * 
	 * @param handler
	 *            The {@link IInstalledQueriesHandler} that handles the backup.
	 */
	public DataDictionaryListener(IInstalledQueriesHandler handler) {
		this.handler = handler;
	}

	@Override
	public void addedSinkDefinition(IDataDictionary sender, String name, ILogicalOperator op, ISession caller) {
		// CreateSinkCommands are backed up by ExecutorCommandListener.
		// This is because using this method here causes serialization errors,
		// when a sink is created in PQL. PQL sinks are part of queries and now
		// backed up within the AddQueryCommands.
	}

	@Override
	public void removedSinkDefinition(IDataDictionary sender, String name, ILogicalOperator op, ISession caller) {
		String sinkName = new Resource(name).getResourceName();
		DropSinkCommand command = new DropSinkCommand(sinkName, true, caller);
		this.handler.backup(command, System.currentTimeMillis());
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op, boolean isView,
			ISession session) {
		// name is the full qualified resource name,which is not allowed for
		// create commands!
		String sourceName = new Resource(name).getResourceName();
		AbstractCreateStreamOrViewCommand command;
		if (isView) {
			command = new CreateViewCommand(sourceName, op, session);
		} else {
			command = new CreateStreamCommand(sourceName, op, session);
		}
		this.handler.backup(command, System.currentTimeMillis());
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op, boolean isView,
			ISession session) {
		String sourceName = new Resource(name).getResourceName();
		AbstractDropStreamOrViewCommand command;
		if (isView) {
			command = new DropViewCommand(sourceName, true, session);
		} else {
			command = new DropStreamCommand(sourceName, true, session);
		}
		this.handler.backup(command, System.currentTimeMillis());
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// Nothing to do.
	}

}