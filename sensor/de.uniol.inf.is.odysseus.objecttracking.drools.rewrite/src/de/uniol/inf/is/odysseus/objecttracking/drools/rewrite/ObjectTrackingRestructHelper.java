package de.uniol.inf.is.odysseus.objecttracking.drools.rewrite;

import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.IRangePredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;

public class ObjectTrackingRestructHelper {

	public static void visitPredicates(Map<IPredicate, IRangePredicate> predicates,
			ILogicalOperator operator) {
		
		for(Entry<IPredicate, IRangePredicate> entry: predicates.entrySet()){
			Stack<IPredicate> predicateStack = new Stack<IPredicate>();
			predicateStack.push(entry.getKey());
			
			while (!predicateStack.isEmpty()) {
				IPredicate curPred = predicateStack.pop();
				if (curPred instanceof ComplexPredicate<?>) {
					predicateStack.push(((ComplexPredicate) curPred).getLeft());
					predicateStack.push(((ComplexPredicate) curPred).getRight());
				} else {
					init(curPred, operator);
				}
			}
			
			entry.getValue().init(operator.getInputSchema(0), operator.getInputSchema(1));	
		}
	}
	
	private static void init(IPredicate predicate, ILogicalOperator logicalOp){
		if(predicate instanceof IRelationalPredicate) {
			// We can always init with the left and right input schema.
			// There are two cases:
			// 1. The relational predicate is used as a key to get the correct
			//    prediction function. In this case, if the predicate only
			//    uses attributes from the left. The field fromRightChannel
			//    will be false for each attribute. If it uses only attributes
			//    from the right fromRightChannel will be true, for each predicate.
			// 2. The relational predicate is used as a condition predicate
			//    for a maple solution. In this case, if the predicate
			//    only uses attributes from the left, the field fromRightChannel
			//    will be false for every attribute. If the predicate only uses
			//    attributes from the right fromRightChannel will be true for
			//    every attribute.
			((IRelationalPredicate)predicate).init(logicalOp.getInputSchema(0), logicalOp.getInputSchema(1));				
		}
	}
	
}
