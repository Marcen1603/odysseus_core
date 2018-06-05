/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.expression.IRelationalExpression;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.mep.IBinaryOperator;
import de.uniol.inf.is.odysseus.mep.IStatefulFunction;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

/**
 * helper class for working with sdf attributes
 * 
 * @author ChrisToenjesDeye
 *
 */
public class SDFAttributeHelper {

	final private static InfoService INFO_SERVICE = InfoServiceFactory
			.getInfoService(SDFAttributeHelper.class);

	private boolean fragmentationIsPossible = true;
	private static SDFAttributeHelper instance;

	/**
	 * returns a single instance
	 * 
	 * @return
	 */
	public static SDFAttributeHelper getInstance() {
		if (instance == null) {
			instance = new SDFAttributeHelper();
		}
		return instance;
	}

	/**
	 * validates of the structure of a given join attribute is in a correct
	 * structure valid structure: (a.A == b.B) && (a.C == b.D) && ... in
	 * addition to this the number of attributes need to be equals for both
	 * input ports
	 * 
	 * @param a
	 *            logical join operator
	 * @return true if the structure is valid, else false
	 */
	public boolean validateStructureOfPredicate(JoinAO joinOperator) {
		IPredicate<?> predicate = joinOperator.getPredicate();
		if (predicate instanceof IRelationalExpression) {
			RelationalExpression<?> relPredicate = (RelationalExpression<?>) predicate;
			IMepExpression<?> expression = relPredicate
					.getMEPExpression();
			IAttributeResolver resolver = relPredicate
					.getAttributeResolver();
			Map<Integer, List<SDFAttribute>> attributes = new HashMap<Integer, List<SDFAttribute>>();
			attributes = getSDFAttributesFromEqualPredicates(attributes,
					expression, resolver, joinOperator);
			// if fragmentation is not possible, remove all collected attributes
			if (!fragmentationIsPossible) {
				attributes.clear();
			} else {
				// if fragmentation is possible, we need to remove
				// duplicate attributes for each input stream
				for (Integer inputPort : attributes.keySet()) {
					List<SDFAttribute> arrayList = attributes.get(inputPort);
					HashSet<SDFAttribute> hashSet = new HashSet<SDFAttribute>(
							arrayList);
					arrayList.clear();
					arrayList.addAll(hashSet);
				}
			}

			fragmentationIsPossible = true;
			// check if at least two attributes exists, one for each input
			// stream
			if (attributes.keySet().size() >= 2) {
				return true;
			}
		}
		return false;
	}

	/**
	 * gets the sdf attributes from a given join operator. Only returns results
	 * if the structure is valid valid structure: (a.A == b.B) && (a.C == b.D)
	 * && ... in addition to this the number of attributes need to be equals for
	 * both input ports
	 * 
	 * @param resultin
	 *            attributes
	 * @param a
	 *            logical join operator
	 * @return a map of attributes for both input ports
	 */
	public Map<Integer, List<SDFAttribute>> getSDFAttributesFromEqualPredicates(
			Map<Integer, List<SDFAttribute>> attributes, JoinAO joinOperator) {
		IPredicate<?> predicate = joinOperator.getPredicate();
		if (predicate instanceof IRelationalExpression) {
			RelationalExpression<?> relPredicate = (RelationalExpression<?>) predicate;
			IMepExpression<?> expression = relPredicate
					.getMEPExpression();
			IAttributeResolver resolver = relPredicate
					.getAttributeResolver();
			attributes = getSDFAttributesFromEqualPredicates(attributes,
					expression, resolver, joinOperator);
			if (!fragmentationIsPossible) {
				attributes.clear();
			} else {
				// if fragmentation is possible, we need to remove
				// duplicate attributes for each input stream
				for (Integer inputPort : attributes.keySet()) {
					List<SDFAttribute> arrayList = attributes.get(inputPort);
					HashSet<SDFAttribute> hashSet = new HashSet<SDFAttribute>(
							arrayList);
					arrayList.clear();
					arrayList.addAll(hashSet);
				}
			}
		}
		fragmentationIsPossible = true;
		return attributes;

	}

