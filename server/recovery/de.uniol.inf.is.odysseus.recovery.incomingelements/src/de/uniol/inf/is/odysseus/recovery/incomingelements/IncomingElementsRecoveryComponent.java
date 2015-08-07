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
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryListener;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.queryadded.IQueryAddedListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.server.recovery.ISysLogEntry;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IOperatorWalker;
import de.uniol.inf.is.odysseus.core.util.LogicalGraphWalker;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.BaDaStRecorderRegistry;
import de.uniol.inf.is.odysseus.recovery.incomingelements.badastrecorder.BaDaStSender;
import de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.logicaloperator.SourceSyncAO;

/**
 * The incoming elements recovery component handles the backup and recovery of
 * incoming data streams. <br />
 * <br />
 * The component uses the Backup of Data Streams (BaDaSt) application.
 * 
 * @author Michael Brand
 *
 */
public class IncomingElementsRecoveryComponent implements IRecoveryComponent,
		IQueryAddedListener, IDataDictionaryListener {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(IncomingElementsRecoveryComponent.class);

	/**
	 * The server executor, if bound.
	 */
	private static Optional<IServerExecutor> cExecutor = Optional.absent();

	/**
	 * All data dictionaries, where this component listens to.
	 */
	private static Set<IDataDictionary> cUsedDataDictionaries = Sets
			.newHashSet();

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

	@Override
	public void recover(List<Integer> queryIds, ISession caller,
			List<ISysLogEntry> log) throws Exception {
		// TODO implement SourceStreamsRecoveryComponent.recover
	}

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig,
			ISession caller, List<ILogicalQuery> queries) {
		if (!cExecutor.isPresent()) {
			cLog.error("No executor bound!");
			return queries;
		}
		for (ILogicalQuery query : queries) {
			insertSourceSyncOperators(query, caller, cExecutor.get());
		}
		return queries;
	}

	/**
	 * Inserts a {@link SourceSyncAO} for each source access operator, if there
	 * is a recorder for the source.
	 * 
	 * @param query
	 *            The logical query to modify.
	 * @param caller
	 *            The user, which created the query.
	 * @param executor
	 *            A present executor.
	 */
	private void insertSourceSyncOperators(ILogicalQuery query,
			ISession caller, IServerExecutor executor) {
		LogicalGraphWalker graphWalker = new LogicalGraphWalker(
				collectOperators(query.getLogicalPlan()));
		graphWalker.walk(new IOperatorWalker<ILogicalOperator>() {

			/**
			 * All recorded sources.
			 */
			private final Set<String> mRecordedSources = BaDaStRecorderRegistry
					.getRecordedSources();

			@Override
			public void walk(ILogicalOperator operator) {
				// TODO Works only with AccessAO, not with StreamAO,
				// because I need the protocol and data handler
				if (AccessAO.class.isInstance(operator)
						&& mRecordedSources
								.contains(((AbstractAccessAO) operator)
										.getAccessAOName().getResourceName())) {
					AccessAO access = (AccessAO) operator;
					SourceSyncAO syncAO = new SourceSyncAO();
					syncAO.setBaDaStRecorder(BaDaStRecorderRegistry
							.getRecorder(access.getAccessAOName()
									.getResourceName()));
					syncAO.setSource(access);
					Collection<LogicalSubscription> subs = Lists
							.newArrayList(operator.getSubscriptions());
					operator.unsubscribeFromAllSinks();
					syncAO.subscribeToSource(operator, 0, 0,
							operator.getOutputSchema());
					for (LogicalSubscription sub : subs) {
						syncAO.subscribeSink(sub.getTarget(),
								sub.getSinkInPort(), sub.getSourceOutPort(),
								sub.getSchema());
					}
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
	private static List<ILogicalOperator> collectOperators(
			ILogicalOperator logicalPlan) {
		List<ILogicalOperator> operators = Lists.newArrayList();
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
	private static void collectOperatorsRecursive(ILogicalOperator operator,
			List<ILogicalOperator> operators) {
		operators.add(operator);
		for (LogicalSubscription sub : operator.getSubscribedToSource()) {
			collectOperatorsRecursive(sub.getTarget(), operators);
		}
	}

	@Override
	public IRecoveryComponent newInstance(Properties config) {
		return new IncomingElementsRecoveryComponent();
	}

	@Override
	public void queryAddedEvent(String query, List<Integer> queryIds,
			QueryBuildConfiguration buildConfig, String parserID,
			ISession user, Context context) {
		// Get data dictionary news
		IDataDictionary dict = cExecutor.get().getDataDictionary(user);
		if (!cUsedDataDictionaries.contains(dict)) {
			dict.addListener(this);
			cUsedDataDictionaries.add(dict);
		}
	}
	
	@Override
	public void removedViewDefinition(IDataDictionary sender, String name,
			ILogicalOperator op) {
		// name is user.sourcename
		String sourcename = name;
		int dotIndex = name.indexOf('.');
		if(dotIndex != -1) {
			sourcename = name.substring(dotIndex + 1);
		}
		
		// Stop the BaDaSt recorder
		String recorder = BaDaStRecorderRegistry.getRecorder(sourcename);
		if(recorder != null) {
			BaDaStSender.sendCloseCommand(recorder);
		}
	}

	@Override
	public void addedViewDefinition(IDataDictionary sender, String name,
			ILogicalOperator op) {
		// Nothing to do
	}

	@Override
	public void dataDictionaryChanged(IDataDictionary sender) {
		// Nothing to do
	}

}