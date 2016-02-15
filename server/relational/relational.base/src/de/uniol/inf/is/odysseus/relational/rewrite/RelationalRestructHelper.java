/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.IRelationalExpression;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.DifferenceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.server.predicate.IUnaryFunctor;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.relational.rewrite.rules.RestructParameterInfoUtil;

/**
 * This class provides functions to support Restructuring Aspects
 * 
 * @author Marco Grawunder
 * 
 */
public class RelationalRestructHelper {

	private static final Logger LOG = LoggerFactory.getLogger(RelationalRestructHelper.class);

	public static boolean containsAllSources(ILogicalOperator op, Set<?> sources) {
		Collection<SDFAttribute> schema = op.getOutputSchema().getAttributes();
		Set<?> schemaSources = sourcesOfAttributes(schema);
		return schemaSources.containsAll(sources);
	}

	public static Set<String> sourcesOfPredicate(IPredicate<?> predicate) {
		final Set<String> sources = new HashSet<>();
		ComplexPredicateHelper.visitPredicates(predicate, new IUnaryFunctor<IPredicate<?>>() {
			@Override
			public void call(IPredicate<?> pred) {
				if (pred instanceof IRelationalPredicate){
					sources.addAll(sourcesOfAttributes(((IRelationalPredicate<?>) pred).getAttributes()));
				}else if (pred instanceof IRelationalExpression){
					sources.addAll(sourcesOfAttributes(((RelationalExpression<?>) pred).getAttributes()));
				}
			}
		});
		return sources;
	}

	public static Set<String> sourcesOfAttributes(Collection<?> attributes) {
		HashSet<String> sources = new 	HashSet<>();
		for (Object attribute : attributes) {
			sources.add(((SDFAttribute) attribute).getSourceName());
		}
		return sources;
	}

	public static boolean subsetPredicate(IPredicate<Tuple<?>> predicate, ILogicalOperator op) {
		for (int inPort = 0; inPort < op.getNumberOfInputs(); inPort++) {
			if (!subsetPredicate(predicate, op.getInputSchema(inPort))) {
				return false;
			}
		}
		return true;
	}

	private static Collection<String> attributesToUris(Collection<SDFAttribute> attributes) {
		List<String> uris = Lists.newArrayList();
		for (SDFAttribute curAttr : attributes) {
			uris.add(curAttr.getURI());
		}
		return uris;
	}

	private static Collection<String> metadataAttributesToUris(SDFMetaSchema metaSchema) {
		List<String> uris = Lists.newArrayList();
		for (SDFAttribute curAttr : metaSchema.getAttributes()) {
			uris.add(curAttr.getURI());
		}
		return uris;
	}

	private static Collection<String> metadataAttributesToUris(Collection<SDFMetaSchema> metaSchemata) {
		List<String> uris = Lists.newArrayList();
		for (SDFMetaSchema curSchema : metaSchemata) {
			uris.addAll(metadataAttributesToUris(curSchema));
		}
		return uris;
	}

	public static boolean subsetPredicate(IPredicate<?> predicate, SDFSchema attributes) {

		final Collection<String> uris = attributesToUris(attributes.getAttributes());
		uris.addAll(metadataAttributesToUris(attributes.getMetaschema()));
		final boolean[] retValue = new boolean[] { true };
		ComplexPredicateHelper.visitPredicates(predicate, new IUnaryFunctor<IPredicate<?>>() {
			@Override
			public void call(IPredicate<?> predicate) {
				if (predicate instanceof IRelationalPredicate) {
					IRelationalPredicate<?> relPred = (IRelationalPredicate<?>) predicate;
					Collection<SDFAttribute> tmpAttrs = relPred.getAttributes();
					Collection<String> tmpUris = attributesToUris(tmpAttrs);
					if (!uris.containsAll(tmpUris)) {
						retValue[0] = false;
					}
				} else if (predicate instanceof IRelationalExpression) {
					RelationalExpression<?> relPred = (RelationalExpression<?>) predicate;
					Collection<SDFAttribute> tmpAttrs = relPred.getAttributes();
					Collection<String> tmpUris = attributesToUris(tmpAttrs);
					if (!uris.containsAll(tmpUris)) {
						retValue[0] = false;
					}

				}
			}
		});
		return retValue[0];
	}

