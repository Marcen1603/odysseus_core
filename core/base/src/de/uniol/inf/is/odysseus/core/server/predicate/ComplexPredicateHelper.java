package de.uniol.inf.is.odysseus.core.server.predicate;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * Diese Klasse dient als Migrationshilfe um eine einfache Umstellung von 
 * complex predicates auf Mep Predicates zu realisieren
 * @author Marco Grawunder
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ComplexPredicateHelper {

	
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

	public static List<IPredicate> splitPredicate(
			IPredicate predicate) {
		LinkedList<IPredicate> result = new LinkedList<IPredicate>();
		Stack<IPredicate> predicateStack = new Stack<IPredicate>();
		predicateStack.push(predicate);
		while (!predicateStack.isEmpty()) {
			IPredicate curPredicate = predicateStack.pop();
			if (curPredicate instanceof AndPredicate) {
				predicateStack
						.push(((AndPredicate) curPredicate)
								.getLeft());
				predicateStack
						.push(((AndPredicate) curPredicate)
								.getRight());
			} else {
				result.add(curPredicate);
			}
		}
		return result;
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
	
	public static boolean isAndPredicate(IPredicate pred){
		return (pred instanceof AndPredicate);
	}
	
	public static boolean isOrPredicate(IPredicate pred){
		return pred instanceof OrPredicate;
	}
	
	public static boolean isNotPredicate(IPredicate pred){
		return pred instanceof NotPredicate;
	}
	
	public static boolean contains(IPredicate oPred, IPredicate pred){
		if (oPred instanceof OrPredicate){
			return ((OrPredicate)oPred).contains(pred);
		}else{
			return false;
		}
	}

	public static IPredicate getChild(IPredicate notPred) {
		if (notPred instanceof NotPredicate){
		return ((NotPredicate) notPred).getChild();
		}else{
			throw new IllegalArgumentException("Argument is not a NotPredicate");
		}
	}
}
