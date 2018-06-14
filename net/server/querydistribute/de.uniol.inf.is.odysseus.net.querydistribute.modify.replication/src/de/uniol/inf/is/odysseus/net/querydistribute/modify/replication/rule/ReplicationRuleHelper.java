package de.uniol.inf.is.odysseus.net.querydistribute.modify.replication.rule;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.net.querydistribute.modify.replication.ReplicationParameterHelper;

/**
 * A replication rule helper provides replication rules and methods to handle
 * them.
 * 
 * @author Michael Brand
 */
public class ReplicationRuleHelper {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(ReplicationRuleHelper.class);

	/**
	 * All bound {@link IReplicationRule}s.
	 */
	private static Collection<IReplicationRule<ILogicalOperator>> rules = Lists
			.newArrayList();

	/**
	 * Returns all bound {@link IReplicationRule}s.
	 */
	public static Collection<IReplicationRule<ILogicalOperator>> getReplicationRules() {

		return ReplicationRuleHelper.rules;

	}

	/**
	 * Binds an {@link IReplicationRule}.
	 * 
	 * @param rule
	 *            The {@link IReplicationRule} to bind.
	 */
	@SuppressWarnings("unchecked")
	public static void bindReplicationRule(IReplicationRule<?> rule) {

		ReplicationRuleHelper.rules
				.add((IReplicationRule<ILogicalOperator>) rule);
		ReplicationRuleHelper.LOG.debug("Bound replication rule '{}'", rule
				.getClass().getSimpleName());

	}

	/**
	 * Unbinds an {@link IReplicationRule} if <code>rule</code> is a bound one.
	 * 
	 * @param rule
	 *            The {@link IReplicationRule} to unbind.
	 */
	public static void unbindReplicationRule(IReplicationRule<?> rule) {

		if (rule != null) {

			ReplicationRuleHelper.rules.remove(rule);
			ReplicationRuleHelper.LOG.debug("Unbound replication rule '{}'",
					rule.getClass().getSimpleName());

		}

	}

	/**
	 * Checks, if a given operator can be replicated.
	 * 
	 * @param operator
	 *            The given operator.
	 * @param helper
	 *            The {@link ReplicationParameterHelper} instance.
	 * @return True, if <code>operator</code> can be replicated.
	 */
	public static boolean canOperatorBeReplicated(ILogicalOperator operator,
			ReplicationParameterHelper helper) {

		Collection<IReplicationRule<ILogicalOperator>> rules = ReplicationRuleHelper
				.getReplicationRules(operator);

		for (IReplicationRule<ILogicalOperator> rule : rules) {

			if (!rule.canOperatorBeReplicated(operator, helper)) {

				return false;

			}

		}

		return true;

	}

	/**
	 * Searches for replication rules for a given operator.
	 * 
	 * @param operator
	 *            The given operator.
	 * @return All available rules.
	 */
	public static Collection<IReplicationRule<ILogicalOperator>> getReplicationRules(
			ILogicalOperator operator) {

		Preconditions.checkNotNull(operator, "Operator must be not null!");

		Collection<IReplicationRule<ILogicalOperator>> rules = Lists
				.newArrayList();

		for (IReplicationRule<ILogicalOperator> rule : ReplicationRuleHelper
				.getReplicationRules()) {

			if (rule.getOperatorClass().isAssignableFrom(operator.getClass())) {

				rules.add(rule);

			}

		}

		return rules;

	}

}