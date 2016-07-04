package de.uniol.inf.is.odysseus.recovery.badast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.badast.recorder.BaDaStRecorderRegistry;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IOperatorWalker;
import de.uniol.inf.is.odysseus.core.util.LogicalGraphWalker;
import de.uniol.inf.is.odysseus.core.util.OperatorCollector;
import de.uniol.inf.is.odysseus.recovery.badast.logicaloperator.BaDaStSyncAO;

/**
 * The BaDaSt recovery component handles the backup and recovery of incoming
 * data streams. <br />
 * <br />
 * The component uses the Backup of Data Streams (BaDaSt) application.
 * 
 * @author Michael Brand
 *
 */
public class BaDaStRecoveryComponent implements IRecoveryComponent {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(BaDaStRecoveryComponent.class);

	@Override
	public void initialize(Properties config) {
		// Nothing to do.
	}

	/**
	 * The ids of all queries, recovery is activated for.
	 */
	private static final Set<Integer> queryIdsForRecovery = new HashSet<>();

	@Override
	public List<ILogicalQuery> activateRecovery(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		LOG.info("Inserting BaDaSt synchronizors...");
		for (ILogicalQuery query : queries) {
			queryIdsForRecovery.add(query.getID());
			insertBaDaStSynchronizors(query, true);
		}
		LOG.info("... done");
		return queries;
	}

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		LOG.info("Inserting BaDaSt synchronizors...");
		for (ILogicalQuery query : queries) {
			if (!queryIdsForRecovery.contains(query.getID())) {
				insertBaDaStSynchronizors(query, false);
			}
		}
		LOG.info("... done");
		return queries;
	}

	/**
	 * Inserts a {@link BaDaStSyncAO} after each access operator.
	 */
	private static void insertBaDaStSynchronizors(ILogicalQuery query, boolean recovery) {
		List<ILogicalOperator> operators = OperatorCollector.collect(query.getLogicalPlan());
		new LogicalGraphWalker(operators).walk(new IOperatorWalker<ILogicalOperator>() {

			/**
			 * All recorded sources.
			 */
			private final Set<String> recordedSources = BaDaStRecorderRegistry.getRecordedSources();

			@Override
			public void walk(ILogicalOperator operator) {
				// XXX Works only with AbstractAccessAO, not with StreamAO,
				// because I need the protocol and data handler
				if (AbstractAccessAO.class.isInstance(operator)
						&& recordedSources.contains(((AbstractAccessAO) operator).getAccessAOName().toString())) {
					AbstractAccessAO sourceAccess = (AbstractAccessAO) operator;
					BaDaStSyncAO sync = new BaDaStSyncAO(sourceAccess, recovery);
					Collection<LogicalSubscription> subs = new ArrayList<>(operator.getSubscriptions());
					operator.unsubscribeFromAllSinks();
					sync.subscribeToSource(operator, 0, 0, operator.getOutputSchema());
					for (LogicalSubscription sub : subs) {
						sync.subscribeSink(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(),
								sub.getSchema());
					}
				}
			}
		});
	}

}