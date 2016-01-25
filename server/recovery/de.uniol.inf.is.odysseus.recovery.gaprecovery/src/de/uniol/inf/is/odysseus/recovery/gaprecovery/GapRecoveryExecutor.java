package de.uniol.inf.is.odysseus.recovery.gaprecovery;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.AbstractRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IOperatorWalker;
import de.uniol.inf.is.odysseus.core.util.LogicalGraphWalker;
import de.uniol.inf.is.odysseus.recovery.gaprecovery.logicaloperator.ConvergenceDetectorAO;
import de.uniol.inf.is.odysseus.trust.ITrust;

/**
 * The gap recovery executor represents a recovery strategy, which backups and
 * recovery query states (incl. sinks and sources).
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class GapRecoveryExecutor extends AbstractRecoveryExecutor {

	@Override
	public String getName() {
		return "GapRecovery";
	}

	@Override
	public List<ILogicalQuery> recover(QueryBuildConfiguration qbConfig, ISession caller, List<ILogicalQuery> queries) {
		// Insert convergence detectors

		// First idea was to insert them only, if stateful operators are used.
		// But not all operators, which may result in a convergence phase
		// implement IStatefulAO/PO. Even if all stateful operators would, what
		// about non-deterministic, stateless operators?

		// Second idea was to check, if convergence detectors are already
		// inserted (would be for a crash after a crash). This is not the case,
		// because this method is called after the backup of the query, so the
		// modification is not logged
		for (ILogicalQuery query : queries) {
			// For all time and element windows: insert a convergence detector
			// after it.

			List<ILogicalOperator> operators = Lists.newArrayList(collectOperators(query.getLogicalPlan()));
			LogicalGraphWalker graphWalker = new LogicalGraphWalker(operators);
			graphWalker.walk(new IOperatorWalker<ILogicalOperator>() {

				@Override
				public void walk(ILogicalOperator operator) {
					if (TimeWindowAO.class.isInstance(operator)) {
						TimeWindowAO tw = (TimeWindowAO) operator;
						// Advance may be null
						TimeValueItem advance = tw.getWindowAdvance();
						if (advance == null) {
							advance = new TimeValueItem(1, tw.getBaseTimeUnit());
						}
						ConvergenceDetectorAO convergenceDetector = new ConvergenceDetectorAO(tw.getWindowSize(),
								advance, tw.getBaseTimeUnit());
						insertConvergenceDetector(tw, convergenceDetector);
					} else if (ElementWindowAO.class.isInstance(operator)) {
						ElementWindowAO ew = (ElementWindowAO) operator;
						// Advance may be null
						Long advance = ew.getWindowAdvanceE();
						if (advance == null) {
							advance = new Long(1);
						}
						ConvergenceDetectorAO convergenceDetector = new ConvergenceDetectorAO(
								ew.getWindowSizeE().longValue(), advance.longValue());
						insertConvergenceDetector(ew, convergenceDetector);
					}
				}

				private void insertConvergenceDetector(AbstractWindowAO windowAO,
						ConvergenceDetectorAO convergenceDetector) {
					SDFSchema schema = windowAO.getInputSchema();
					if (!schema.hasMetatype(ITimeInterval.class) || !schema.hasMetatype(ITrust.class)) {
						return;
					}
					Collection<LogicalSubscription> subs = Lists.newArrayList(windowAO.getSubscriptions());
					windowAO.unsubscribeFromAllSinks();
					convergenceDetector.subscribeToSource(windowAO, 0, 0, schema);
					for (LogicalSubscription sub : subs) {
						convergenceDetector.subscribeSink(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(),
								sub.getSchema());
					}
				}

			});
		}

		return queries;
	}

	/**
	 * Nothing to do, because backup of query states (incl. sinks and sources)
	 * is done globally.
	 */
	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession caller,
			List<ILogicalQuery> queries) {
		// Nothing to do.
		return queries;
	}

	@Override
	public IRecoveryExecutor newInstance(Properties config) {
		GapRecoveryExecutor instance = new GapRecoveryExecutor();
		instance.mConfig = config;
		return instance;
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

}