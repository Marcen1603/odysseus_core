package de.uniol.inf.is.odysseus.recovery.installedqueries;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AbstractDropStreamOrViewCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.AddQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.DropSinkCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.PartialQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.RemoveQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.ResumeQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.StartQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.StopQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.query.SuspendQueryCommand;
import de.uniol.inf.is.odysseus.systemlog.ISystemLog;

/**
 * Component that backups and recovers installed sources, sinks and queries.
 * <br />
 * <br />
 * Note that sources, sinks and queries get automatically backed up and
 * installed, when this component is active. The changes of query states, e.g.
 * starting a query, will also be automatically backed up, but not automatically
 * restored. This is because in some cases, recovery strategies do some
 * processing between reinstallation and restarting a query. To restore the
 * query states, call {@link #recoverQueryStates()}.
 *
 * @author Michael Brand
 *
 */
public interface IInstalledQueriesHandler {

	/**
	 * Backup of a source removal.
	 *
	 * @param cmd
	 *            The {@link IExecutorCommand} that deleted the source.
	 * @param ts
	 *            The time stamp for the {@link ISystemLog}.
	 */
	public void backup(AbstractDropStreamOrViewCommand cmd, long ts);

	/**
	 * Backup of a sink removal.
	 *
	 * @param cmd
	 *            The {@link IExecutorCommand} that deleted the sink.
	 * @param ts
	 *            The time stamp for the {@link ISystemLog}.
	 */
	public void backup(DropSinkCommand cmd, long ts);

	/**
	 * Backup of a new query (part 2). <br />
	 * Works only together with {@link #backup(CreateQueryCommand, long)}.
	 *
	 * @param cmd
	 *            The {@link IExecutorCommand} that created the source.
	 * @param ts
	 *            The time stamp for the {@link ISystemLog}.
	 */
	public void backup(AddQueryCommand cmd, long ts);

	/**
	 * Backup of a query removal.
	 *
	 * @param cmd
	 *            The {@link IExecutorCommand} that deleted the query.
	 * @param ts
	 *            The time stamp for the {@link ISystemLog}.
	 */
	public void backup(RemoveQueryCommand cmd, long ts);

	/**
	 * Backup of a query change after that the query gets only a partial of the
	 * incoming streams.
	 *
	 * @param cmd
	 *            The {@link IExecutorCommand} that changed the query.
	 * @param ts
	 *            The time stamp for the {@link ISystemLog}.
	 */
	public void backup(PartialQueryCommand cmd, long ts);

	/**
	 * Backup of a query change after that the query is running.
	 *
	 * @param cmd
	 *            The {@link IExecutorCommand} that changed the query.
	 * @param ts
	 *            The time stamp for the {@link ISystemLog}.
	 */
	public void backup(StartQueryCommand cmd, long ts);

	/**
	 * Backup of a query change after that the query is inactive.
	 *
	 * @param cmd
	 *            The {@link IExecutorCommand} that changed the query.
	 * @param ts
	 *            The time stamp for the {@link ISystemLog}.
	 */
	public void backup(StopQueryCommand cmd, long ts);

	/**
	 * Backup of a query change after that the query is suspended.
	 *
	 * @param cmd
	 *            The {@link IExecutorCommand} that changed the query.
	 * @param ts
	 *            The time stamp for the {@link ISystemLog}.
	 */
	public void backup(SuspendQueryCommand cmd, long ts);

	/**
	 * Backup of a query change after that the query is running again (after
	 * suspension).
	 *
	 * @param cmd
	 *            The {@link IExecutorCommand} that changed the query.
	 * @param ts
	 *            The time stamp for the {@link ISystemLog}.
	 */
	public void backup(ResumeQueryCommand cmd, long ts);

}