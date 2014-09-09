package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.rule;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationParameterHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationQueryPartModificator;

/**
 * A fragmentation rule helper provides fragmentation rules and methods to
 * handle them.
 * 
 * @author Michael Brand
 */
public class FragmentationRuleHelper {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(FragmentationRuleHelper.class);

	/**
	 * All bound {@link IFragmentationRule}s.
	 */
	private static Collection<IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator>> rules = Lists
			.newArrayList();

	/**
	 * Returns all bound {@link IFragmentationRule}s.
	 */
	public static Collection<IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator>> getFragmentationRules() {

		return FragmentationRuleHelper.rules;

	}

	/**
	 * Binds an {@link IFragmentationRule}.
	 * 
	 * @param rule
	 *            The {@link IFragmentationRule} to bind.
	 */
	@SuppressWarnings("unchecked")
	public static void bindFragmentationRule(IFragmentationRule<?, ?> rule) {

		FragmentationRuleHelper.rules
				.add((IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator>) rule);
		FragmentationRuleHelper.LOG.debug("Bound fragmentation rule '{}'", rule
				.getClass().getSimpleName());

	}

	/**
	 * Unbinds an {@link IFragmentationRule} if <code>rule</code> is a bound
	 * one.
	 * 
	 * @param rule
	 *            The {@link IFragmentationRule} to unbind.
	 */
	public static void unbindFragmentationRule(IFragmentationRule<?, ?> rule) {

		if (rule != null) {

			FragmentationRuleHelper.rules.remove(rule);
			FragmentationRuleHelper.LOG.debug(
					"Unbound fragmentation rule '{}'", rule.getClass()
							.getSimpleName());

		}

	}

	/**
	 * Checks, if a given operator can be part of a fragment.
	 * 
	 * @param strategy
	 *            The given strategy.
	 * @param operator
	 *            The given operator.
	 * @param helper
	 *            The {@link AbstractFragmentationParameterHelper} instance.
	 * @return True, if <code>operator</code> can be executed in
	 *         intra-operational parallelism and therefore be part of a
	 *         fragment.
	 */
	public static boolean canOperatorBePartOfFragment(
			AbstractFragmentationQueryPartModificator strategy,
			ILogicalOperator operator,
			AbstractFragmentationParameterHelper helper) {

		Collection<IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator>> rules = FragmentationRuleHelper
				.getFragmentationRules(strategy, operator);

		for (IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator> rule : rules) {

			if (!rule.canOperatorBePartOfFragments(strategy, operator, helper)) {

				return false;

			}

		}

		return true;

	}

	/**
	 * Checks if a query part needs a special handling. <br />
	 * This may be the usage of partial aggregates.
	 * 
	 * @param part
	 *            The query part.
	 * @param strategy
	 *            The given strategy.
	 * @param helper
	 *            The current fragmentation helper.
	 * @return True, if <code>part</code> needs a special handling. This makes
	 *         direct connection without fragmentation and/or reunion operators
	 *         impossible.
	 */
	public static boolean needSpecialHandlingForQueryPart(
			ILogicalQueryPart part,
			AbstractFragmentationQueryPartModificator strategy,
			AbstractFragmentationParameterHelper helper) {

		for (ILogicalOperator operator : part.getOperators()) {

			Collection<IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator>> rules = FragmentationRuleHelper
					.getFragmentationRules(strategy, operator);

			for (IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator> rule : rules) {

				if (rule.needSpecialHandlingForQueryPart(part, operator, helper)) {

					return true;

				}

			}

		}

		return false;

	}

	/**
	 * Searches for fragmentation rules for a given combination of a
	 * fragmentation strategy and an operator.
	 * 
	 * @param strategy
	 *            The given fragmentation strategy.
	 * @param operator
	 *            The given operator.
	 * @return All available rules.
	 */
	public static Collection<IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator>> getFragmentationRules(
			AbstractFragmentationQueryPartModificator strategy,
			ILogicalOperator operator) {

		Preconditions.checkNotNull(strategy,
				"Fragmentation strategy must be not null!");
		Preconditions.checkNotNull(operator, "Operator must be not null!");

		Collection<IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator>> rules = Lists
				.newArrayList();

		for (IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator> rule : FragmentationRuleHelper
				.getFragmentationRules()) {

			if (rule.getStrategyClass().isAssignableFrom(strategy.getClass())
					&& rule.getOperatorClass().isAssignableFrom(
							operator.getClass())) {

				rules.add(rule);

			}

		}

		return rules;

	}

}