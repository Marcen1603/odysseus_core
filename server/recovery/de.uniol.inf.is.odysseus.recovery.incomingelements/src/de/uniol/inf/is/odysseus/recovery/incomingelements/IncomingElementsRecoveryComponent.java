package de.uniol.inf.is.odysseus.recovery.incomingelements;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.queryadded.IQueryAddedListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IOperatorWalker;
import de.uniol.inf.is.odysseus.core.util.LogicalGraphWalker;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.BaDaStRecorderRegistry;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.SourceRecoveryAO;
import de.uniol.inf.is.odysseus.recovery.incomingelements.trustpunctuation.logicaloperator.TrustPunctuationReaderAO;

/**
 * The incoming elements recovery component handles the backup and recovery of
 * incoming data streams. <br />
 * <br />
 * The component uses the Backup of Data Streams (BaDaSt) application.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class IncomingElementsRecoveryComponent
		implements IRecoveryComponent, IQueryAddedListener, IDataDictionaryListener {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory.getLogger(IncomingElementsRecoveryComponent.class);

	/**
	 * The server executor, if bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();

	/**
	 * All data dictionaries, where this component listens to.
	 */
	private static Set<IDataDictionary> cUsedDataDictionaries = Sets.newHashSet();

	/**
	 * Binds an implementation of the server executor.
	 * 
	 * @param executor
	 *            The implementation to bind.
	 */
	public void bindServerExecutor(IExecutor executor) {
		if (IServerExecutor.class.isInstance(executor)) {
			cExecutor = Optional.of((IServerExecutor) executor);
			((IServerExecutor) executor).addQueryAddedListener(this);
		}
	}

	/**
	 * Unbinds an implementation of the server executor.
	 * 
	 * @param executor
	 *            The implementation to unbind.
	 */
	public void unbindServerExecutor(IExecutor executor) {
		if (cExecutor.isPresent() && cExecutor.get() == executor) {
			cExecutor = Optional.absent();
			((IServerExecutor) executor).removeQueryAddedListener(this);
			for (IDataDictionary usedDict : cUsedDataDictionaries) {
				usedDict.removeListener(this);
			}
			cUsedDataDictionaries.clear();
		}
	}

	@Override
	public String getName() {
		return "Incoming Elements";
	}

	/**
	 * A set of all queries, which are a currently recovered. This means, they
	 * are an outcome of
	 * {@link #recover(QueryBuildConfiguration, ISession, List)}, but they are
	 * not yet an income of
	 * {@link #activateBackup(QueryBuildConfiguration, ISession, List)}.
	 */
	private final Set<ILogicalQuery> mCurrentlyRecoveredQueries = Sets.newHashSet();

	@Override
	public List<ILogicalQuery> recover(QueryBuildConfiguration qbConfig, ISession caller, List<ILogicalQuery> queries) {
		if (!cExecutor.isPresent()) {
			cLog.error("No executor bound!");
			return queries;
		}
		for (ILogicalQuery query : queries) {
			insertSourceSyncOperators(query, true);
			if (this.decreasedTrust != null) {
				this.insertTrustPunctuationReaderOperators(query, true);
			}
		}
		this.mCurrentlyRecoveredQueries.addAll(queries);
		return queries;
	}

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession caller,
			List<ILogicalQuery> queries) {
		if (!cExecutor.isPresent()) {
			cLog.error("No executor bound!");
			return queries;
		}
		for (ILogicalQuery query : queries) {
			if (this.mCurrentlyRecoveredQueries.contains(query)) {
				this.mCurrentlyRecoveredQueries.remove(query);
			} else {
				insertSourceSyncOperators(query, false);
				if (this.decreasedTrust != null) {
					this.insertTrustPunctuationReaderOperators(query, false);
				}
			}
		}
		return queries;
	}

	/**
	 * Inserts a {@link BaDaStAccessAO} for each source access operator, if
	 * there is a recorder for the source.
	 * 
	 * @param query
	 *            The logical query to modify.
	 * @param recoveryMode
	 *            True, if data stream elements shall be recovered from BaDaSt.
	 */
	private static void insertSourceSyncOperators(final ILogicalQuery query, final boolean recoveryMode) {
		List<ILogicalOperator> operators = Lists.newArrayList(collectOperators(query.getLogicalPlan()));
		LogicalGraphWalker graphWalker = new LogicalGraphWalker(operators);
		graphWalker.walk(new IOperatorWalker<ILogicalOperator>() {

			/**
			 * All recorded sources.
			 */
			private final Set<String> mRecordedSources = BaDaStRecorderRegistry.getRecordedSources();

			@Override
			public void walk(ILogicalOperator operator) {
				// XXX Works only with AbstractAccessAO, not with StreamAO,
				// because I need the protocol and data handler
				if (AbstractAccessAO.class.isInstance(operator)
						&& this.mRecordedSources.contains(((AbstractAccessAO) operator).getAccessAOName().toString())) {
					AbstractAccessAO sourceAccess = (AbstractAccessAO) operator;
					SourceRecoveryAO sourceRecovery = new SourceRecoveryAO(sourceAccess, recoveryMode);
					sourceRecovery.setUniqueIdentifier(
							((AbstractAccessAO) operator).getAccessAOName().getResourceName() + query.getID());
					sourceRecovery.addOwner(query);
					Collection<LogicalSubscription> subs = Lists.newArrayList(operator.getSubscriptions());
					operator.unsubscribeFromAllSinks();
					sourceRecovery.subscribeToSource(operator, 0, 0, operator.getOutputSchema());
					for (LogicalSubscription sub : subs) {
						sourceRecovery.subscribeSink(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(),
								sub.getSchema());
					}
				}
			}

		});
	}

	/**
	 * Inserts a {@link TrustPunctuationReaderAO} for each real sink.
	 * 
	 * @param query
	 *            The logical query to modify.
	 * @param recoveryMode
	 *            True, if trust shall be changed.
	 */
	private void insertTrustPunctuationReaderOperators(final ILogicalQuery query, boolean recoveryMode) {
		List<ILogicalOperator> operators = Lists.newArrayList(collectOperators(query.getLogicalPlan()));
		LogicalGraphWalker graphWalker = new LogicalGraphWalker(operators);
		graphWalker.walk(new IOperatorWalker<ILogicalOperator>() {

			@Override
			public void walk(ILogicalOperator operator) {
				if (operator.isSinkOperator() && !operator.isSourceOperator() && !(operator instanceof TopAO)) {
					TrustPunctuationReaderAO reader = new TrustPunctuationReaderAO(
							IncomingElementsRecoveryComponent.this.decreasedTrust.doubleValue(), recoveryMode);
					reader.addOwner(query);
					Collection<LogicalSubscription> subs = Lists.newArrayList(operator.getSubscribedToSource());
					operator.unsubscribeFromAllSources();
					SDFSchema schema = subs.iterator().next().getSchema();
					for (LogicalSubscription sub : subs) {
						reader.subscribeToSource(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(),
								sub.getSchema());
					}
					reader.subscribeSink(operator, 0, 0, schema);
				}
			}

		});
	}

	/**
	 * Collects all operators from a logical plan.
	 * 
	 * @param logicalPlan
	 *            The logical plan.
	 * @return All operators within the logical plan.
	 */
	private static Set<ILogicalOperator> collectOperators(ILogicalOperator logicalPlan) {
		Set<ILogicalOperator> operators = Sets.newHashSet();
		collectOperatorsRecursive(logicalPlan, operators);
		return operators;
	}

	/**
	 * Collects all operators from a logical plan recursively from top to
	 * sources.
	 * 
	 * @param operator
	 *            The current operator to add and check.
	 * @param operators
	 *            All already collected operators.
	 * @return All operators within the logical plan.
	 */
	private static void collectOperatorsRecursive(ILogicalOperator operator, Set<ILogicalOperator> operators) {
		operators.add(operator);
		for (LogicalSubscription sub : operator.getSubscribedToSource()) {
			collectOperatorsRecursive(sub.getTarget(), operators);
		}
	}

	/**
	 * The decreased trust value for {@link TrustPunctuationReaderAO}s, if set.
	 */
	Double decreasedTrust = null;

	@Override
	public IRecoveryComponent newInstance(Properties config) {
		IncomingElementsRecoveryComponent instance = new IncomingElementsRecoveryComponent();
		if (config.containsKey("decreasedTrust")) {
			instance.decreasedTrust = (Double) config.get("decreasedTrust");
		}
		return instance;
	}

	@Override
	public void queryAddedEvent(String query, List<Integer> queryIds, QueryBuildConfiguration buildConfig,
			String parserID, ISession user, Context context) {
		// Get data dictionary news
		IDataDictionary dict = cExecutor.get().getDataDictionary(user);
		if (!cUsedDataDictionaries.contains(dict)) {
			dict.addListener(this);
			cUsedDataDictionaries.add(dict);
		}
	}

	@Override
	public void removedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op, boolean isView,
			ISession session) {
		// Stop the BaDaSt recorder
		String recorder = BaDaStRecorderRegistry.getRecorder(name);
		if (recorder != null) {
			BaDaStSender.sendCloseCommand(recorder);
			BaDaStRecorderRegistry.unregister(name);
		}
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name, ILogicalOperator op, boolean isView,
			ISession session) {
		// Nothing to do
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// Nothing to do
	}

}