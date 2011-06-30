package de.uniol.inf.is.odysseus.predicate;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Diese Klasse dient als Migrationshilfe um eine einfache Umstellung von 
 * complex predicates auf Mep Predicates zu realisieren
 * @author Marco Grawunder
 *
 */

public class ComplexPredicateBuilder {

	public static IPredicate createAndPredicate(IPredicate leftPredicate, IPredicate rightPredicate){
		return new AndPredicate(leftPredicate, rightPredicate);
	}
	
	public static IPredicate createOrPredicate(IPredicate leftPredicate, IPredicate rightPredicate){
		return new OrPredicate(leftPredicate, rightPredicate);
	}

	public static IPredicate createNotPredicate(IPredicate predicate){
		return new NotPredicate(predicate);
	}
	
	public static IPredicate pushDownNegation(
			IPredicate pred, boolean negatived) {
		if (pred instanceof NotPredicate) {
			return pushDownNegation(((NotPredicate) pred)
					.getChild(), !negatived);
		}
		if (pred instanceof ComplexPredicate) {
			ComplexPredicate compPred = (ComplexPredicate) pred;
			if (negatived) {
				if (pred instanceof OrPredicate) {
					compPred = new AndPredicate();
				} else {
					compPred = new OrPredicate();
				}
			}
			compPred.setLeft(pushDownNegation(compPred.getLeft(), negatived));
			compPred.setRight(pushDownNegation(compPred.getRight(), negatived));
			return compPred;
		}
		if (negatived) {
			return new NotPredicate(pred);
		} else {
			return pred;
		}
	}

	
	@SuppressWarnings("unchecked")
	public static List<IPredicate> splitPredicate(
			IPredicate predicate) {
		LinkedList<IPredicate> result = new LinkedList<IPredicate>();
		Stack<IPredicate> predicateStack = new Stack<IPredicate>();
		predicateStack.push(predicate);
		while (!predicateStack.isEmpty()) {
			IPredicate curPredicate = predicateStack.pop();
			if (curPredicate instanceof AndPredicate) {
				predicateStack
						.push((IPredicate) ((AndPredicate) curPredicate)
								.getLeft());
				predicateStack
						.push((IPredicate) ((AndPredicate) curPredicate)
								.getRight());
			} else {
				result.add(curPredicate);
			}
		}
		return result;
	}
	
}
