package de.uniol.inf.is.odysseus.rewrite.relational;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.base.DifferenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.RenameAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnionAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.rewrite.drools.RestructHelper;
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

	public static Collection<ILogicalOperator> switchOperator(SelectAO father, WindowAO son){
		return RestructHelper.switchOperatorInternal(father,son);
	}
	
	public static Collection<ILogicalOperator> switchOperator(ProjectAO father, RenameAO son){
		return RestructHelper.switchOperatorInternal(father, son);
	}
	
	public static Collection<ILogicalOperator> switchOperator(ProjectAO father, WindowAO son){
		return RestructHelper.switchOperatorInternal(father, son);
	}
	
	public static Collection<ILogicalOperator> switchOperator(SelectAO father, ProjectAO son){
		return RestructHelper.switchOperatorInternal(father, son);
	}
	
	private static Collection<ILogicalOperator> switchOperatorInternal(SelectAO father, BinaryLogicalOp son, Collection<ILogicalOperator> toInsert){
		SelectAO selLeft = father;
		SelectAO selRight = new SelectAO(father.getPredicate());
		toInsert.add(selRight);
		
		Collection<ILogicalOperator> ret = removeOperator(father);
		ret.add(selLeft);
		
		ret.addAll(RestructHelper.insertOperator(selLeft, son, 0));
		ret.addAll(RestructHelper.insertOperator(selRight, son, 1));
		return ret;		
	}
	
	public static Collection<ILogicalOperator> switchOperator(SelectAO father, UnionAO son, Collection<ILogicalOperator> toInsert){	
		return switchOperatorInternal(father, son, toInsert);
	}	

	public static Collection<ILogicalOperator> switchOperator(SelectAO father, DifferenceAO son, Collection<ILogicalOperator> toInsert){	
		return switchOperatorInternal(father, son, toInsert);
	}	

	public static Collection<ILogicalOperator> switchOperator(SelectAO father, JoinAO son, Collection<ILogicalOperator> toInsert){
		final JoinAO join = son;
		final SelectAO sel = father;
		
		Collection<ILogicalOperator> ret = removeOperator(sel);
	
		boolean hasSameInput = (join.getLeftInput() == join.getRightInput());
		SelectAO newSel = createSelection(sel, join, 0, ret);
		if (newSel != null) {
			toInsert.add(newSel);
		}
		//if the join is a self join and has the same input operator on both sides,
		//we don't want to create two separate selections as new inputs, but set
		//one selection as input on both ports
		if (hasSameInput) {
			ret.addAll(RestructHelper.insertOperator(newSel, join, 1));
		} else {
			newSel = createSelection(sel, join, 1, ret);
			if (newSel != null) {
				toInsert.add(newSel);
			}
		}			
		return ret;
	}
	
	static private SelectAO createSelection(SelectAO select, JoinAO join, int port, Collection<ILogicalOperator> ret) {
		if (!subsetPredicate(select.getPredicate(), join.getInputSchema(port))) {
			return null;
		}
		SelectAO newSel = new SelectAO();
		newSel.setPredicate(select.getPredicate());
		ret.addAll(RestructHelper.insertOperator(newSel, join, port));
		return newSel;
	}


	
	public static Collection<ILogicalOperator> switchOperator(SelectAO father, RenameAO son){
		final RenameAO ren = son;
		final SelectAO select = father; 
		Collection<ILogicalOperator> ret = RestructHelper.switchOperatorInternal(father, son);
		visitPredicates(select.getPredicate(), new IUnaryFunctor<IPredicate<?>>() {
			public void call(IPredicate<?> curPred){
				List<SDFAttribute> attributes = ((IRelationalPredicate)curPred).getAttributes();
				List<SDFAttribute> tmp = new ArrayList<SDFAttribute>(attributes);
				attributes.clear();
				for(SDFAttribute curAttr : tmp) {
					int index = ren.getOutputSchema().indexOf(curAttr);
					SDFAttribute newAttr = select.getInputSchema().get(index);
					attributes.add(newAttr);
				}
			}
		});
		return ret;
	}	
	
	public static Collection<ILogicalOperator> removeOperator(RenameAO op){
		return RestructHelper.removeOperator(op, true);
	}

	public static Collection<ILogicalOperator> removeOperator(UnaryLogicalOp op){
		return RestructHelper.removeOperator(op, false);
	}
	
	
}