	public static Collection<ILogicalOperator> switchOperator(SelectAO father, AbstractWindowAO son) {
		return RestructHelper.simpleOperatorSwitch(father, son);
	}

	public static Collection<ILogicalOperator> switchOperator(final ProjectAO father, final RenameAO son) {
		// Change the father
		List<SDFAttribute> oldAttributes_proj = father.getAttributes();
		List<SDFAttribute> newAttributes_proj = new ArrayList<>(oldAttributes_proj.size());
		for (SDFAttribute attr : oldAttributes_proj) {
			int index = son.getOutputSchema().indexOf(attr);
			SDFAttribute newAttr = son.getInputSchema().get(index);
			newAttributes_proj.add(newAttr);
		}
		father.setOutputSchemaWithList(newAttributes_proj);
		SDFSchema newOutputSchema_proj = SDFSchemaFactory.createNewWithAttributes(newAttributes_proj,
				father.getOutputSchema());
		father.setOutputSchema(newOutputSchema_proj);
		RestructParameterInfoUtil.updateAttributesParameterInfo(father.getParameterInfos(), newAttributes_proj);

		// Change the son
		List<String> oldAliases = son.getAliases();
		List<String> newAliases = new ArrayList<>(oldAliases.size());
		List<SDFAttribute> newOutputAttributes = new ArrayList<>();
		for (String alias : oldAliases) {
			SDFAttribute correspondingAttr = null;
			for (SDFAttribute attr : oldAttributes_proj) {
				if (attr.getAttributeName().equals(alias)) {
					correspondingAttr = attr;
					break;
				}
			}
			if (correspondingAttr != null) {
				newAliases.add(alias);
				newOutputAttributes.add(correspondingAttr);
			}
		}
		son.setAliases(newAliases);
		son.setOutputSchema(SDFSchemaFactory.createNewWithAttributes(newOutputAttributes, son.getOutputSchema()));
		RestructParameterInfoUtil.updateAliasesParameterInfo(son.getParameterInfos(), newAliases);

		Collection<ILogicalOperator> ret = RestructHelper.simpleOperatorSwitch(father, son);
		return ret;
	}

	// public static Collection<ILogicalOperator> switchOperator(ProjectAO
	// father, RenameAO son) {
	//
	// SDFSchema inputSchema = son.getInputSchema();
	// SDFSchema renameOutputSchema = son.getOutputSchema();
	// SDFSchema oldOutputSchema = father.getOutputSchema();
	// LogicalSubscription toDown = son.getSubscribedToSource(0);
	// // TODO: Can there be more than one father??
	// LogicalSubscription toUp = father.getSubscriptions().iterator().next();
	//
	// son.unsubscribeFromSource(toDown);
	// father.unsubscribeFromSource(son, 0, 0, son.getOutputSchema());
	// father.unsubscribeSink(toUp);
	//
	// father.subscribeTo(toDown.getTarget(), toDown.getSchema());
	//
	// // change attribute names for projection
	// List<SDFAttribute> newOutputAttributes = new ArrayList<SDFAttribute>();
	// for (SDFAttribute a : oldOutputSchema) {
	// int pos = son.getOutputSchema().indexOf(a);
	// newOutputAttributes.add(inputSchema.get(pos));
	// }
	// SDFSchema newOutputSchema = new SDFSchema(oldOutputSchema,
	// newOutputAttributes);
	// father.setOutputSchema(newOutputSchema);
	//
	// father.subscribeSink(son, 0, 0, father.getOutputSchema());
	//
	// // remove attributes from rename operator that get projected away
	// Iterator<SDFAttribute> inIt = inputSchema.iterator();
	// Iterator<SDFAttribute> outIt = renameOutputSchema.iterator();
	// List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
	// while (inIt.hasNext()) {
	// SDFAttribute nextIn = inIt.next();
	// SDFAttribute nextOut = outIt.next();
	// if (newOutputSchema.contains(nextIn)) {
	// attrs.add(nextOut);
	// }
	// }
	// SDFSchema newRenameSchema = new SDFSchema(inputSchema.getURI(),
	// inputSchema.getType(), attrs);
	// son.setOutputSchema(newRenameSchema);
	//
	// son.subscribeSink(toUp.getTarget(), toUp.getSinkInPort(), 0,
	// son.getOutputSchema());
	//
	// Collection<ILogicalOperator> toUpdate = new
	// ArrayList<ILogicalOperator>(2);
	// toUpdate.add(toDown.getTarget());
	// toUpdate.add(toUp.getTarget());
	// return toUpdate;
	// }

