/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.relational.rewrite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.DifferenceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.server.predicate.IUnaryFunctor;
import de.uniol.inf.is.odysseus.core.server.util.LoggerHelper;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;

/**
 * This class provides functions to support Restructuring Aspects
 * 
 * @author Marco Grawunder
 * 
 */
public class RelationalRestructHelper {
	
	static String LOGGER_NAME = RelationalRestructHelper.class.toString();

	public static boolean containsAllSources(ILogicalOperator op, Set<?> sources) {
		Collection<SDFAttribute> schema = op.getOutputSchema().getAttributes();
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
		ComplexPredicateHelper.visitPredicates((IPredicate<?>) predicate,
				new IUnaryFunctor<IPredicate<?>>() {
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

	public static Set<?> sourcesOfAttributes(Collection<?> attributes) {
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
			IPredicate<?> predicate,
			SDFSchema attributes) {

		final List<String> uris = new ArrayList<String>(attributes
				.size());
		for (SDFAttribute curAttr : attributes) {
			uris.add(curAttr.getURI());
		}
		final boolean[] retValue = new boolean[] { true };
		ComplexPredicateHelper.visitPredicates(predicate,
				new IUnaryFunctor<IPredicate<?>>() {
					@Override
					public void call(IPredicate<?> predicate) {
						if (predicate instanceof IRelationalPredicate) {
							IRelationalPredicate relPred = (IRelationalPredicate) predicate;
							List<SDFAttribute> tmpAttrs = relPred
									.getAttributes();
							List<String> tmpUris = new ArrayList<String>(
									tmpAttrs.size());
							for (SDFAttribute curAttr : tmpAttrs) {
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
	
	public static Collection<ILogicalOperator> switchOperator(SelectAO father,
			WindowAO son) {
		return RestructHelper.simpleOperatorSwitch(father, son);
	}

	public static Collection<ILogicalOperator> switchOperator(ProjectAO father,
			RenameAO son) {
		SDFSchema inputSchema = son.getInputSchema();
		SDFSchema renameOutputSchema = son.getOutputSchema();
		SDFSchema oldOutputSchema = father.getOutputSchema();
		LogicalSubscription toDown = son.getSubscribedToSource(0);
		LogicalSubscription toUp = father.getSubscription();

		son.unsubscribeFromSource(toDown);
		father.unsubscribeFromSource(son, 0, 0, son.getOutputSchema());
		father.unsubscribeSink(toUp);

		father.subscribeTo(toDown.getTarget(), toDown.getSchema());

		// change attribute names for projection
		List<SDFAttribute> newOutputAttributes = new ArrayList<SDFAttribute>();
		for (SDFAttribute a : oldOutputSchema) {
			int pos = son.getOutputSchema().indexOf(a);
			newOutputAttributes.add(inputSchema.get(pos));
		}
		SDFSchema newOutputSchema = new SDFSchema(oldOutputSchema.getURI(), newOutputAttributes);
		father.setOutputSchema(newOutputSchema);

		father.subscribeSink(son, 0, 0, father.getOutputSchema());

		// remove attributes from rename operator that get projected away
		Iterator<SDFAttribute> inIt = inputSchema.iterator();
		Iterator<SDFAttribute> outIt = renameOutputSchema.iterator();
		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		while (inIt.hasNext()) {
			SDFAttribute nextIn = inIt.next();
			SDFAttribute nextOut = outIt.next();
			if (newOutputSchema.contains(nextIn)) {
				attrs.add(nextOut);
			}
		}
		SDFSchema newRenameSchema = new SDFSchema(inputSchema.getURI(), attrs);
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
		ComplexPredicateHelper.visitPredicates(select.getPredicate(),
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
