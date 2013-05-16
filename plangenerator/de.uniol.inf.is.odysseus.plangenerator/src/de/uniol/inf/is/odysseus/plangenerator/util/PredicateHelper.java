/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.plangenerator.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.TypeSafeRelationalPredicate;

/**
 * This class provides help to find and combine predicates. Also the
 * satisfaction of predicates is handled here.
 * 
 * @author Merlin Wasmann
 * 
 */
public class PredicateHelper {

	private static final Logger LOG = LoggerFactory
			.getLogger(PredicateHelper.class);

	private Map<AccessAO, Set<IRelationalPredicate>> unsatisfiedPredicates;

	private Map<ILogicalOperator, Set<IRelationalPredicate>> joinToSatisfiedPredicates;

	private IAttributeResolver resolver;

	public PredicateHelper() {
		this.unsatisfiedPredicates = new HashMap<AccessAO, Set<IRelationalPredicate>>();
		this.joinToSatisfiedPredicates = new HashMap<ILogicalOperator, Set<IRelationalPredicate>>();
	}

	public void initialize(Set<AccessAO> sources, Set<JoinAO> joins) {
		Map<AccessAO, Set<IRelationalPredicate>> predicateMap = collectPredicateMap(
				sources, joins);
		SDFSchema schema = null;
		for (AccessAO source : predicateMap.keySet()) {
			this.unsatisfiedPredicates.put(source,
					splitPredicates(predicateMap.get(source)));
			if (schema == null) {
				schema = source.getOutputSchema();
			} else {
				schema = SDFSchema.union(schema, source.getOutputSchema());
			}
		}
		this.resolver = new DirectAttributeResolver(schema);
	}

	/**
	 * Generates a new predicate based on a collection of AccessAOs, all related
	 * predicates and the related partial join plan.
	 * 
	 * @param partialJoinPlan
	 *            Join plan which should be joined with the
	 * @param sources
	 *            All sources contained in the plan.
	 * @param newSource
	 *            source which should be joined with the partialJoinPlan
	 * @param existingJoinPlan
	 *            existing joinplan which should be joined with the newSource
	 *            and the generatedPredicate.
	 * @return
	 */
	public Pair<IPredicate<?>, Set<IRelationalPredicate>> generatePredicate(
			Set<AccessAO> sources, AccessAO newSource,
			ILogicalOperator existingJoinPlan) {
		sources.add(newSource);
		if (sources.size() < 2) {
			LOG.debug("[PredicateHelper] generatePredicate(...) only one or no source given.");
		}

		Set<IRelationalPredicate> possibleUnsatisfiedPredicates = getUnsatisfiedPredicates(
				sources, existingJoinPlan);
		Set<IRelationalPredicate> satisfiablePredicates = getSatisfiablePredicates(
				possibleUnsatisfiedPredicates, sources);

		if (satisfiablePredicates.isEmpty()) {
			LOG.debug("[PredicateHelper] No satisfiable predicates found for this source pair.");
			return null;
		}

		Iterator<IRelationalPredicate> iterator = satisfiablePredicates
				.iterator();
		IPredicate<?> result = iterator.next();
		if (satisfiablePredicates.size() > 1) {
			while (iterator.hasNext()) {
				IRelationalPredicate next = iterator.next();
				result = (IPredicate<?>) ComplexPredicateHelper
						.createAndPredicate(result, next);
			}
		}
		LOG.debug("New predicate: {}", result);
		return new Pair<IPredicate<?>, Set<IRelationalPredicate>>(
				result, satisfiablePredicates);

		// Set<SDFExpression> expressions = new HashSet<SDFExpression>();
		// for (IRelationalPredicate predicate : satisfiablePredicates) {
		// expressions.add(((RelationalPredicate) predicate).getExpression());
		// }
		//
		// StringBuilder sb = new StringBuilder();
		// for (SDFExpression expression : expressions) {
		// sb.append(expression.getExpressionString());
		// sb.append(" AND ");
		// }
		// if (sb.length() > 5) {
		// sb.delete(sb.length() - 5, sb.length());
		// }
		//
		// SDFExpression ex = new SDFExpression(null, sb.toString(),
		// this.resolver, MEP.getInstance());
		//
		// RelationalPredicate predicate = new RelationalPredicate(ex);
		//
		// LOG.debug("[PredicateHelper] New predicate: "
		// + ((RelationalPredicate) predicate).getExpression()
		// .getExpressionString());
		// return new Pair<IRelationalPredicate, Set<IRelationalPredicate>>(
		// predicate, satisfiablePredicates);
	}

