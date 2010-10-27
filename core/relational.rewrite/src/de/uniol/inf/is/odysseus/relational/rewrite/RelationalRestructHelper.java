package de.uniol.inf.is.odysseus.relational.rewrite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import de.uniol.inf.is.odysseus.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.DifferenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.util.LoggerHelper;

/**
 * This class provides functions to support Restructuring Aspects
 * 
 * @author Marco Grawunder
 * 
 */
@SuppressWarnings("unchecked")
public class RelationalRestructHelper {
	
	static String LOGGER_NAME = RelationalRestructHelper.class.toString();

	public static boolean containsAllSources(ILogicalOperator op, Set<?> sources) {
		List<SDFAttribute> schema = op.getOutputSchema();
		Set<?> schemaSources = sourcesOfAttributes(schema);
		for (Object source : sources) {
			if (!schemaSources.contains(source)) {
				return false;
			}
		}
		return true;
	}

	public static Set<?> sourcesOfPredicate(IPredicate<?> predicate) {
		final HashSet<String> sources = new HashSet<String>();
		visitPredicates((IPredicate<?>) predicate,
				new RelationalRestructHelper.IUnaryFunctor<IPredicate<?>>() {
					@Override
					public void call(IPredicate<?> pred) {
						List<SDFAttribute> attributes = ((IRelationalPredicate) pred)
								.getAttributes();
						// this should be a call to sourcesOfAttributes, but
						// there are strange compilation errors when
						// sourcesOfAttributes
						// get called from here
						for (SDFAttribute attribute : attributes) {
							sources.add(((SDFAttribute) attribute)
									.getSourceName());
						}
					}
				});
		return sources;
	}

	public static Set<?> sourcesOfAttributes(List<?> attributes) {
		HashSet<String> sources = new HashSet<String>();
		for (Object attribute : attributes) {
			sources.add(((SDFAttribute) attribute).getSourceName());
		}
		return sources;
	}

	public static boolean subsetPredicate(
			IPredicate<RelationalTuple<?>> predicate, ILogicalOperator op) {
		for (LogicalSubscription l : op.getSubscribedToSource()) {
			if (!subsetPredicate(predicate, l.getTarget().getOutputSchema())) {
				return false;
			}
		}
		return true;
	}

