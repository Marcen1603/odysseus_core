package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

/**
 * For a given join, find those selections, which (1) can be placed behind the
 * join and (2) can not be placed on one input side before the join. The
 * selections will be placed directly behind the join.
 * 
 * @author Michael Brand
 *
 */
public class RPregroupJoinSelectionRule extends AbstractRelationalRewriteRule<JoinAO> {

	@Override
	public void execute(JoinAO join, RewriteConfiguration config) throws RuleException {
		Collection<SelectAO> selections = findSelectionsToPlaceAfter(join);
		for (SelectAO select : selections) {
			moveSelectionBehind(select, join);
		}
	}

	private void moveSelectionBehind(SelectAO select, JoinAO join) {
		SelectAO newSelect = new SelectAO();
		newSelect.setPredicate(select.getPredicate());
		removeOperator(select);
		Collection<ILogicalOperator> toUpdate = LogicalPlan.insertOperatorBefore(newSelect, join);
		retract(select);
		insert(newSelect);
		for (ILogicalOperator operator : toUpdate) {
			update(operator);
		}
	}

	@Override
	public boolean isExecutable(JoinAO join, RewriteConfiguration config) {
		// True, if there is a selection, which can be placed directly behind
		// the join
		return !findSelectionsToPlaceAfter(join).isEmpty();
	}

	private static Collection<SelectAO> findSelectionsToPlaceAfter(JoinAO join) {
		List<SelectAO> selections = Lists.newArrayList();
		findSelectionsToPlaceAfterRecursive(join, join, selections, true);
		return selections;
	}

	// selectAOTrace = true <=> All operators between the join and the current operator are selections
	private static void findSelectionsToPlaceAfterRecursive(JoinAO join, ILogicalOperator currentOperator,
			Collection<SelectAO> foundSelections, boolean selectAOTrace) {
		for (LogicalSubscription sub : currentOperator.getSubscriptions()) {
			if (SelectAO.class.isInstance(sub.getSink()) && !join.equals(currentOperator)) {
				// Second clause is to avoid selecting the same selection over and over again.
				SelectAO select = (SelectAO) sub.getSink();
				// 1. If all operators between the join and selection are selections, don't change the order of the selections (endless loop of rule execution)
				// 2. Selection is executable direct behind the join
				// 3. Selection can not be switched with the join.
				if (!selectAOTrace && isExecutableAfter(select, join) && !RSwitchSelectionJoinRule.canSwitch(select, join)) {
					foundSelections.add(select);
				}
			}
			// selectAOTrace = true <=> All operators between the join and the current operator are selections
			findSelectionsToPlaceAfterRecursive(join, sub.getSink(), foundSelections, selectAOTrace && SelectAO.class.isInstance(sub.getSink()));
		}
	}

	private static boolean isExecutableAfter(SelectAO select, JoinAO join) {
		return subsetPredicate(select.getPredicate(), join.getOutputSchema());
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.PREGROUP;
	}

	@Override
	public Class<? super JoinAO> getConditionClass() {
		return JoinAO.class;
	}

	@Override
	public int getPriority() {
		return 0;
	}

}