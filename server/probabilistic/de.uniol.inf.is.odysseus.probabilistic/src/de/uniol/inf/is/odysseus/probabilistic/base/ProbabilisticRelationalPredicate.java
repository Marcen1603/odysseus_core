/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.probabilistic.base.common.ExpressionUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.VarHelper;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.NormalDistributionMixture;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticRelationalPredicate implements IPredicate<ProbabilisticTuple<? extends IProbabilistic>> {
    /**
     * 
     */
    private static final long serialVersionUID = -446918467035191505L;

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(ProbabilisticRelationalPredicate.class);

    protected SDFProbabilisticExpression expression;

    private PredicateEvaluator[] deterministicExpressions;
    private PredicateEvaluator[] discreteDistributionExpressions;
    private PredicateEvaluator[] continuousDistributionExpressions;

    protected int[] attributePositions;

    final List<SDFAttribute> neededAttributes;

    protected boolean[] fromRightChannel;

    public ProbabilisticRelationalPredicate(SDFExpression expression) {
        this.expression = new SDFProbabilisticExpression(expression);
        this.neededAttributes = expression.getAllAttributes();
    }

    public ProbabilisticRelationalPredicate(SDFProbabilisticExpression expression) {
        this.expression = expression;
        this.neededAttributes = expression.getAllAttributes();
    }

    public ProbabilisticRelationalPredicate(RelationalPredicate predicate) {
        this(predicate.getExpression());
    }

    public ProbabilisticRelationalPredicate(ProbabilisticRelationalPredicate predicate) {
        this.attributePositions = predicate.attributePositions == null ? null : (int[]) predicate.attributePositions.clone();
        this.fromRightChannel = predicate.fromRightChannel == null ? null : (boolean[]) predicate.fromRightChannel.clone();
        this.expression = predicate.expression == null ? null : predicate.expression.clone();
        if (this.expression != null) {
            this.neededAttributes = expression.getAllAttributes();
        }
        else {
            this.neededAttributes = new ArrayList<SDFAttribute>();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean evaluate(ProbabilisticTuple<? extends IProbabilistic> input) {
        ProbabilisticTuple<? extends IProbabilistic> result = probabilisticEvaluate(input);
        return result.getMetadata().getExistence() > 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean evaluate(ProbabilisticTuple<? extends IProbabilistic> left, ProbabilisticTuple<? extends IProbabilistic> right) {
        ProbabilisticTuple<? extends IProbabilistic> result = probabilisticEvaluate(left, right);
        return result.getMetadata().getExistence() > 0.0;
    }

    public ProbabilisticTuple<? extends IProbabilistic> probabilisticEvaluate(ProbabilisticTuple<? extends IProbabilistic> input) {

        ProbabilisticTuple<? extends IProbabilistic> output = null;
        for (int i = 0; i < this.deterministicExpressions.length; ++i) {
            output = evaluateDeterministicPredicate(input, deterministicExpressions[i]);
            if (output == null) {
                break;
            }
        }
        if (output != null) {
            for (int i = 0; i < this.discreteDistributionExpressions.length; ++i) {
                output = evaluateDiscreteDistributionPredicate(output, discreteDistributionExpressions[i]);
            }
            if (output.getMetadata().getExistence() > 0.0) {
                for (int i = 0; i < this.continuousDistributionExpressions.length; ++i) {
                    output = evaluateContinuousDistributionPredicate(output, continuousDistributionExpressions[i]);
                }
            }
            return output;
        }
        else {
            return input;
        }
    }

    public ProbabilisticTuple<? extends IProbabilistic> probabilisticEvaluate(ProbabilisticTuple<?> left, ProbabilisticTuple<?> right) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {

    }

    public void init(SDFSchema schema) {
        Objects.requireNonNull(schema);
        initExpression(schema, this.expression);
        this.attributePositions = new int[neededAttributes.size()];
        this.fromRightChannel = new boolean[neededAttributes.size()];
    }

    public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
        Objects.requireNonNull(leftSchema);
        Objects.requireNonNull(rightSchema);

        List<SDFAttribute> neededAttributes = expression.getAllAttributes();
        this.attributePositions = new int[neededAttributes.size()];
        this.fromRightChannel = new boolean[neededAttributes.size()];

        for (int i = 0; i < neededAttributes.size(); i++) {
            SDFAttribute curAttribute = neededAttributes.get(i);
            Objects.requireNonNull(curAttribute);
            int pos = leftSchema.indexOf(curAttribute);
            if (pos == -1) {
                pos = rightSchema.indexOf(curAttribute);
                if (pos == -1) {
                    throw new IllegalArgumentException("Attribute " + curAttribute + " not in " + rightSchema);
                }
                this.fromRightChannel[i] = true;
            }
            this.attributePositions[i] = pos;
        }
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
    @SuppressWarnings("rawtypes")
    @Override
    public List<IPredicate> conjunctiveSplit(boolean init) {
        List<IPredicate> result = new LinkedList<IPredicate>();
        Collection<IExpression<?>> split = ExpressionUtils.conjunctiveSplitExpression(this.expression.getMEPExpression());
        for (IExpression<?> expr : split) {
            SDFProbabilisticExpression sdfExpression = new SDFProbabilisticExpression(expr, expression.getAttributeResolver(), MEP.getInstance());
            ProbabilisticRelationalPredicate predicate = new ProbabilisticRelationalPredicate(sdfExpression);
            result.add(predicate);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public List<IPredicate> disjunctiveSplit(boolean init) {
        List<IPredicate> result = new LinkedList<IPredicate>();
        Collection<IExpression<?>> split = ExpressionUtils.disjunctiveSplitExpression(this.expression.getMEPExpression());
        for (IExpression<?> expr : split) {
            SDFProbabilisticExpression sdfExpression = new SDFProbabilisticExpression(expr, expression.getAttributeResolver(), MEP.getInstance());
            ProbabilisticRelationalPredicate predicate = new ProbabilisticRelationalPredicate(sdfExpression);
            result.add(predicate);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isContainedIn(IPredicate<?> o) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(IPredicate<ProbabilisticTuple<? extends IProbabilistic>> other) {
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
        result = prime * result + ((this.expression == null) ? 0 : this.expression.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ProbabilisticRelationalPredicate other = (ProbabilisticRelationalPredicate) obj;
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

    private ProbabilisticTuple<? extends IProbabilistic> evaluateDeterministicPredicate(ProbabilisticTuple<? extends IProbabilistic> input, PredicateEvaluator evaluator) {
        Collection<SDFProbabilisticExpression> expressions = evaluator.expressions;
        boolean result = true;
        for (SDFProbabilisticExpression expression : expressions) {
            final Object[] values = new Object[evaluator.varHelper.length];
            for (int j = 0; j < evaluator.varHelper.length; ++j) {
                Object attribute = input.getAttribute(evaluator.varHelper[j].getPos());
                if (attribute.getClass() == ProbabilisticContinuousDouble.class) {
                    int index = ((ProbabilisticContinuousDouble) attribute).getDistribution();
                    attribute = input.getDistribution(index);
                }
                values[j] = attribute;
            }

            expression.bindMetaAttribute(input.getMetadata());
            expression.bindDistributions(input.getDistributions());
            expression.bindAdditionalContent(input.getAdditionalContent());
            expression.bindVariables(values);

            result = (boolean) expression.getValue();
            if (result) {
                break;
            }
        }
        if (!result) {
            return null;
        }
        return input;
    }

    private ProbabilisticTuple<? extends IProbabilistic> evaluateDiscreteDistributionPredicate(ProbabilisticTuple<? extends IProbabilistic> input, PredicateEvaluator evaluator) {

        ProbabilisticTuple<? extends IProbabilistic> output = null;

        Collection<SDFProbabilisticExpression> expressions = evaluator.expressions;
        double existence = input.getMetadata().getExistence();
        for (SDFProbabilisticExpression expression : expressions) {
            ProbabilisticTuple<? extends IProbabilistic> clone = input.clone();

            final Object[] values = new Object[evaluator.varHelper.length];
            for (int j = 0; j < evaluator.varHelper.length; ++j) {
                Object attribute = input.getAttribute(evaluator.varHelper[j].getPos());
                if (attribute.getClass() == ProbabilisticContinuousDouble.class) {
                    int index = ((ProbabilisticContinuousDouble) attribute).getDistribution();
                    attribute = input.getDistribution(index);
                }
                values[j] = attribute;
            }

            expression.bindMetaAttribute(input.getMetadata());
            expression.bindDistributions(input.getDistributions());
            expression.bindAdditionalContent(input.getAdditionalContent());
            expression.bindVariables(values);

            // Evaluate all worlds....

            if ((output == null) || (clone.getMetadata().getExistence() > existence)) {
                output = clone;
                existence = clone.getMetadata().getExistence();
            }
        }
        if (existence == 0.0) {
            return null;
        }
        output.getMetadata().setExistence(existence);
        return output;
    }

    private ProbabilisticTuple<? extends IProbabilistic> evaluateContinuousDistributionPredicate(ProbabilisticTuple<? extends IProbabilistic> input, PredicateEvaluator evaluator) {
        ProbabilisticTuple<? extends IProbabilistic> output = null;

        Collection<SDFProbabilisticExpression> expressions = evaluator.expressions;
        double existence = input.getMetadata().getExistence();
        double scale = 1.0;
        for (SDFProbabilisticExpression expression : expressions) {
            ProbabilisticTuple<? extends IProbabilistic> clone = input.clone();

            final Object[] values = new Object[evaluator.varHelper.length];
            int d = -1;
            for (int j = 0; j < evaluator.varHelper.length; ++j) {
                Object attribute = input.getAttribute(evaluator.varHelper[j].getPos());
                if (attribute.getClass() == ProbabilisticContinuousDouble.class) {
                    int index = ((ProbabilisticContinuousDouble) attribute).getDistribution();
                    attribute = input.getDistribution(index);
                    scale = ((NormalDistributionMixture) attribute).getScale();
                    // FIXME What happens if we have more than one
                    // distribution inside an expression or even other
                    // functions? (CKu 17.12.2013)
                    if (d >= 0) {
                        throw new IllegalArgumentException("More than one distribution not supported inside predicate");
                    }
                    d = index;
                }
                values[j] = attribute;
            }

            expression.bindMetaAttribute(input.getMetadata());
            expression.bindDistributions(input.getDistributions());
            expression.bindAdditionalContent(input.getAdditionalContent());
            expression.bindVariables(values);

            final Object expr = expression.getValue();
            if (expression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE)) {
                final NormalDistributionMixture distribution = (NormalDistributionMixture) expr;
                existence *= scale / distribution.getScale();
                // distribution.getAttributes()[0] = i;
                output.setDistribution(d, distribution);
                d++;
            }
            else {
                if (!(Boolean) expr) {
                    existence = 0.0;
                }
            }

            // Evaluate

            if ((output == null) || (clone.getMetadata().getExistence() > existence)) {
                output = clone;
                existence = clone.getMetadata().getExistence();
            }
        }
        if (existence == 0.0) {
            return null;
        }
        output.getMetadata().setExistence(existence);
        return null;
    }

    private void initExpression(SDFSchema schema, SDFProbabilisticExpression expression) {
        List<PredicateEvaluator> deterministicExpr = new ArrayList<PredicateEvaluator>();
        List<PredicateEvaluator> discreteDistributionExpr = new ArrayList<PredicateEvaluator>();
        List<PredicateEvaluator> continuousDistributionExpr = new ArrayList<PredicateEvaluator>();

        Collection<SDFProbabilisticExpression> split = ExpressionUtils.conjunctiveSplitExpression(expression);

        for (SDFExpression expr : split) {
            final List<SDFAttribute> neededAttributes = expr.getAllAttributes();

            final VarHelper[] varHelper = new VarHelper[neededAttributes.size()];

            for (int j = 0; j < neededAttributes.size(); j++) {
                final SDFAttribute curAttribute = neededAttributes.get(j);
                varHelper[j++] = new VarHelper(schema.indexOf(curAttribute), 0, curAttribute.getDatatype() instanceof SDFProbabilisticDatatype);
            }
            if (!SchemaUtils.containsProbabilisticAttributes(expr.getAllAttributes())) {
                deterministicExpr.add(new PredicateEvaluator(ExpressionUtils.disjunctiveSplitExpression(new SDFProbabilisticExpression(expr)), varHelper));
            }
            else if (!SchemaUtils.containsContinuousProbabilisticAttributes(expr.getAllAttributes())) {
                discreteDistributionExpr.add(new PredicateEvaluator(ExpressionUtils.disjunctiveSplitExpression(new SDFProbabilisticExpression(expr)), varHelper));
            }
            else {
                continuousDistributionExpr.add(new PredicateEvaluator(ExpressionUtils.disjunctiveSplitExpression(new SDFProbabilisticExpression(expr)), varHelper));
            }
        }

        this.deterministicExpressions = deterministicExpr.toArray(new PredicateEvaluator[deterministicExpr.size()]);
        this.discreteDistributionExpressions = discreteDistributionExpr.toArray(new PredicateEvaluator[deterministicExpr.size()]);
        this.continuousDistributionExpressions = continuousDistributionExpr.toArray(new PredicateEvaluator[deterministicExpr.size()]);
    }

    private class PredicateEvaluator {
        VarHelper[] varHelper;
        Collection<SDFProbabilisticExpression> expressions;

        /**
         * Creates a new instances of a predicate evaluator.
         */
        public PredicateEvaluator(Collection<SDFProbabilisticExpression> expressions, VarHelper[] varHelper) {
            this.expressions = expressions;
            this.varHelper = varHelper;
        }
    }

}