	public static Collection<ILogicalOperator> switchOperator(ProjectAO father, AbstractWindowAO son) {
		return RestructHelper.simpleOperatorSwitch(father, son);
	}

	public static Collection<ILogicalOperator> switchOperator(SelectAO father, ProjectAO son) {
		return RestructHelper.simpleOperatorSwitch(father, son);
	}

	private static Collection<ILogicalOperator> switchOperatorInternal(SelectAO father, AbstractLogicalOperator son,
			Collection<ILogicalOperator> toInsert) {
		SelectAO[] toAdd = new SelectAO[son.getNumberOfInputs()];
		toAdd[0] = father;

		for (int i = 1; i < son.getNumberOfInputs(); i++) {
			toAdd[i] = new SelectAO(father.getPredicate().clone());
			toInsert.add(toAdd[i]);
		}

		Collection<ILogicalOperator> ret = removeOperator(father);
		ret.add(toAdd[0]);
		// TODO: check if 0,0 as last param is ok should be retrieved from
		// father subscription?
		for (int i = 0; i < son.getNumberOfInputs(); i++) {
			ret.addAll(RestructHelper.insertOperator(toAdd[i], son, i, 0, 0));
		}
		return ret;
	}

	private static Collection<ILogicalOperator> switchOperatorInternal(ProjectAO father, AbstractLogicalOperator son,
			Collection<ILogicalOperator> toInsert) {
		ProjectAO[] toAdd = new ProjectAO[son.getNumberOfInputs()];
		toAdd[0] = father;
		for (int i = 1; i < son.getNumberOfInputs(); i++) {
			toAdd[i] = new ProjectAO();
			toAdd[i].setOutputSchemaWithList(father.getOutputSchemaWithList());
			toInsert.add(toAdd[i]);
		}

		Collection<ILogicalOperator> ret = removeOperator(father);
		ret.add(toAdd[0]);

		// TODO: check if 0,0 as last param is ok should be retrieved from
		// father subscription?
		for (int i = 0; i < son.getNumberOfInputs(); i++) {
			ret.addAll(RestructHelper.insertOperator(toAdd[i], son, i, 0, 0));
		}
		return ret;
	}

	public static Collection<ILogicalOperator> switchOperator(SelectAO father, UnionAO son,
			Collection<ILogicalOperator> toInsert) {
		return switchOperatorInternal(father, son, toInsert);
	}

	public static Collection<ILogicalOperator> switchOperator(ProjectAO father, UnionAO son,
			Collection<ILogicalOperator> toInsert) {
		return switchOperatorInternal(father, son, toInsert);
	}

	// invalid. simple switch may reduce the difference set
	// public static Collection<ILogicalOperator> switchOperator(SelectAO
	// father, DifferenceAO son, Collection<ILogicalOperator> toInsert) {
	// return switchOperatorInternal(father, son, toInsert);
	// }

