package de.uniol.inf.is.odysseus.rewrite.relational;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.RenameAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
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

public class RelationalRestructHelper {
	
	/**
	 * 
	 * @param remove
	 * @param reserveOutputSchema: If true the inputschema of all father nodes is set to the output
	 * schema of the replaced node. E.g. used for deletion of rename-Operation
	 * @return
	 */


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
		RelationalRestructHelper.visitPredicates(predicate, new RelationalRestructHelper.IUnaryFunctor<IPredicate<?>>() {
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
