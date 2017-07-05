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
package de.uniol.inf.is.odysseus.probabilistic.base.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;

import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.EqualsOperator;
import de.uniol.inf.is.odysseus.probabilistic.base.predicate.ProbabilisticRelationalPredicate;

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
    @SuppressWarnings("deprecation")
	public static boolean isAndPredicate(final IPredicate<?> predicate) {
        if (predicate instanceof AndPredicate) {
            return ComplexPredicateHelper.isAndPredicate(predicate);
        }
        else if (predicate instanceof RelationalExpression) {
            return ((RelationalExpression<?>) predicate).isAndPredicate();
        }
        else if (predicate instanceof ProbabilisticRelationalPredicate) {
            return ((ProbabilisticRelationalPredicate) predicate).isAndPredicate();
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
        }
        else if (predicate instanceof RelationalExpression) {
            return ((RelationalExpression<?>) predicate).isOrPredicate();
        }
        else if (predicate instanceof ProbabilisticRelationalPredicate) {
            return ((RelationalExpression<?>) predicate).isOrPredicate();
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
        }
        else if (predicate instanceof RelationalExpression) {
            return ((RelationalExpression<?>) predicate).isNotPredicate();
        }
        else if (predicate instanceof ProbabilisticRelationalPredicate) {
            return ((RelationalExpression<?>) predicate).isNotPredicate();
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
    @SuppressWarnings({ "rawtypes" })
    public static Collection<IPredicate<?>> conjunctiveSplit(final IPredicate<?> predicate) {
        final Collection<IPredicate<?>> result = new LinkedList<IPredicate<?>>();
        if (PredicateUtils.isAndPredicate(predicate)) {
            if (predicate instanceof AndPredicate) {

                final Stack<IPredicate<?>> predicateStack = new Stack<IPredicate<?>>();
                predicateStack.push(predicate);
                while (!predicateStack.isEmpty()) {
                    final IPredicate curPredicate = predicateStack.pop();
                    if (curPredicate instanceof AndPredicate) {
                        predicateStack.push(((AndPredicate) curPredicate).getLeft());
                        predicateStack.push(((AndPredicate) curPredicate).getRight());
                    }
                    else {
                        result.add(curPredicate);
                    }
                }
                return result;
            }
            else {
                result.add(predicate);
                // result.addAll((Collection<? extends IPredicate<?>>)
                // predicate.conjunctiveSplit(false));
                return result;
            }
        }
        final ArrayList<IPredicate<?>> predicates = new ArrayList<IPredicate<?>>();
        predicates.add(predicate);
        return predicates;
    }

    /**
     * Splits the given predicate if it is an OR predicate into sub-predicates.
     *
     * @param predicate
     *            The predicate
     * @return A list of predicates
     */
    @SuppressWarnings("rawtypes")
    public static Collection<IPredicate<?>> disjunctiveSplit(final IPredicate<?> predicate) {
        final Collection<IPredicate<?>> result = new LinkedList<IPredicate<?>>();
        if (PredicateUtils.isAndPredicate(predicate)) {
            if (predicate instanceof OrPredicate) {

                final Stack<IPredicate<?>> predicateStack = new Stack<IPredicate<?>>();
                predicateStack.push(predicate);
                while (!predicateStack.isEmpty()) {
                    final IPredicate curPredicate = predicateStack.pop();
                    if (curPredicate instanceof OrPredicate) {
                        predicateStack.push(((OrPredicate) curPredicate).getLeft());
                        predicateStack.push(((OrPredicate) curPredicate).getRight());
                    }
                    else {
                        result.add(curPredicate);
                    }
                }
                return result;
            }
            else {
                // result.addAll((Collection<? extends IPredicate<?>>)
                // predicate.disconjunctiveSplit(false));
                return result;
            }
        }
        final ArrayList<IPredicate<?>> predicates = new ArrayList<IPredicate<?>>();
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
    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
    public static Set<SDFAttribute> getAttributes(final IPredicate<?> predicate) {
        final Set<SDFAttribute> attributes = new HashSet<SDFAttribute>();
        final Collection<IPredicate<?>> predicates = PredicateUtils.conjunctiveSplit(predicate);
        for (final IPredicate pre : predicates) {
            if (pre instanceof RelationalExpression) {
                attributes.addAll(((RelationalExpression) pre).getAttributes());
            }
            else if (pre instanceof ProbabilisticRelationalPredicate) {
                attributes.addAll(((ProbabilisticRelationalPredicate) pre).getAttributes());
            }
            else {
                if (pre instanceof OrPredicate) {
                    attributes.addAll(PredicateUtils.getAttributes(((OrPredicate) pre).getLeft()));
                    attributes.addAll(PredicateUtils.getAttributes(((OrPredicate) pre).getRight()));
                }
                if (pre instanceof AndPredicate) {
                    attributes.addAll(PredicateUtils.getAttributes(((AndPredicate) pre).getLeft()));
                    attributes.addAll(PredicateUtils.getAttributes(((AndPredicate) pre).getRight()));
                }
                else if (pre instanceof NotPredicate) {
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
    @SuppressWarnings("deprecation")
	public static List<SDFExpression> getExpressions(final IPredicate<?> predicate) {
        final List<SDFExpression> expressions = new ArrayList<SDFExpression>();
        final Collection<IPredicate<?>> predicates = PredicateUtils.conjunctiveSplit(predicate);
        for (final IPredicate<?> pre : predicates) {
            if (pre instanceof RelationalExpression) {
                expressions.add(((RelationalExpression<?>) pre).getExpression());
            }
            else if (pre instanceof ProbabilisticRelationalPredicate) {
                expressions.add(((ProbabilisticRelationalPredicate) pre).getExpression());
            }
        }
        return expressions;
    }

    /**
     * Return true if the given expression is of the form:
     *
     * A.x=B.y AND A.y=B.z AND * ...
     *
     * @param expression
     *            The expression
     * @return <code>true</code> iff the expression is of the given form
     */
    public static boolean isEquiExpression(final IMepExpression<?> expression) {
        Objects.requireNonNull(expression);
        if (expression instanceof AndOperator) {
            return PredicateUtils.isEquiExpression(((AndOperator) expression).getArgument(0)) && PredicateUtils.isEquiExpression(((AndOperator) expression).getArgument(1));

        }
        if (expression instanceof EqualsOperator) {
            final EqualsOperator eq = (EqualsOperator) expression;
            final IMepExpression<?> arg1 = eq.getArgument(0);
            final IMepExpression<?> arg2 = eq.getArgument(1);
            if ((arg1 instanceof IMepVariable) && (arg2 instanceof IMepVariable)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the map of attributes used in a equi expression.
     *
     * @param expression
     *            The expression
     * @param resolver
     *            The attribute resolver
     * @return The map of attributes
     */
    public static Map<SDFAttribute, List<SDFAttribute>> getEquiExpressionAtributes(final IMepExpression<?> expression, final IAttributeResolver resolver) {
        Objects.requireNonNull(expression);
        Objects.requireNonNull(resolver);
        final Map<SDFAttribute, List<SDFAttribute>> attributes = new HashMap<SDFAttribute, List<SDFAttribute>>();
        if (expression instanceof AndOperator) {
            final Map<SDFAttribute, List<SDFAttribute>> leftAttributes = PredicateUtils.getEquiExpressionAtributes(((AndOperator) expression).getArgument(0), resolver);
            for (final SDFAttribute key : leftAttributes.keySet()) {
                if (!attributes.containsKey(key)) {
                    attributes.put(key, new ArrayList<SDFAttribute>());
                }
                attributes.get(key).addAll(leftAttributes.get(key));
            }
            final Map<SDFAttribute, List<SDFAttribute>> rigthAttributes = PredicateUtils.getEquiExpressionAtributes(((AndOperator) expression).getArgument(1), resolver);
            for (final SDFAttribute key : rigthAttributes.keySet()) {
                if (!attributes.containsKey(key)) {
                    attributes.put(key, new ArrayList<SDFAttribute>());
                }
                attributes.get(key).addAll(rigthAttributes.get(key));
            }
        }
        if (expression instanceof EqualsOperator) {
            final EqualsOperator eq = (EqualsOperator) expression;
            final IMepExpression<?> arg1 = eq.getArgument(0);
            final IMepExpression<?> arg2 = eq.getArgument(1);
            if ((arg1 instanceof IMepVariable) && (arg2 instanceof IMepVariable)) {
                final SDFAttribute key = resolver.getAttribute(((IMepVariable) arg1).getIdentifier());
                if (!attributes.containsKey(key)) {
                    attributes.put(key, new ArrayList<SDFAttribute>());
                }
                attributes.get(key).add(resolver.getAttribute(((IMepVariable) arg2).getIdentifier()));
            }
        }
        return attributes;
    }

    /**
     * Return true if the given relational predicate is of the form:
     *
     * A.x=B.y AND A.y=B.z AND * ...
     *
     * @param predicate
     *            The relational predicate
     * @return <code>true</code> iff the relational predicate is of the given
     *         form
     */
    public static boolean isEquiPredicate(final RelationalExpression<?> predicate) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(predicate.getMEPExpression());
        final IMepExpression<?> expression = predicate.getMEPExpression();
        return PredicateUtils.isEquiExpression(expression);
    }

    /**
     * Private constructor.
     */
    private PredicateUtils() {
        throw new UnsupportedOperationException();
    }
}
