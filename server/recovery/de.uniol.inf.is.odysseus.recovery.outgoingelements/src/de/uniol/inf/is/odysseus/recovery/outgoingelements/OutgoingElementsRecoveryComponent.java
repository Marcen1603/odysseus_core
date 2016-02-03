package de.uniol.inf.is.odysseus.recovery.outgoingelements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IOperatorWalker;
import de.uniol.inf.is.odysseus.core.util.LogicalGraphWalker;
import de.uniol.inf.is.odysseus.recovery.outgoingelements.transform.TSenderAOWithPreciseRecoveryRule;

/**
 * The outgoing elements recovery component handles the backup and recovery of
 * the progress of outgoing data streams in order to eliminate duplicates.
 * 
 * @author Michael Brand
 *
 */
@SuppressWarnings(value = { "nls" })
public class OutgoingElementsRecoveryComponent implements IRecoveryComponent {

	@Override
	public String getName() {
		return "Outgoing Elements";
	}

	@Override
	public IRecoveryComponent newInstance(Properties config) {
		OutgoingElementsRecoveryComponent instance = new OutgoingElementsRecoveryComponent();
		return instance;
	}

	@Override
	public List<ILogicalQuery> recover(QueryBuildConfiguration qbConfig, ISession caller, List<ILogicalQuery> queries) {
		// See TSenderWithPreciseRecoveryRule
		for (ILogicalQuery query : queries) {
			setRecoveryMode(query);
		}
		return queries;
	}

	@Override
	public List<ILogicalQuery> activateBackup(QueryBuildConfiguration qbConfig, ISession caller,
			List<ILogicalQuery> queries) {
		// Nothing to do. See TSenderWithPreciseRecoveryRule
		return queries;
	}

	/**
	 * Adds an option (key = "recovery", value = "true") to all
	 * {@link AbstractSenderAO}s to impact transformation. See
	 * {@link TSenderAOWithPreciseRecoveryRule}.
	 * 
	 * @param query
	 *            The query to recover.
	 */
	private static void setRecoveryMode(ILogicalQuery query) {
		final String OPTION_KEY = "recovery";
		List<ILogicalOperator> operators = new ArrayList<>(collectOperators(query.getLogicalPlan()));
		LogicalGraphWalker graphWalker = new LogicalGraphWalker(operators);
		graphWalker.walk(new IOperatorWalker<ILogicalOperator>() {

			@Override
			public void walk(ILogicalOperator operator) {
				if (AbstractSenderAO.class.isInstance(operator)) {
					((AbstractSenderAO) operator).getOptionsMap().put(OPTION_KEY, String.valueOf(true));
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
		Set<ILogicalOperator> operators = new HashSet<>();
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