	/**
	 * Generate a new predicate based on a pair of AccessAOs and all related
	 * predicates.
	 * 
	 * @param pair
	 * @return a new predicate.
	 */
	public Pair<IPredicate<?>, Set<IRelationalPredicate>> generatePredicate(
			Pair<AccessAO, AccessAO> pair) {
		Set<AccessAO> source = new HashSet<AccessAO>();
		source.add(pair.getE1());
		return generatePredicate(source, pair.getE2(), null);
	}

	public boolean allPredicatesSatisfied() {
		for (Set<IRelationalPredicate> predicates : this.unsatisfiedPredicates
				.values()) {
			if (!predicates.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Set the given predicates as satisfied for the given join plan.
	 * 
	 * @param joinPlan
	 * @param predicates
	 */
	public void setSatisfied(ILogicalOperator joinPlan,
			Set<IRelationalPredicate> predicates) {
		this.joinToSatisfiedPredicates.put(joinPlan, predicates);
	}

	private Set<IRelationalPredicate> getUnsatisfiedPredicates(
			Set<AccessAO> sources, ILogicalOperator existingJoinPlan) {
		Set<IRelationalPredicate> unsatisfied = new HashSet<IRelationalPredicate>();
		// first collect all predicates which contain attributes originating
		// from the sources.
		for (AccessAO source : sources) {
			AccessAO original = PlanGeneratorHelper.getOriginal2Clone(source);
			unsatisfied.addAll(this.unsatisfiedPredicates.get(original));
		}
		// remove all predicates which are already satisfied in the existing
		// join plan.
		if (existingJoinPlan != null) {
			unsatisfied.removeAll(this.joinToSatisfiedPredicates
					.get(existingJoinPlan));
		}
		return unsatisfied;
	}

	private Set<IRelationalPredicate> getSatisfiablePredicates(
			Set<IRelationalPredicate> predicates, Set<AccessAO> sources) {
		Set<IRelationalPredicate> satisfiable = new HashSet<IRelationalPredicate>();
		for (IRelationalPredicate predicate : predicates) {
			if (isSatisfiable(predicate, sources)) {
				satisfiable.add(predicate);
			}
		}
		return satisfiable;
	}

	private Map<AccessAO, Set<IRelationalPredicate>> collectPredicateMap(
			Set<AccessAO> sources, Set<JoinAO> joins) {
		Map<AccessAO, Set<IRelationalPredicate>> predicateMap = new HashMap<AccessAO, Set<IRelationalPredicate>>();
		for (AccessAO source : sources) {
			Set<IRelationalPredicate> predicates = collectPredicates(joins,
					source);
			predicateMap.put(source, predicates);
		}
		return predicateMap;
	}

	/**
	 * Collects all predicates containing attributes for which the given
	 * AccessAO is the source.
	 * 
	 * @param strippedPlan
	 *            Plan containing only AccessAO and JoinAO operators.
	 * @param source
	 *            AccessAO
	 * @return a collection of predicates based on the AccessAO.
	 */
	private Set<IRelationalPredicate> collectPredicates(Set<JoinAO> joins,
			AccessAO source) {
		Set<IRelationalPredicate> predicates = new HashSet<IRelationalPredicate>();
		for (JoinAO join : joins) {
			for (IPredicate<?> predicate : join.getPredicates()) {
				predicates.add((IRelationalPredicate) predicate);
			}
		}
		return predicates;
	}

	/**
	 * Determine if a given predicate is satisfiable with the join of the given
	 * sources.
	 * 
	 * @param predicate
	 * @param sources
	 * @return true if satisfiable, false if not
	 */
	private boolean isSatisfiable(IRelationalPredicate predicate,
			Set<AccessAO> sources) {
		SDFSchema schema = null;
		for (AccessAO source : sources) {
			if (schema == null) {
				schema = source.getOutputSchema();
			} else {
				schema = SDFSchema.union(schema, source.getOutputSchema());
			}
		}
		IAttributeResolver attrResolver = new DirectAttributeResolver(schema);
		SDFExpression expression = ((RelationalPredicate) predicate)
				.getExpression();
		boolean result = true;
		// FIXME: kontrollfluss mit exceptions sicherzustellen ist evil!
		try {
			new SDFExpression(null,
					expression.getExpressionString(), attrResolver,
					MEP.getInstance());
		} catch (IllegalArgumentException e) {
			result = false;
		}
		return result;

//		Set<String> sourceNames = new HashSet<String>();
//		for (AccessAO source : sources) {
//			// (in diesem Fall System.) davor
//			sourceNames.add(source.getSourcename().substring(7));
//		}
//		for (SDFAttribute attribute : predicate.getAttributes()) {
//			if (!sourceNames.contains(attribute.getSourceName())) {
//				return false;
//			}
//		}
//		return true;
	}

	/**
	 * Split predicates into predicates which only use one relation to operate
	 * if possible.
	 * 
	 * @param predicates
	 * @return a set of IRelationalPredicates which use only one relation if
	 *         possible.
	 */
	private Set<IRelationalPredicate> splitPredicates(
			Set<IRelationalPredicate> predicates) {
		Set<IRelationalPredicate> splitPredicates = new HashSet<IRelationalPredicate>();
		for (IRelationalPredicate predicate : predicates) {
			splitPredicates.addAll(splitPredicate(predicate));
		}
		return splitPredicates;
	}

	private Set<IRelationalPredicate> splitPredicate(
			IRelationalPredicate predicate) {
		// if (hasOnlyTwoRelations(predicate)) {
		// HashSet<IRelationalPredicate> predicates = new
		// HashSet<IRelationalPredicate>();
		// predicates.add(predicate);
		// return predicates;
		// }
		if (predicate instanceof RelationalPredicate
				|| predicate instanceof TypeSafeRelationalPredicate) {
			return splitRelationalPredicate((RelationalPredicate) predicate);
		}
		return new HashSet<IRelationalPredicate>();
	}

	@SuppressWarnings("rawtypes")
	private Set<IRelationalPredicate> splitRelationalPredicate(
			RelationalPredicate predicate) {
		Set<IRelationalPredicate> rPredicates = new HashSet<IRelationalPredicate>();
		// only conjunctive predicates can be split
		if (predicate.isAndPredicate()) {
			List<IPredicate> predicates = predicate.splitPredicate();
			for (IPredicate p : predicates) {
				rPredicates.add(cleanPredicate((RelationalPredicate) p));
			}
		} else {
			rPredicates.add(predicate);
		}
		return rPredicates;
	}

	/**
	 * remove operators which are at the beginning or the end.
	 * 
	 * @param predicate
	 * @return
	 */
	private IRelationalPredicate cleanPredicate(RelationalPredicate predicate) {
		String expression = predicate.getExpression().getExpressionString();
		String trimmedExpression = expression.trim();
		StringBuilder sb = new StringBuilder();
		if (trimmedExpression.contains("&&")) {
			for (int i = 0; i < trimmedExpression.length(); i++) {
				if (trimmedExpression.charAt(i) == '&'
						&& i < trimmedExpression.length() - 1
						&& trimmedExpression.charAt(i + 1) == '&') {
					// nop
				} else {
					sb.append(trimmedExpression.charAt(i));
				}
			}
		} else {
			return predicate;
		}
		return new RelationalPredicate(new SDFExpression(null, sb.toString(),
				this.resolver, MEP.getInstance()));
	}

	public IRelationalPredicate createTruePredicate() {
		return new RelationalPredicate(new SDFExpression(null, "true",
				this.resolver, MEP.getInstance()));
	}
}
