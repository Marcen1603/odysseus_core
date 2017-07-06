/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.base.common;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public final class ExpressionUtils {
    /**
     * Checks whether the given expression is an AND operator.
     * 
     * A AND B AND C ...
     * 
     * @param expression
     *            The expression
     * @return <code>true</code> if the given expression is an AND operator
     */
    public static boolean isAndOperator(final IMepExpression<?> expression) {
        return ((expression.isFunction()) && (expression instanceof AndOperator));
    }

    /**
     * Checks whether the given expression is an OR operator.
     * 
     * A OR B OR C ...
     * 
     * @param expression
     *            The expression
     * @return <code>true</code> if the given expression is an OR operator
     */
    public static boolean isOrOperator(final IMepExpression<?> expression) {
        return ((expression.isFunction()) && (expression instanceof AndOperator));
    }

    /**
     * Checks whether the given expression is an NOT operator.
     * 
     * NOT A ...
     * 
     * @param expression
     *            The expression
     * @return <code>true</code> if the given expression is an NOT operator
     */
    public static boolean isNotOperator(final IMepExpression<?> expression) {
        return ((expression.isFunction()) && (expression instanceof NotOperator));
    }

    /**
     * Conjunctive split the given expression into a set of expressions.
     * 
     * @param expression
     *            The expression
     * 
     * @return A set of expressions
     */
    public static Collection<IMepExpression<?>> conjunctiveSplitExpression(final IMepExpression<?> expression) {
        final Set<IMepExpression<?>> result = new HashSet<IMepExpression<?>>();
        if (ExpressionUtils.isAndOperator(expression)) {
            final Stack<IMepExpression<?>> expressionStack = new Stack<IMepExpression<?>>();
            expressionStack.push(expression);

            while (!expressionStack.isEmpty()) {
                final IMepExpression<?> curExpression = expressionStack.pop();
                if (ExpressionUtils.isAndOperator(curExpression)) {
                    expressionStack.push(curExpression.toFunction().getArgument(0));
                    expressionStack.push(curExpression.toFunction().getArgument(1));
                }
                else {
                    result.add(curExpression);
                }
            }
            return result;

        }
        result.add(expression);
        return result;
    }

    /**
     * Disjunctive split the given expression into a set of expressions.
     * 
     * @param expression
     *            The expression
     * 
     * @return A set of expressions
     */
    public static Collection<IMepExpression<?>> disjunctiveSplitExpression(final IMepExpression<?> expression) {
        final Set<IMepExpression<?>> result = new HashSet<IMepExpression<?>>();

        if (ExpressionUtils.isOrOperator(expression)) {
            final Stack<IMepExpression<?>> expressionStack = new Stack<IMepExpression<?>>();
            expressionStack.push(expression);

            while (!expressionStack.isEmpty()) {
                final IMepExpression<?> curExpression = expressionStack.pop();
                if (ExpressionUtils.isOrOperator(curExpression)) {
                    expressionStack.push(curExpression.toFunction().getArgument(0));
                    expressionStack.push(curExpression.toFunction().getArgument(1));
                }
                else {
                    result.add(curExpression);
                }
            }
            return result;

        }
        result.add(expression);
        return result;
    }

    /**
     * Conjunctive split the given expression into a set of expressions.
     * 
     * @param expression
     *            The expression
     * 
     * @return A set of expressions
     */
    public static Collection<SDFExpression> conjunctiveSplitExpression(final SDFExpression expression) {
        final Set<SDFExpression> result = new TreeSet<SDFExpression>(new Comparator<SDFExpression>() {

            @Override
            public int compare(final SDFExpression o1, final SDFExpression o2) {
                return Integer.compare(o1.getAllAttributes().size(), o2.getAllAttributes().size());
            }
        });
        if (ExpressionUtils.isAndOperator(expression.getMEPExpression())) {
            final Stack<IMepExpression<?>> expressionStack = new Stack<IMepExpression<?>>();
            expressionStack.push(expression.getMEPExpression());

            while (!expressionStack.isEmpty()) {
                final IMepExpression<?> curExpression = expressionStack.pop();
                if (ExpressionUtils.isAndOperator(curExpression)) {
                    expressionStack.push(curExpression.toFunction().getArgument(0));
                    expressionStack.push(curExpression.toFunction().getArgument(1));
                }
                else {
                    final SDFExpression sdfExpression = new SDFExpression(curExpression.toString(), expression.getAttributeResolver(), MEP.getInstance());
                    result.add(sdfExpression);
                }
            }
            return result;

        }
        result.add(expression);
        return result;
    }

    /**
     * Conjunctive split the given expression into a set of expressions.
     * 
     * @param expression
     *            The expression
     * 
     * @return A set of expressions
     */
    public static Collection<SDFProbabilisticExpression> conjunctiveSplitExpression(final SDFProbabilisticExpression expression) {
        final Set<SDFProbabilisticExpression> result = new TreeSet<SDFProbabilisticExpression>(new Comparator<SDFProbabilisticExpression>() {

            @Override
            public int compare(final SDFProbabilisticExpression o1, final SDFProbabilisticExpression o2) {
                return Integer.compare(o1.getAllAttributes().size(), o2.getAllAttributes().size());
            }
        });
        if (ExpressionUtils.isAndOperator(expression.getMEPExpression())) {
            final Stack<IMepExpression<?>> expressionStack = new Stack<IMepExpression<?>>();
            expressionStack.push(expression.getMEPExpression());

            while (!expressionStack.isEmpty()) {
                final IMepExpression<?> curExpression = expressionStack.pop();
                if (ExpressionUtils.isAndOperator(curExpression)) {
                    expressionStack.push(curExpression.toFunction().getArgument(0));
                    expressionStack.push(curExpression.toFunction().getArgument(1));
                }
                else {
                    final SDFProbabilisticExpression sdfExpression = new SDFProbabilisticExpression(curExpression, expression.getAttributeResolver(), MEP.getInstance());
                    result.add(sdfExpression);
                }
            }
            return result;

        }
        result.add(expression);
        return result;
    }

    /**
     * Disjunctive split the given expression into a set of expressions.
     * 
     * @param expression
     *            The expression
     * 
     * @return A set of expressions
     */
    public static Collection<SDFExpression> disjunctiveSplitExpression(final SDFExpression expression) {
        final Set<SDFExpression> result = new TreeSet<SDFExpression>(new Comparator<SDFExpression>() {

            @Override
            public int compare(final SDFExpression o1, final SDFExpression o2) {
                return Integer.compare(o1.getAllAttributes().size(), o2.getAllAttributes().size());
            }
        });
        if (ExpressionUtils.isOrOperator(expression.getMEPExpression())) {
            final Stack<IMepExpression<?>> expressionStack = new Stack<IMepExpression<?>>();
            expressionStack.push(expression.getMEPExpression());

            while (!expressionStack.isEmpty()) {
                final IMepExpression<?> curExpression = expressionStack.pop();
                if (ExpressionUtils.isOrOperator(curExpression)) {
                    expressionStack.push(curExpression.toFunction().getArgument(0));
                    expressionStack.push(curExpression.toFunction().getArgument(1));
                }
                else {
                    final SDFExpression sdfExpression = new SDFExpression(curExpression.toString(), expression.getAttributeResolver(), MEP.getInstance());
                    result.add(sdfExpression);
                }
            }
            return result;

        }
        result.add(expression);
        return result;
    }

    /**
     * Disjunctive split the given expression into a set of expressions.
     * 
     * @param expression
     *            The expression
     * 
     * @return A set of expressions
     */
    public static Collection<SDFProbabilisticExpression> disjunctiveSplitExpression(final SDFProbabilisticExpression expression) {
        final Set<SDFProbabilisticExpression> result = new TreeSet<SDFProbabilisticExpression>(new Comparator<SDFProbabilisticExpression>() {

            @Override
            public int compare(final SDFProbabilisticExpression o1, final SDFProbabilisticExpression o2) {
                return Integer.compare(o1.getAllAttributes().size(), o2.getAllAttributes().size());
            }
        });
        if (ExpressionUtils.isOrOperator(expression.getMEPExpression())) {
            final Stack<IMepExpression<?>> expressionStack = new Stack<IMepExpression<?>>();
            expressionStack.push(expression.getMEPExpression());

            while (!expressionStack.isEmpty()) {
                final IMepExpression<?> curExpression = expressionStack.pop();
                if (ExpressionUtils.isOrOperator(curExpression)) {
                    expressionStack.push(curExpression.toFunction().getArgument(0));
                    expressionStack.push(curExpression.toFunction().getArgument(1));
                }
                else {
                    final SDFProbabilisticExpression sdfExpression = new SDFProbabilisticExpression(curExpression, expression.getAttributeResolver(), MEP.getInstance());
                    result.add(sdfExpression);
                }
            }
            return result;

        }
        result.add(expression);
        return result;
    }

    /**
     * 
     */
    public ExpressionUtils() {
        throw new UnsupportedOperationException();
    }
}
