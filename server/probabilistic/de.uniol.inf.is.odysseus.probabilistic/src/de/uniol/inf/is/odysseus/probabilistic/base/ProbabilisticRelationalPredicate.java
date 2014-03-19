/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.probabilistic.base.common.ExpressionUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.VarHelper;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticRelationalPredicate extends AbstractPredicate<ProbabilisticTuple<? extends IProbabilistic>> implements IProbabilisticRelationalPredicate {
    /**
     * 
     */
    private static final long serialVersionUID = -446918467035191505L;

    private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticRelationalPredicate.class);

    protected SDFProbabilisticExpression expression;
    protected Map<SDFAttribute, SDFAttribute> replacementMap = new HashMap<SDFAttribute, SDFAttribute>();

    private IPredicateEvaluator[][] deterministicExpressions;
    private IPredicateEvaluator[][] discreteDistributionExpressions;
    private IPredicateEvaluator[][] continuousDistributionExpressions;

    protected int[] attributePositions;

    final List<SDFAttribute> neededAttributes;

    protected boolean[] fromRightChannel;

    public ProbabilisticRelationalPredicate(final SDFExpression expression) {
        this.expression = new SDFProbabilisticExpression(expression);
        this.neededAttributes = expression.getAllAttributes();
    }

    public ProbabilisticRelationalPredicate(final SDFProbabilisticExpression expression) {
        this.expression = expression;
        this.neededAttributes = expression.getAllAttributes();
    }

    public ProbabilisticRelationalPredicate(final RelationalPredicate predicate) {
        this(predicate.getExpression());
    }

    public ProbabilisticRelationalPredicate(final ProbabilisticRelationalPredicate predicate) {
        this.attributePositions = predicate.attributePositions == null ? null : (int[]) predicate.attributePositions.clone();
        this.fromRightChannel = predicate.fromRightChannel == null ? null : (boolean[]) predicate.fromRightChannel.clone();
        this.expression = predicate.expression == null ? null : predicate.expression.clone();
        if (this.expression != null) {
            this.neededAttributes = this.expression.getAllAttributes();
        }
        else {
            this.neededAttributes = new ArrayList<SDFAttribute>();
        }
        this.replacementMap = new HashMap<SDFAttribute, SDFAttribute>(predicate.replacementMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean evaluate(final ProbabilisticTuple<? extends IProbabilistic> input) {
        final ProbabilisticTuple<? extends IProbabilistic> result = this.probabilisticEvaluate(input);
        return result.getMetadata().getExistence() > 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean evaluate(final ProbabilisticTuple<? extends IProbabilistic> left, final ProbabilisticTuple<? extends IProbabilistic> right) {
        // ProbabilisticTuple<? extends IProbabilistic> result =
        // probabilisticEvaluate(left, right);
        // return result.getMetadata().getExistence() > 0.0;
        throw new UnsupportedOperationException("Not supported, merge first and call evaluate");

    }

    public ProbabilisticTuple<? extends IProbabilistic> probabilisticEvaluate(final ProbabilisticTuple<? extends IProbabilistic> input) {
        ProbabilisticTuple<? extends IProbabilistic> output = input;
        for (final IPredicateEvaluator[] deterministicExpression : this.deterministicExpressions) {
            output = this.evaluateDeterministicPredicate(input, deterministicExpression);
            if (output.getMetadata().getExistence() == 0.0) {
                break;
            }
        }
        if (output.getMetadata().getExistence() > 0.0) {
            for (final IPredicateEvaluator[] discreteDistributionExpression : this.discreteDistributionExpressions) {
                output = this.evaluateDiscreteDistributionPredicate(output, discreteDistributionExpression);
            }
            if (output.getMetadata().getExistence() > 0.0) {
                for (final IPredicateEvaluator[] continuousDistributionExpression : this.continuousDistributionExpressions) {
                    output = this.evaluateContinuousDistributionPredicate(output, continuousDistributionExpression);
                }
            }
            return output;
        }
        return output;
    }

    public ProbabilisticTuple<? extends IProbabilistic> probabilisticEvaluate(final ProbabilisticTuple<?> left, final ProbabilisticTuple<?> right) {
        throw new UnsupportedOperationException("Not supported, merge first and call evaluate");
    }

    public void init(final SDFSchema schema) {
        Objects.requireNonNull(schema);
        this.initExpression(schema, this.expression);
        this.attributePositions = new int[this.neededAttributes.size()];
        this.fromRightChannel = new boolean[this.neededAttributes.size()];
    }

    @Override
    public void init(final SDFSchema leftSchema, final SDFSchema rightSchema) {
        this.init(leftSchema, rightSchema, true);
    }

    public void init(final SDFSchema leftSchema, final SDFSchema rightSchema, final boolean checkRightSchema) {
        Objects.requireNonNull(leftSchema);

        final List<SDFAttribute> neededAttributes = this.expression.getAllAttributes();
        this.attributePositions = new int[neededAttributes.size()];
        this.fromRightChannel = new boolean[neededAttributes.size()];

        for (int i = 0; i < neededAttributes.size(); i++) {
            final SDFAttribute curAttribute = neededAttributes.get(i);
            int pos = leftSchema.indexOf(curAttribute);
            if (pos == -1) {
                if ((rightSchema == null) && checkRightSchema) {
                    throw new IllegalArgumentException("Attribute " + curAttribute + " not in " + leftSchema + " and rightSchema is null!");
                }
                if (checkRightSchema) {
                    pos = this.indexOf(rightSchema, curAttribute);
                    if (pos == -1) {
                        throw new IllegalArgumentException("Attribute " + curAttribute + " not in " + rightSchema);
                    }
                }
                this.fromRightChannel[i] = true;
            }
            this.attributePositions[i] = pos;
        }
    }

    private int indexOf(final SDFSchema schema, final SDFAttribute attr) {
        final SDFAttribute cqlAttr = this.getReplacement(attr);
        final Iterator<SDFAttribute> it = schema.iterator();
        for (int i = 0; it.hasNext(); ++i) {
            final SDFAttribute a = it.next();
            if (cqlAttr.equalsCQL(a)) {
                return i;
            }
        }
        return -1;
    }

    private SDFAttribute getReplacement(final SDFAttribute a) {
        SDFAttribute ret = a;
        SDFAttribute tmp = null;
        while ((tmp = this.replacementMap.get(ret)) != null) {
            ret = tmp;
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SDFAttribute> getAttributes() {
        return Collections.unmodifiableList(this.expression.getAllAttributes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void replaceAttribute(final SDFAttribute curAttr, final SDFAttribute newAttr) {
        if (!curAttr.equals(newAttr)) {
            this.replacementMap.put(curAttr, newAttr);
        }
        else {
            ProbabilisticRelationalPredicate.LOG.warn("Replacement " + curAttr + " --> " + newAttr + " not added because they are equal!");
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List<IPredicate> conjunctiveSplit(final boolean init) {
        final List<IPredicate> result = new LinkedList<IPredicate>();
        final Collection<IExpression<?>> split = ExpressionUtils.conjunctiveSplitExpression(this.expression.getMEPExpression());
        for (final IExpression<?> expr : split) {
            final SDFProbabilisticExpression sdfExpression = new SDFProbabilisticExpression(expr, this.expression.getAttributeResolver(), MEP.getInstance());
            final ProbabilisticRelationalPredicate predicate = new ProbabilisticRelationalPredicate(sdfExpression);
            result.add(predicate);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public List<IPredicate> disjunctiveSplit(final boolean init) {
        final List<IPredicate> result = new LinkedList<IPredicate>();
        final Collection<IExpression<?>> split = ExpressionUtils.disjunctiveSplitExpression(this.expression.getMEPExpression());
        for (final IExpression<?> expr : split) {
            final SDFProbabilisticExpression sdfExpression = new SDFProbabilisticExpression(expr, this.expression.getAttributeResolver(), MEP.getInstance());
            final ProbabilisticRelationalPredicate predicate = new ProbabilisticRelationalPredicate(sdfExpression);
            result.add(predicate);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isContainedIn(final IPredicate<?> o) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final IPredicate<ProbabilisticTuple<? extends IProbabilistic>> other) {
        if (this.equals((Object) other)) {
            return true;
        }
        else {
            return this.isContainedIn(other) && other.isContainedIn(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((this.expression == null) ? 0 : this.expression.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final ProbabilisticRelationalPredicate other = (ProbabilisticRelationalPredicate) obj;
        if (this.expression == null) {
            if (other.expression != null) {
                return false;
            }
        }
        else if (!this.expression.equals(other.expression)) {
            return false;
        }
        return true;
    }

    @Override
    public ProbabilisticRelationalPredicate clone() {
        return new ProbabilisticRelationalPredicate(this);
    }

    @Override
    public String toString() {
        return this.expression.toString();
    }

    private ProbabilisticTuple<? extends IProbabilistic> evaluateDeterministicPredicate(final ProbabilisticTuple<? extends IProbabilistic> input, final IPredicateEvaluator[] evaluators) {
        Objects.requireNonNull(input);

        ProbabilisticTuple<? extends IProbabilistic> output = null;
        for (final IPredicateEvaluator evaluator : evaluators) {
            output = evaluator.evaluate(input);
            if (output.getMetadata().getExistence() > 0.0) {
                return output;
            }
        }
        if (output == null) {
            output = input;
        }
        return output;
    }

    private ProbabilisticTuple<? extends IProbabilistic> evaluateDiscreteDistributionPredicate(final ProbabilisticTuple<? extends IProbabilistic> input, final IPredicateEvaluator[] evaluators) {
        Objects.requireNonNull(input);

        ProbabilisticTuple<? extends IProbabilistic> output = null;
        double existence = 0.0;
        for (final IPredicateEvaluator evaluator : evaluators) {
            final ProbabilisticTuple<? extends IProbabilistic> tmp = evaluator.evaluate(input.clone());
            if ((output == null) || (tmp.getMetadata().getExistence() > existence)) {
                output = tmp;
                existence = tmp.getMetadata().getExistence();
            }
        }
        if (output == null) {
            output = input;
        }
        return output;
    }

    private ProbabilisticTuple<? extends IProbabilistic> evaluateContinuousDistributionPredicate(final ProbabilisticTuple<? extends IProbabilistic> input, final IPredicateEvaluator[] evaluators) {
        Objects.requireNonNull(input);

        ProbabilisticTuple<? extends IProbabilistic> output = null;
        double existence = 0.0;
        // Evaluate OR predicate
        for (final IPredicateEvaluator evaluator : evaluators) {
            final ProbabilisticTuple<? extends IProbabilistic> tmp = evaluator.evaluate(input.clone());
            if ((output == null) || (tmp.getMetadata().getExistence() > existence)) {
                output = tmp;
                existence = tmp.getMetadata().getExistence();
            }
        }
        if (output == null) {
            output = input;
        }
        return output;
    }

    private void initExpression(final SDFSchema schema, final SDFProbabilisticExpression expression) {
        final List<IPredicateEvaluator[]> deterministicExpr = new ArrayList<IPredicateEvaluator[]>();
        final List<IPredicateEvaluator[]> discreteDistributionExpr = new ArrayList<IPredicateEvaluator[]>();
        final List<IPredicateEvaluator[]> continuousDistributionExpr = new ArrayList<IPredicateEvaluator[]>();

        for (final SDFExpression conjunctiveSplitExpression : ExpressionUtils.conjunctiveSplitExpression(expression)) {
            final List<IPredicateEvaluator> deterministicDisjunctiveExpr = new ArrayList<IPredicateEvaluator>();
            final List<IPredicateEvaluator> discreteDistributionDisjunctiveExpr = new ArrayList<IPredicateEvaluator>();
            final List<IPredicateEvaluator> continuousDistributionDisjunctiveExpr = new ArrayList<IPredicateEvaluator>();
            if (!SchemaUtils.containsProbabilisticAttributes(conjunctiveSplitExpression.getAllAttributes())) {
                for (final SDFExpression disjunctiveSplitExpression : ExpressionUtils.disjunctiveSplitExpression(new SDFProbabilisticExpression(conjunctiveSplitExpression))) {
                    final List<SDFAttribute> neededAttributes = disjunctiveSplitExpression.getAllAttributes();
                    final VarHelper[] varHelper = new VarHelper[neededAttributes.size()];
                    for (int j = 0; j < neededAttributes.size(); j++) {
                        final SDFAttribute curAttribute = neededAttributes.get(j);
                        varHelper[j++] = new VarHelper(schema.indexOf(curAttribute), 0);
                    }
                    deterministicDisjunctiveExpr.add(new DeterministicPredicateEvaluator(new SDFProbabilisticExpression(disjunctiveSplitExpression), varHelper));
                }
            }
            else if (!SchemaUtils.containsProbabilisticAttributes(conjunctiveSplitExpression.getAllAttributes())) {
                for (final SDFExpression disjunctiveSplitExpression : ExpressionUtils.disjunctiveSplitExpression(new SDFProbabilisticExpression(conjunctiveSplitExpression))) {
                    final List<SDFAttribute> neededAttributes = disjunctiveSplitExpression.getAllAttributes();
                    final VarHelper[] varHelper = new VarHelper[neededAttributes.size()];
                    for (int j = 0; j < neededAttributes.size(); j++) {
                        final SDFAttribute curAttribute = neededAttributes.get(j);
                        varHelper[j++] = new VarHelper(schema.indexOf(curAttribute), 0);
                    }
                    final Collection<SDFAttribute> discreteProbabilisticAttributes = SchemaUtils.getProbabilisticAttributes(neededAttributes);
                    discreteDistributionDisjunctiveExpr.add(new DiscreteDistributionPredicateEvaluator(new SDFProbabilisticExpression(disjunctiveSplitExpression), varHelper, SchemaUtils
                            .getAttributePos(schema, discreteProbabilisticAttributes)));
                }
            }
            else {
                for (final SDFExpression disjunctiveSplitExpression : ExpressionUtils.disjunctiveSplitExpression(new SDFProbabilisticExpression(conjunctiveSplitExpression))) {
                    final List<SDFAttribute> neededAttributes = disjunctiveSplitExpression.getAllAttributes();
                    final VarHelper[] varHelper = new VarHelper[neededAttributes.size()];
                    for (int j = 0; j < neededAttributes.size(); j++) {
                        final SDFAttribute curAttribute = neededAttributes.get(j);
                        varHelper[j++] = new VarHelper(schema.indexOf(curAttribute), 0);
                    }
                    continuousDistributionDisjunctiveExpr.add(new ContinuousDistributionPredicateEvaluator(new SDFProbabilisticExpression(disjunctiveSplitExpression), varHelper));
                }
            }

            deterministicExpr.add(deterministicDisjunctiveExpr.toArray(new IPredicateEvaluator[deterministicDisjunctiveExpr.size()]));
            discreteDistributionExpr.add(discreteDistributionDisjunctiveExpr.toArray(new IPredicateEvaluator[discreteDistributionDisjunctiveExpr.size()]));
            continuousDistributionExpr.add(continuousDistributionDisjunctiveExpr.toArray(new IPredicateEvaluator[continuousDistributionDisjunctiveExpr.size()]));

        }

        this.deterministicExpressions = deterministicExpr.toArray(new IPredicateEvaluator[deterministicExpr.size()][]);
        this.discreteDistributionExpressions = discreteDistributionExpr.toArray(new IPredicateEvaluator[deterministicExpr.size()][]);
        this.continuousDistributionExpressions = continuousDistributionExpr.toArray(new IPredicateEvaluator[deterministicExpr.size()][]);
    }

}
