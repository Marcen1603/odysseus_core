package de.uniol.inf.is.odysseus.recovery.duplicateelemination;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IOperatorWalker;
import de.uniol.inf.is.odysseus.core.util.LogicalGraphWalker;
import de.uniol.inf.is.odysseus.core.util.OperatorCollector;
import de.uniol.inf.is.odysseus.recovery.duplicateelemination.transform.TSenderAORule;

/**
 * The duplicate elimination component handles the backup and recovery of the
 * progress of outgoing data streams in and eliminates duplicates.
 * 
 * @author Michael Brand
 *
 */
public class DuplicateEliminationRecoveryComponent implements IRecoveryComponent {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DuplicateEliminationRecoveryComponent.class);

	/**
	 * The key to put in the options map of SenderAOs to activate
	 * {@link TSenderAORule}.
	 */
	public static final String DUPLICATEELIMINATION_OPTION_KEY = "duplicateelimination";

	@Override
	public void initialize(Properties config) {
		// Nothing to do.
	}

	@Override
	public List<ILogicalQuery> activateRecovery(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		LOG.info("Inserting senders with duplicate elimination...");
		for (ILogicalQuery query : queries) {
			setRecoveryMode(query, true);
		}
		LOG.info("... done");
		return queries;
	}

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		LOG.info("Inserting senders with backup option...");
		for (ILogicalQuery query : queries) {
			setRecoveryMode(query, false);
		}
		LOG.info("... done");
		return queries;
	}

	/**
	 * Adds an option (see {@link #DUPLICATEELIMINATION_OPTION_KEY}) to all
	 * {@link AbstractSenderAO}s to impact transformation. See
	 * {@link TSenderAORule}.
	 * 
	 * @param query
	 *            The query to recover.
	 */
	private static void setRecoveryMode(ILogicalQuery query, boolean recovery) {
		LogicalGraphWalker graphWalker = new LogicalGraphWalker(OperatorCollector.collect(query.getLogicalPlan()));
		graphWalker.walk(new IOperatorWalker<ILogicalOperator>() {

			@Override
			public void walk(ILogicalOperator operator) {
				if (AbstractSenderAO.class.isInstance(operator)) {
					AbstractSenderAO sender = (AbstractSenderAO) operator;
					if (!sender.getOptionsMap().containsKey(DUPLICATEELIMINATION_OPTION_KEY) || recovery) {
						// backup is called after recovery. So
						// duplicateelimination=true will be overridden
						// otherwise
						sender.getOptionsMap().setOption(DUPLICATEELIMINATION_OPTION_KEY, String.valueOf(recovery));
					}
				}
			}

		});
	}

}