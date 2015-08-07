package de.uniol.inf.is.odysseus.recovery.incomingelements;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.server.recovery.ISysLogEntry;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IOperatorWalker;
import de.uniol.inf.is.odysseus.core.util.LogicalGraphWalker;
import de.uniol.inf.is.odysseus.recovery.incomingelements.kafkaconsumer.BaDaStSender;
import de.uniol.inf.is.odysseus.recovery.incomingelements.logicaloperator.SourceSyncAO;

/**
 * The incoming elements recovery component handles the backup and recovery of
 * incoming data streams. <br />
 * <br />
 * The component uses the Backup of Data Streams (BaDaSt) application.
 * 
 * @author Michael Brand
 *
 */
public class IncomingElementsRecoveryComponent implements IRecoveryComponent {

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
	 * Binds an implementation of the server executor.
	 * 
	 * @param executor
	 *            The implementation to bind.
	 */
	public void bindServerExecutor(IExecutor executor) {
		if (IServerExecutor.class.isInstance(executor)) {
			cExecutor = Optional.of((IServerExecutor) executor);
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

			@Override
			public void walk(ILogicalOperator operator) {
				// TODO Works only with AccessAO, not with StreamAO,
				// because I need the protocol and data handler
				if (AccessAO.class.isInstance(operator)
						&& IncomingElementsRecoveryComponent.this.mBaDaStRecorders
								.keySet().contains(
										((AbstractAccessAO) operator)
												.getAccessAOName()
												.getResourceName())) {
					AccessAO access = (AccessAO) operator;
					SourceSyncAO syncAO = new SourceSyncAO();
					syncAO.setBaDaStRecorder(IncomingElementsRecoveryComponent.this.mBaDaStRecorders
							.get(access.getAccessAOName().getResourceName()));
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
		IncomingElementsRecoveryComponent component = new IncomingElementsRecoveryComponent();
		Map<String, Properties> sourceConfigurations = determineSourceConfigurations(config);
		for (String sourcename : sourceConfigurations.keySet()) {
			String badastRecorder = BaDaStSender
					.sendCreateCommand(sourceConfigurations.get(sourcename));
			component.mBaDaStRecorders.put(sourcename, badastRecorder);
			component.mDataHandlers.put(
					sourcename,
					sourceConfigurations.get(sourcename).getProperty(
							"datahandler"));
			BaDaStSender.sendStartCommand(badastRecorder);
		}
		return component;
	}

	/**
	 * Determines the configuration for the BaDaSt recorders.
	 * 
	 * @param config
	 *            The complete configuration for the recovery component.
	 * @return The configurations of the BaDaSt recorders mapped to the source
	 *         names.
	 */
	private static Map<String, Properties> determineSourceConfigurations(
			Properties config) {
		final String prefix = "source";
		Map<Integer, Properties> sourceStreamsConfigsTmp = Maps.newHashMap();
		for (String key : config.stringPropertyNames()) {
			if (key.toLowerCase().startsWith(prefix)) {
				int dotIndex = key.indexOf(".");
				if (dotIndex != -1) {
					int sourceNumber = Integer.parseInt(key.substring(
							prefix.length(), dotIndex));
					Properties sourceStreamsConfig;
					if (sourceStreamsConfigsTmp.containsKey(sourceNumber)) {
						sourceStreamsConfig = sourceStreamsConfigsTmp
								.get(sourceNumber);
					} else {
						sourceStreamsConfig = new Properties();
					}
					sourceStreamsConfig.setProperty(
							key.substring(dotIndex + 1),
							config.getProperty(key));
					sourceStreamsConfigsTmp.put(sourceNumber,
							sourceStreamsConfig);
				}
			}
		}

		Map<String, Properties> out = Maps.newHashMap();
		for (int sourceNumber : sourceStreamsConfigsTmp.keySet()) {
			String sourcename = sourceStreamsConfigsTmp.get(sourceNumber)
					.getProperty("sourcename");
			out.put(sourcename, sourceStreamsConfigsTmp.get(sourceNumber));
		}
		return out;
	}

	/**
	 * The name of the BaDaSt recorders mapped to the source names.
	 */
	private final Map<String, String> mBaDaStRecorders = Maps.newHashMap();

	/**
	 * The name of the data handlers mapped to the source names.
	 */
	private final Map<String, String> mDataHandlers = Maps.newHashMap();

}