	/**
	 * Gets the attributes from expression 
	 * 
	 * @param attributes
	 * @param expression
	 * @param resolver
	 * @param joinOperator
	 * @return attributes grouped by input port
	 */
	private Map<Integer, List<SDFAttribute>> getSDFAttributesFromEqualPredicates(
			Map<Integer, List<SDFAttribute>> attributes,
			IMepExpression<?> expression, IAttributeResolver resolver,
			JoinAO joinOperator) {
		String symbol = expression.toFunction().getSymbol();

		// check if expression is function and is AND
		if (expression.isFunction() && (symbol.equalsIgnoreCase("&&"))) {

			IBinaryOperator<?> binaryOperator = (IBinaryOperator<?>) expression;
			IMepExpression<?> argument1 = binaryOperator.getArgument(0);
			if (argument1.isFunction()) {
				// recursive call for left part of AND
				getSDFAttributesFromEqualPredicates(attributes, argument1,
						resolver, joinOperator);
			}
			IMepExpression<?> argument2 = binaryOperator.getArgument(1);
			if (argument2.isFunction()) {
				// recursive call for right part of AND
				getSDFAttributesFromEqualPredicates(attributes, argument2,
						resolver, joinOperator);
			}
		} else if ((expression.isFunction()) && (symbol.equalsIgnoreCase("="))) {
			// if we have an EQUALS function
			final IBinaryOperator<?> eq = (IBinaryOperator<?>) expression;
			final IMepExpression<?> arg1 = eq.getArgument(0);
			final IMepExpression<?> arg2 = eq.getArgument(1);
			if ((arg1.isVariable()) && (arg2.isVariable())) {
				// Resolve first attribute and get input port
				SDFAttribute attr1 = resolver.getAttribute(((IMepVariable) arg1)
						.getIdentifier());

				int attribute1port = -1;
				if (joinOperator.getInputSchema(0)
						.findAttribute(attr1.getURI()) != null) {
					attribute1port = 0;
				} else if (joinOperator.getInputSchema(1).findAttribute(
						attr1.getURI()) != null) {
					attribute1port = 1;
				}

				// Resolve second attribute and get input port
				SDFAttribute attr2 = resolver.getAttribute(((IMepVariable) arg2)
						.getIdentifier());

				int attribute2port = -1;
				if (joinOperator.getInputSchema(0)
						.findAttribute(attr2.getURI()) != null) {
					attribute2port = 0;
				} else if (joinOperator.getInputSchema(1).findAttribute(
						attr2.getURI()) != null) {
					attribute2port = 1;
				}

				// save attributes in combination with input port only if
				// attributes belong to both input ports
				if (attribute1port > -1 && attribute2port > -1) {
					if (!attributes.containsKey(attribute1port)) {
						attributes.put(attribute1port,
								new ArrayList<SDFAttribute>());
					}
					attributes.get(attribute1port).add(attr1);

					if (!attributes.containsKey(attribute2port)) {
						attributes.put(attribute2port,
								new ArrayList<SDFAttribute>());
					}
					attributes.get(attribute2port).add(attr2);
				}
			}
		} else {
			// only EQUALS and AND functions are allowed
			fragmentationIsPossible = false;
		}

		return attributes;
	}

	/**
	 * checks if a given mep expression contains stateful expressions
	 * 
	 * @param mepExpression
	 * @return true if the given expression contains stateful expressions
	 */
	public static boolean expressionContainsStatefulFunction(
			IMepExpression<?> mepExpression) {
		if (mepExpression.isFunction()) {
			if (mepExpression instanceof IStatefulFunction) {
				return true;
			} else {
				final IBinaryOperator<?> binaryOperator = (IBinaryOperator<?>) mepExpression;
				if (expressionContainsStatefulFunction(binaryOperator
						.getArgument(0))
						|| expressionContainsStatefulFunction(binaryOperator
								.getArgument(0))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * checks if the grouping attributes contains in one of the given
	 * fragmentation operators (only hash fragments)
	 * 
	 * @param aggregateAO
	 * @param iteration
	 * @param fragmentAOs
	 * @param assureSemanticCorrectness
	 */
	public static void checkIfAttributesAreEqual(AggregateAO aggregateOperator,
			int iteration, List<AbstractStaticFragmentAO> fragmentAOs,
			boolean assureSemanticCorrectness) {

		List<SDFAttribute> groupingAttributes = aggregateOperator
				.getGroupingAttributes();

		for (SDFAttribute sdfAttribute : groupingAttributes) {
			boolean attributeFound = false;
			for (AbstractFragmentAO fragment : fragmentAOs) {
				if (fragment instanceof HashFragmentAO) {
					HashFragmentAO hashFragment = (HashFragmentAO) fragment;
					if (hashFragment.getAttributes().contains(sdfAttribute)) {
						attributeFound = true;
					}
				}
			}
			if (!attributeFound) {
				if (assureSemanticCorrectness) {
					throw new IllegalArgumentException(
							"Hash Fragment does not contain grouping attribute of aggregate operator. "
									+ "Semantic changes for parallelization are not allowed.");
				} else {
					if (iteration == 0) {
						INFO_SERVICE
								.info("Hash Fragment does not contain grouping attribute of aggregate operator. "
										+ "Semantic change is possible");
					}
				}
			}
		}
	}

}
