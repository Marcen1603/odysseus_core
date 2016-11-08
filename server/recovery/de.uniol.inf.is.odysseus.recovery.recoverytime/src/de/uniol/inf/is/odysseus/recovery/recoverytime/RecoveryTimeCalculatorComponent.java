package de.uniol.inf.is.odysseus.recovery.recoverytime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IOperatorWalker;
import de.uniol.inf.is.odysseus.core.util.LogicalGraphWalker;
import de.uniol.inf.is.odysseus.core.util.OperatorCollector;
import de.uniol.inf.is.odysseus.recovery.recoverytime.IRecoveryTime;
import de.uniol.inf.is.odysseus.recovery.recoverytime.logicaloperator.RecoveryTimeCalculatorAO;
import de.uniol.inf.is.odysseus.trust.ITrust;

/**
 * Component that inserts {@link RecoveryTimeCalculatorAO}s before each sink in
 * order to calculate the {@link IRecoveryTime}.
 *
 * @author Michael Brand
 *
 */
public class RecoveryTimeCalculatorComponent implements IRecoveryComponent {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(RecoveryTimeCalculatorComponent.class);

	@Override
	public void initialize(Properties config) {
		// Nothing to do.
	}

	@Override
	public List<ILogicalQuery> activateRecovery(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		LOG.info("Inserting recovery time calculators...");
		queries.stream().forEach(query -> insertRecoveryTimeCalculators(query));
		LOG.info("... done");
		return queries;
	}

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession session,
			List<ILogicalQuery> queries, IExecutor caller) {
		// Nothing to do.
		return queries;
	}

	/**
	 * Inserts a {@link RecoveryTimeCalculatorAO} before each sink.
	 */
	private static void insertRecoveryTimeCalculators(ILogicalQuery query) {
		List<ILogicalOperator> operators = OperatorCollector.collect(query.getLogicalPlan());
		new LogicalGraphWalker(operators).walk(new IOperatorWalker<ILogicalOperator>() {

			@Override
			public void walk(ILogicalOperator operator) {
				if (operator.isSinkOperator() && !operator.isSourceOperator()) {
					insertRecoveryTimeCalculator(operator, new RecoveryTimeCalculatorAO());
				}
			}
		});
	}

	/**
	 * Inserts a given {@link RecoveryTimeCalculatorAO} before a given sink.
	 */
	static void insertRecoveryTimeCalculator(ILogicalOperator sink, RecoveryTimeCalculatorAO calculator) {
		Collection<LogicalSubscription> subs = new ArrayList<>(sink.getSubscribedToSource());
		sink.unsubscribeFromAllSources();
		subs.stream().forEach(sub -> insertRecoveryTimeCalculator(sink, sub, calculator));
	}

	/**
	 * Inserts a given {@link RecoveryTimeCalculatorAO} before a given sink.
	 */
	private static void insertRecoveryTimeCalculator(ILogicalOperator sink, LogicalSubscription subToSink,
			RecoveryTimeCalculatorAO calculator) {
		SDFSchema schema = subToSink.getSchema();
		if (!schema.hasMetatype(IRecoveryTime.class) || !schema.hasMetatype(ITrust.class)
				|| !schema.hasMetatype(ITimeInterval.class)) {
			return;
		}
		calculator.subscribeToSource(subToSink.getTarget(), 0, subToSink.getSourceOutPort(), subToSink.getSchema());
		sink.subscribeToSource(calculator, 0, subToSink.getSourceOutPort(), subToSink.getSchema());
	}

}