	public static boolean subsetPredicate(
			IPredicate<RelationalTuple<?>> predicate,
			SDFAttributeList attributes) {

		final List<String> uris = new ArrayList<String>(attributes
				.getAttributeCount());
		for (SDFAttribute curAttr : attributes) {
			uris.add(curAttr.getPointURI());
		}
		final boolean[] retValue = new boolean[] { true };
		RelationalRestructHelper.visitPredicates(predicate,
				new RelationalRestructHelper.IUnaryFunctor<IPredicate<?>>() {
					@Override
					public void call(IPredicate<?> predicate) {
						if (predicate instanceof IRelationalPredicate) {
							IRelationalPredicate relPred = (IRelationalPredicate) predicate;
							List<SDFAttribute> tmpAttrs = relPred
									.getAttributes();
							List<String> tmpUris = new ArrayList<String>(
									tmpAttrs.size());
							for (SDFAttribute curAttr : tmpAttrs) {
								tmpUris.add(curAttr.getPointURI());
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
			} else if(curPred instanceof NotPredicate){
				predicates.push(((NotPredicate<?>) curPred).getChild());
			}
			else {
				functor.call(curPred);
			}
		}
	}

	public static Collection<ILogicalOperator> switchOperator(SelectAO father,
			WindowAO son) {
		return RestructHelper.simpleOperatorSwitch(father, son);
	}

	public static Collection<ILogicalOperator> switchOperator(ProjectAO father,
			RenameAO son) {
		SDFAttributeList inputSchema = son.getInputSchema();
		SDFAttributeList renameOutputSchema = son.getOutputSchema();
		SDFAttributeList oldOutputSchema = father.getOutputSchema();
		LogicalSubscription toDown = son.getSubscribedToSource(0);
		LogicalSubscription toUp = father.getSubscription();

		son.unsubscribeFromSource(toDown);
		father.unsubscribeFromSource(son, 0, 0, son.getOutputSchema());
		father.unsubscribeSink(toUp);

		father.subscribeTo(toDown.getTarget(), toDown.getSchema());

		// change attribute names for projection
		SDFAttributeList newOutputSchema = new SDFAttributeList();
		for (SDFAttribute a : oldOutputSchema) {
			int pos = son.getOutputSchema().indexOf(a);
			newOutputSchema.add(inputSchema.get(pos));
		}
		father.setOutputSchema(newOutputSchema);

		father.subscribeSink(son, 0, 0, father.getOutputSchema());

		// remove attributes from rename operator that get projected away
		SDFAttributeList newRenameSchema = new SDFAttributeList();
		Iterator<SDFAttribute> inIt = inputSchema.iterator();
		Iterator<SDFAttribute> outIt = renameOutputSchema.iterator();
		while (inIt.hasNext()) {
			SDFAttribute nextIn = inIt.next();
			SDFAttribute nextOut = outIt.next();
			if (newOutputSchema.contains(nextIn)) {
				newRenameSchema.add(nextOut);
			}
		}
		son.setOutputSchema(newRenameSchema);

		son.subscribeSink(toUp.getTarget(), toUp.getSinkInPort(), 0, son
				.getOutputSchema());

		Collection<ILogicalOperator> toUpdate = new ArrayList<ILogicalOperator>(2);
		toUpdate.add(toDown.getTarget());
		toUpdate.add(toUp.getTarget());
		return toUpdate;
	}

	public static Collection<ILogicalOperator> switchOperator(ProjectAO father,
			WindowAO son) {
		return RestructHelper.simpleOperatorSwitch(father, son);
	}

	public static Collection<ILogicalOperator> switchOperator(SelectAO father,
			ProjectAO son) {
		return RestructHelper.simpleOperatorSwitch(father, son);
	}

	private static Collection<ILogicalOperator> switchOperatorInternal(
			SelectAO father, BinaryLogicalOp son,
			Collection<ILogicalOperator> toInsert) {
		SelectAO selLeft = father;
		SelectAO selRight = new SelectAO(father.getPredicate());
		toInsert.add(selRight);

		Collection<ILogicalOperator> ret = removeOperator(father);
		ret.add(selLeft);

		ret.addAll(RestructHelper.insertOperator(selLeft, son, 0, 0, 0));
		ret.addAll(RestructHelper.insertOperator(selRight, son, 1, 0, 0));
		return ret;
	}

	public static Collection<ILogicalOperator> switchOperator(SelectAO father,
			UnionAO son, Collection<ILogicalOperator> toInsert) {
		return switchOperatorInternal(father, son, toInsert);
	}

	public static Collection<ILogicalOperator> switchOperator(SelectAO father,
			DifferenceAO son, Collection<ILogicalOperator> toInsert) {
		return switchOperatorInternal(father, son, toInsert);
	}

	public static Collection<ILogicalOperator> switchOperator(SelectAO father,
			JoinAO son, Collection<ILogicalOperator> toInsert,
			Collection<ILogicalOperator> toRemove) {
		final JoinAO join = son;
		final SelectAO sel = father;
		toRemove.add(sel);
		Collection<ILogicalOperator> ret = removeOperator(sel);

		boolean hasSameInput = (join.getLeftInput() == join.getRightInput());
		SelectAO newSel = createSelection(sel, join, 0, ret);
		if (newSel != null) {
			toInsert.add(newSel);
		}
		// if the join is a self join and has the same input operator on both
		// sides,
		// we don't want to create two separate selections as new inputs, but
		// set
		// one selection as input on both ports
		if (hasSameInput) {
			ret.addAll(RestructHelper.insertOperator(newSel, join, 1, 0, 0));
		} else {
			newSel = createSelection(sel, join, 1, ret);
			if (newSel != null) {
				toInsert.add(newSel);
			}
		}
		return ret;
	}

	static private SelectAO createSelection(SelectAO select, JoinAO join,
			int port, Collection<ILogicalOperator> ret) {
		if (!subsetPredicate(select.getPredicate(), join.getInputSchema(port))) {
			return null;
		}
		SelectAO newSel = new SelectAO();
		newSel.setPredicate(select.getPredicate());
		ret.addAll(RestructHelper.insertOperator(newSel, join, port, 0, 0));
		return newSel;
	}

	public static Collection<ILogicalOperator> switchOperator(SelectAO father,
			RenameAO son) {
		final RenameAO ren = son;
		final SelectAO select = father;
		Collection<ILogicalOperator> ret = RestructHelper.simpleOperatorSwitch(father, son);
		visitPredicates(select.getPredicate(),
				new IUnaryFunctor<IPredicate<?>>() {
					@Override
					public void call(IPredicate<?> curPred) {
						List<SDFAttribute> attributes = ((IRelationalPredicate) curPred)
								.getAttributes();
						List<SDFAttribute> tmp = new ArrayList<SDFAttribute>(
								attributes);
						
						//attributes.clear();
						for (SDFAttribute curAttr : tmp) {
							int index = ren.getOutputSchema().indexOf(curAttr);
							SDFAttribute newAttr = select.getInputSchema().get(
									index);
							//attributes.add(newAttr);
							((IRelationalPredicate)curPred).replaceAttribute(curAttr, newAttr);
						}
					}
				});
		return ret;
	}

//	public static Collection<ILogicalOperator> removeOperator(RenameAO op) {
//		LoggerHelper.getInstance(LOGGER_NAME).debug("removing rename:" + op);
//		return RestructHelper.removeOperator(op, true);
//	}

	public static Collection<ILogicalOperator> removeOperator(UnaryLogicalOp op) {
		LoggerHelper.getInstance(LOGGER_NAME).debug("removing operator:" + op);
		return RestructHelper.removeOperator(op, false);
	}

}
