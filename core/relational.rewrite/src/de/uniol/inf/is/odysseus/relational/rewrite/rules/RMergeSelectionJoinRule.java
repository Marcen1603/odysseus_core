package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.Collection;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.engine.RewriteConfiguration;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;

public class RMergeSelectionJoinRule extends AbstractRewriteRule<JoinAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(JoinAO join, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (isValidSelectAO(sel, join)) {
				if (sel.getPredicate() != null) {
					if (join.getPredicate() != null) {
						join.setPredicate(new AndPredicate(join.getPredicate(), sel.getPredicate()));
					} else {
						join.setPredicate(sel.getPredicate());
					}
					Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.removeOperator(sel);
					for (ILogicalOperator o : toUpdate) {
						update(o);
					}
					update(join);
					retract(sel);
				}
			}
		}

	}

	@Override
	public boolean isExecutable(JoinAO join, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (isValidSelectAO(sel, join)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Merge Selection Join";
	}

	private boolean isValidSelectAO(SelectAO sel, JoinAO join) {
		if (sel.getPredicate() != null) {
			Set<?> sources = RelationalRestructHelper.sourcesOfPredicate(sel.getPredicate());
			ILogicalOperator left = join.getLeftInput();
			ILogicalOperator right = join.getRightInput();
			if (!RelationalRestructHelper.containsAllSources(left, sources)) {
				if (!RelationalRestructHelper.containsAllSources(right, sources)) {
					if (RelationalRestructHelper.containsAllSources(join, sources)) {
						if (sel.getPredicate() != null) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
