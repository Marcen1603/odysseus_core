/*
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.server.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.core.server.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

//import de.uniol.inf.is.odysseus.core.server.predicate.AndPredicate;
/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public final class PredicateUtils {
	/**
	 * Checks whether the given predicate is an AND predicate.
	 * 
	 * A AND B AND C ...
	 * 
	 * @param predicate
	 *            The predicate
	 * @return <code>true</code> if the given predicate is an AND predicate
	 */
	public static boolean isAndPredicate(final IPredicate<?> predicate) {
		if (predicate instanceof AndPredicate) {
			return ComplexPredicateHelper.isAndPredicate(predicate);
		} else if (predicate instanceof RelationalPredicate) {
			return ((RelationalPredicate) predicate).isAndPredicate();
		}
		return false;
	}

	/**
	 * Checks whether the given predicate is an OR predicate.
	 * 
	 * A OR B OR C ...
	 * 
	 * @param predicate
	 *            The predicate
	 * @return <code>true</code> if the given predicate is an OR predicate
	 */
	public static boolean isOrPredicate(final IPredicate<?> predicate) {
		if (predicate instanceof OrPredicate) {
			return ComplexPredicateHelper.isOrPredicate(predicate);
		} else if (predicate instanceof RelationalPredicate) {
			return ((RelationalPredicate) predicate).isOrPredicate();
		}
		return false;
	}

	/**
	 * Checks whether the given predicate is an OR predicate.
	 * 
	 * NOT A ...
	 * 
	 * @param predicate
	 *            The predicate
	 * @return <code>true</code> if the given predicate is a NOT predicate
	 */
	public static boolean isNotPredicate(final IPredicate<?> predicate) {
		if (predicate instanceof NotPredicate) {
			return ComplexPredicateHelper.isNotPredicate(predicate);
		} else if (predicate instanceof RelationalPredicate) {
			return ((RelationalPredicate) predicate).isNotPredicate();
		}
		return false;
	}

	/**
	 * Splits the given predicate if it is an AND predicate into sub-predicates.
	 * 
	 * @param predicate
	 *            The predicate
	 * @return A list of predicates
	 */
	@SuppressWarnings("rawtypes")
	public static List<IPredicate> splitPredicate(final IPredicate<?> predicate) {
		if (PredicateUtils.isAndPredicate(predicate)) {
			if (predicate instanceof AndPredicate) {
				return ComplexPredicateHelper.splitPredicate(predicate);
			} else if (predicate instanceof RelationalPredicate) {
				return ((RelationalPredicate) predicate).splitPredicate(false);
			}
		}
		final ArrayList<IPredicate> predicates = new ArrayList<IPredicate>();
		predicates.add(predicate);
		return predicates;
	}

	/**
	 * Get all attributes used in the given predicate.
	 * 
	 * @param predicate
	 *            The predicate
	 * @return A set of referenced attributes
	 */
	@SuppressWarnings("rawtypes")
	public static Set<SDFAttribute> getAttributes(final IPredicate<?> predicate) {
		final Set<SDFAttribute> attributes = new HashSet<SDFAttribute>();
		final List<IPredicate> predicates = PredicateUtils.splitPredicate(predicate);
		for (final IPredicate pre : predicates) {
			if (pre instanceof RelationalPredicate) {
				attributes.addAll(((RelationalPredicate) pre).getAttributes());
			} else {
				if (pre instanceof OrPredicate) {
					attributes.addAll(PredicateUtils.getAttributes(((OrPredicate) pre).getLeft()));
					attributes.addAll(PredicateUtils.getAttributes(((OrPredicate) pre).getRight()));
				} else if (pre instanceof NotPredicate) {
					attributes.addAll(PredicateUtils.getAttributes(((NotPredicate) pre).getChild()));
				}
			}
		}
		return attributes;
	}

	/**
	 * Gets all expressions used in the given predicate.
	 * 
	 * @param predicate
	 *            The predicate
	 * @return The list of expressions
	 */
	public static List<SDFExpression> getExpressions(final IPredicate<?> predicate) {
		final List<SDFExpression> expressions = new ArrayList<SDFExpression>();
		@SuppressWarnings("rawtypes")
		final List<IPredicate> predicates = PredicateUtils.splitPredicate(predicate);
		for (final IPredicate<?> pre : predicates) {
			if (pre instanceof RelationalPredicate) {
				expressions.add(((RelationalPredicate) pre).getExpression());
			}
		}
		return expressions;
	}

	/**
	 * Private constructor.
	 */
	private PredicateUtils() {
	}
}
