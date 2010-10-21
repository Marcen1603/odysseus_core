package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Stack;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TInitPredicateRule extends AbstractTransformationRule<ILogicalOperator> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ILogicalOperator operator,
			TransformationConfiguration config) {
		operator.getPredicate().init();
		visitPredicates(operator.getPredicate(), new InitPredicateFunctor(operator));
	}

	@Override
	public boolean isExecutable(ILogicalOperator operator,
			TransformationConfiguration config) {
		return (operator.getPredicate()!=null);
	}

	@Override
	public String getName() {
		return "Init Predicates";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.INIT;
	}
	
	public static interface IUnaryFunctor<T> {
		public void call(T parameter);
	}
	
	public static void visitPredicates(IPredicate<?> p,
			IUnaryFunctor<IPredicate<?>> functor) {
		Stack<IPredicate<?>> predicates = new Stack<IPredicate<?>>();
		predicates.push(p);
		while (!predicates.isEmpty()) {
			IPredicate<?> curPred = predicates.pop();
			if (curPred instanceof ComplexPredicate<?>) {
				predicates.push(((ComplexPredicate<?>) curPred).getLeft());
				predicates.push(((ComplexPredicate<?>) curPred).getRight());
			} else if(curPred instanceof NotPredicate){
				predicates.push(((NotPredicate<?>) curPred).getChild());
			}
			else {
				functor.call(curPred);
			}
		}
	}



}
