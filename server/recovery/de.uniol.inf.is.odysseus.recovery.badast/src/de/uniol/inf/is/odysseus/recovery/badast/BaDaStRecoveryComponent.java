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
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryProvider;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
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

	/**
	 * An active session of the super user.
	 */
	private ISession activeSession = null;

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
	private void insertBaDaStSynchronizors(ILogicalQuery query, boolean recovery) {
		final Set<String> recordedSources = BaDaStRecorderRegistry.getRecordedSources();
		List<ILogicalOperator> operators = OperatorCollector.collect(query.getLogicalPlan().getRoot());
		new LogicalGraphWalker(operators).walk(op -> walkingStep(op, recordedSources, recovery));
	}

	/**
	 * Inserts a {@link BaDaStSyncAO} after each access operator.
	 */
	private void walkingStep(ILogicalOperator operator, Set<String> recordedSources, boolean recovery) {
		if (operator instanceof AbstractAccessAO
				&& recordedSources.contains(((AbstractAccessAO) operator).getAccessAOName().toString())) {
			AbstractAccessAO sourceAccess = (AbstractAccessAO) operator;
			BaDaStSyncAO sync = new BaDaStSyncAO(sourceAccess, recovery);
			Collection<LogicalSubscription> subs = new ArrayList<>(operator.getSubscriptions());
			operator.unsubscribeFromAllSinks();
			sync.subscribeToSource(operator, 0, 0, operator.getOutputSchema());
			for (LogicalSubscription sub : subs) {
				sync.subscribeSink(sub.getSink(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());
			}
		} else if (operator instanceof StreamAO) {
			List<ILogicalOperator> operators = OperatorCollector
					.collect(DataDictionaryProvider.instance.getDataDictionary(UserManagementProvider.instance.getDefaultTenant())
							.getStreamForTransformation(((StreamAO) operator).getStreamname(), getSession()).getRoot());
			new LogicalGraphWalker(operators).walk(op -> walkingStep(op, recordedSources, recovery));
		}
	}

	protected ISession getSession() {
		if (activeSession == null || !activeSession.isValid()) {
			activeSession = SessionManagement.instance.loginSuperUser(null);
		}
		return activeSession;
	}

}