	public static Collection<ILogicalOperator> switchOperator(SelectAO father, JoinAO son,
			Collection<ILogicalOperator> toInsert, Collection<ILogicalOperator> toRemove) {
		final JoinAO join = son;
		final SelectAO sel = father;
		toRemove.add(sel);
		Collection<ILogicalOperator> ret = removeOperator(sel);

		// hasSameInput does not work, because it is forbidden for joins to have
		// same inputs.
		// boolean hasSameInput = (join.getLeftInput() == join.getRightInput());
		SelectAO newSel = createSelection(sel, join, 0, ret);
		if (newSel != null) {
			toInsert.add(newSel);
		}
		// if the join is a self join and has the same input operator on both
		// sides,
		// we don't want to create two separate selections as new inputs, but
		// set
		// one selection as input on both ports
		// if (hasSameInput) {
		// ret.addAll(RestructHelper.insertOperator(newSel, join, 1, 0, 0));
		// } else {
		// newSel = createSelection(sel, join, 1, ret);
		// if (newSel != null) {
		// toInsert.add(newSel);
		// }
		// }
		newSel = createSelection(sel, join, 1, ret);
		if (newSel != null) {
			toInsert.add(newSel);
		}
		return ret;
	}

	static private SelectAO createSelection(SelectAO select, JoinAO join, int port, Collection<ILogicalOperator> ret) {
		if (!subsetPredicate(select.getPredicate(), join.getInputSchema(port))) {
			return null;
		}
		SelectAO newSel = new SelectAO();
		newSel.setPredicate(select.getPredicate());
		ret.addAll(RestructHelper.insertOperator(newSel, join, port, 0, 0));
		return newSel;
	}

	public static Collection<ILogicalOperator> switchOperator(SelectAO father, DifferenceAO son,
			Collection<ILogicalOperator> toInsert, Collection<ILogicalOperator> toRemove) {
		final DifferenceAO difference = son;
		final SelectAO sel = father;
		toRemove.add(sel);
		Collection<ILogicalOperator> ret = removeOperator(sel);

		SelectAO newSel = createSelection(sel, difference, 0, ret);
		if (newSel != null) {
			toInsert.add(newSel);
		}

		newSel = createSelection(sel, difference, 1, ret);
		if (newSel != null) {
			toInsert.add(newSel);
		}
		return ret;
	}

	static private SelectAO createSelection(SelectAO select, DifferenceAO difference, int port,
			Collection<ILogicalOperator> ret) {
		if (!subsetPredicate(select.getPredicate(), difference.getInputSchema(port))) {
			return null;
		}
		SelectAO newSel = new SelectAO();
		newSel.setPredicate(select.getPredicate());
		ret.addAll(RestructHelper.insertOperator(newSel, difference, port, 0, 0));
		return newSel;
	}

	public static Collection<ILogicalOperator> switchOperator(final SelectAO father, final RenameAO son) {
		Collection<ILogicalOperator> ret = RestructHelper.simpleOperatorSwitch(father, son);
		ComplexPredicateHelper.visitPredicates(father.getPredicate(), new IUnaryFunctor<IPredicate<?>>() {
			@Override
			public void call(IPredicate<?> curPred) {
				List<SDFAttribute> attributes = ((IPredicate<?>) curPred).getAttributes();

				for (SDFAttribute curAttr : attributes) {
					int index = son.getOutputSchema().indexOf(curAttr);
					SDFAttribute newAttr = son.getInputSchema().get(index);
					if (!curAttr.equals(newAttr)) {
						((RelationalExpression<?>) curPred).replaceAttribute(curAttr, newAttr);
					}
				}
			}
		});
		return ret;
	}

	// public static Collection<ILogicalOperator> removeOperator(RenameAO op) {
	// LoggerHelper.getInstance(LOGGER_NAME).debug("removing rename:" + op);
	// return RestructHelper.removeOperator(op, true);
	// }

	public static Collection<ILogicalOperator> removeOperator(UnaryLogicalOp op) {
		LOG.debug("removing operator:" + op);
		return RestructHelper.removeOperator(op, false);
	}

}
