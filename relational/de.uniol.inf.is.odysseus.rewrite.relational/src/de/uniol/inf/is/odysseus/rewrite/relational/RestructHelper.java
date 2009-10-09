package de.uniol.inf.is.odysseus.rewrite.relational;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.RenameAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;



/**
 * This class provides functions to support Restructuring Aspects
 * 
 * @author Marco Grawunder
 * 
 */

public class RestructHelper {
	
	/**
	 * 
	 * @param op
	 * @param reserveOutputSchema: If true the inputschema of all father nodes is set to the output
	 * schema of the replaced node. E.g. used for deletion of rename-Operation
	 * @return
	 */
	public static Collection<ILogicalOperator> removeOperator(UnaryLogicalOp op, boolean reserveOutputSchema){
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		Collection<LogicalSubscription> fathers = op.getSubscribtions();
		LogicalSubscription child = op.getSubscribedTo(0);
		// remove Connection between child and op
		op.unsubscribeTo(child);
		// Subscribe Child to every father of op
		for (LogicalSubscription father:fathers){
			op.unsubscribe(father);
			child.getTarget().subscribe(father.getTarget(), father.getSinkPort(), child.getSourcePort());
			ret.add(child.getTarget());
		}
		ret.add(child.getTarget());
		
		
		return ret;
	}

	/**	Insert an operator in the tree at some special point and update all subscriptions
	 * i.e. the new Operator gets all subscriptions currently bound to the after operator
	 * and create a new subscription from toInsert to after
	 * 
	 * @param toInsert Operator that should be inserted as child of the after operator 
	 * @param after
	 * @return
	 */
	public static Collection<ILogicalOperator> insertOperator(ILogicalOperator toInsert, ILogicalOperator after, int sinkPort){
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		Collection<LogicalSubscription> sinks = after.getSubscribtions();
		LogicalSubscription source = after.getSubscribedTo(sinkPort);
		for (LogicalSubscription s: sinks){
			ret.add(s.getTarget());
			after.unsubscribeSubscriptionTo(s.getTarget(), s.getSinkPort(), s.getSourcePort());
			toInsert.subscribeTo(s.getTarget(), s.getSinkPort(), s.getSourcePort());
			toInsert.setInputSchema(sinkPort, s.getInputSchema());
		}
		after.subscribeTo(toInsert, sinkPort, source.getSourcePort());
		after.setInputSchema(sinkPort, toInsert.getOutputSchema());
		return ret;
	}
	
	/**
	 * Switches two unary operators in a Plan (i.e. no new Operator need to be defined)
	 * Cannot be used if one of the operators is binary!
	 * @param a
	 * @param b
	 * @return
	 */
	
	public static Collection<ILogicalOperator> switchOperator(UnaryLogicalOp a, UnaryLogicalOp b){
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		Collection<LogicalSubscription> subs = b.getSubscribtions(a);
		ret.addAll(removeOperator(a, true));
		for (LogicalSubscription l:subs){
			ret.addAll(insertOperator(a, b, l.getSinkPort()));
		}
		return ret;
	}
	
	public static Collection<ILogicalOperator> removeRename(RenameAO op){
		return removeOperator(op, true);
	}

	public static Collection<ILogicalOperator> removeOperator(UnaryLogicalOp op){
		return removeOperator(op, false);
	}

	public static boolean subsetPredicate(
			IPredicate<RelationalTuple<?>> predicate,
			ILogicalOperator op) {
		for(LogicalSubscription l:op.getSubscribedTo()){
			if (!subsetPredicate(predicate, l.getTarget().getOutputSchema() )){
				return false;
			}	
		}
		return true;
	}
	
	public static boolean subsetPredicate(
			IPredicate<RelationalTuple<?>> predicate,
			SDFAttributeList attributes) {
		
		final List<String> uris = new ArrayList<String>(attributes.getAttributeCount());
		for(SDFAttribute curAttr : attributes) {
			uris.add(curAttr.getURI());
		}
		final boolean[] retValue = new boolean[]{true};
		RestructHelper.visitPredicates(predicate, new RestructHelper.IUnaryFunctor<IPredicate<?>>() {
			public void call(IPredicate<?> predicate) {
				if (predicate instanceof IRelationalPredicate) {
					IRelationalPredicate relPred = (IRelationalPredicate)predicate;
					List<SDFAttribute> tmpAttrs = relPred.getAttributes();
					List<String> tmpUris = new ArrayList<String>(tmpAttrs.size());
					for(SDFAttribute curAttr : tmpAttrs) {
						tmpUris.add(curAttr.getURI());
					}
					if (!uris.containsAll(tmpUris)) {
						retValue[0] = false;
					}
				}
			}
		});
		return retValue[0];
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
			} else {
				functor.call(curPred);
			}
		}
	}

}
