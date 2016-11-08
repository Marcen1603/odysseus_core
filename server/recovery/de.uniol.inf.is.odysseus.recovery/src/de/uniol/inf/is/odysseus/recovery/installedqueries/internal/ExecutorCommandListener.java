package de.uniol.inf.is.odysseus.recovery.installedqueries.internal;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AddQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.executorcommand.IExecutorCommandListener;
import de.uniol.inf.is.odysseus.recovery.installedqueries.IInstalledQueriesHandler;

/**
 * A listener for executed {@link IExecutorCommand}s ({@link CreateQueryCommand}
 * and {@link AddQueryCommand}).
 *
 * @author Michael Brand
 *
 */
public class ExecutorCommandListener implements IExecutorCommandListener {

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
	public ExecutorCommandListener(IInstalledQueriesHandler handler) {
		this.handler = handler;
	}

	@Override
	public void executorCommandEvent(IExecutorCommand command) {
		if (command instanceof AddQueryCommand) {
			this.handler.backup((AddQueryCommand) command, System.currentTimeMillis());
		}
	}

}
