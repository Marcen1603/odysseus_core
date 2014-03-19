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
        this.replacementMap = new HashMap<SDFAttribute, SDFAttribute>(predicate.replacementMap);
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
        // ProbabilisticTuple<? extends IProbabilistic> result =
        // probabilisticEvaluate(left, right);
        // return result.getMetadata().getExistence() > 0.0;
        throw new UnsupportedOperationException("Not supported, merge first and call evaluate");

    }

    public ProbabilisticTuple<? extends IProbabilistic> probabilisticEvaluate(ProbabilisticTuple<? extends IProbabilistic> input) {
        ProbabilisticTuple<? extends IProbabilistic> output = input;
        for (int i = 0; i < this.deterministicExpressions.length; ++i) {
            output = evaluateDeterministicPredicate(input, deterministicExpressions[i]);
            if (output.getMetadata().getExistence() == 0.0) {
                break;
            }
        }
        if (output.getMetadata().getExistence() > 0.0) {
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
        return output;
    }

    public ProbabilisticTuple<? extends IProbabilistic> probabilisticEvaluate(ProbabilisticTuple<?> left, ProbabilisticTuple<?> right) {
        throw new UnsupportedOperationException("Not supported, merge first and call evaluate");
    }

    public void init(SDFSchema schema) {
        Objects.requireNonNull(schema);
        initExpression(schema, this.expression);
        this.attributePositions = new int[neededAttributes.size()];
        this.fromRightChannel = new boolean[neededAttributes.size()];
    }

    @Override
    public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
        init(leftSchema, rightSchema, true);
    }

    public void init(SDFSchema leftSchema, SDFSchema rightSchema, boolean checkRightSchema) {
        Objects.requireNonNull(leftSchema);

        List<SDFAttribute> neededAttributes = expression.getAllAttributes();
        this.attributePositions = new int[neededAttributes.size()];
        this.fromRightChannel = new boolean[neededAttributes.size()];

        for (int i = 0; i < neededAttributes.size(); i++) {
            SDFAttribute curAttribute = neededAttributes.get(i);
            int pos = leftSchema.indexOf(curAttribute);
            if (pos == -1) {
                if (rightSchema == null && checkRightSchema) {
                    throw new IllegalArgumentException("Attribute " + curAttribute + " not in " + leftSchema + " and rightSchema is null!");
                }
                if (checkRightSchema) {
                    pos = indexOf(rightSchema, curAttribute);
                    if (pos == -1) {
                        throw new IllegalArgumentException("Attribute " + curAttribute + " not in " + rightSchema);
                    }
                }
                this.fromRightChannel[i] = true;
            }
            this.attributePositions[i] = pos;
        }
    }

    private int indexOf(SDFSchema schema, SDFAttribute attr) {
        SDFAttribute cqlAttr = getReplacement(attr);
        Iterator<SDFAttribute> it = schema.iterator();
        for (int i = 0; it.hasNext(); ++i) {
            SDFAttribute a = it.next();
            if (cqlAttr.equalsCQL(a)) {
                return i;
            }
        }
        return -1;
    }

    private SDFAttribute getReplacement(SDFAttribute a) {
        SDFAttribute ret = a;
        SDFAttribute tmp = null;
        while ((tmp = replacementMap.get(ret)) != null) {
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
    public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
        if (!curAttr.equals(newAttr)) {
            replacementMap.put(curAttr, newAttr);
        }
        else {
            LOG.warn("Replacement " + curAttr + " --> " + newAttr + " not added because they are equal!");
        }
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

    private ProbabilisticTuple<? extends IProbabilistic> evaluateDeterministicPredicate(ProbabilisticTuple<? extends IProbabilistic> input, IPredicateEvaluator[] evaluators) {
        Objects.requireNonNull(input);

        ProbabilisticTuple<? extends IProbabilistic> output = null;
        for (IPredicateEvaluator evaluator : evaluators) {
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

    private ProbabilisticTuple<? extends IProbabilistic> evaluateDiscreteDistributionPredicate(ProbabilisticTuple<? extends IProbabilistic> input, IPredicateEvaluator[] evaluators) {
        Objects.requireNonNull(input);

        ProbabilisticTuple<? extends IProbabilistic> output = null;
        double existence = 0.0;
        for (IPredicateEvaluator evaluator : evaluators) {
            ProbabilisticTuple<? extends IProbabilistic> tmp = evaluator.evaluate(input.clone());
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

    private ProbabilisticTuple<? extends IProbabilistic> evaluateContinuousDistributionPredicate(ProbabilisticTuple<? extends IProbabilistic> input, IPredicateEvaluator[] evaluators) {
        Objects.requireNonNull(input);

        ProbabilisticTuple<? extends IProbabilistic> output = null;
        double existence = 0.0;
        // Evaluate OR predicate
        for (IPredicateEvaluator evaluator : evaluators) {
            ProbabilisticTuple<? extends IProbabilistic> tmp = evaluator.evaluate(input.clone());
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

    private void initExpression(SDFSchema schema, SDFProbabilisticExpression expression) {
        List<IPredicateEvaluator[]> deterministicExpr = new ArrayList<IPredicateEvaluator[]>();
        List<IPredicateEvaluator[]> discreteDistributionExpr = new ArrayList<IPredicateEvaluator[]>();
        List<IPredicateEvaluator[]> continuousDistributionExpr = new ArrayList<IPredicateEvaluator[]>();

        for (SDFExpression conjunctiveSplitExpression : ExpressionUtils.conjunctiveSplitExpression(expression)) {
            List<IPredicateEvaluator> deterministicDisjunctiveExpr = new ArrayList<IPredicateEvaluator>();
            List<IPredicateEvaluator> discreteDistributionDisjunctiveExpr = new ArrayList<IPredicateEvaluator>();
            List<IPredicateEvaluator> continuousDistributionDisjunctiveExpr = new ArrayList<IPredicateEvaluator>();
            if (!SchemaUtils.containsProbabilisticAttributes(conjunctiveSplitExpression.getAllAttributes())) {
                for (SDFExpression disjunctiveSplitExpression : ExpressionUtils.disjunctiveSplitExpression(new SDFProbabilisticExpression(conjunctiveSplitExpression))) {
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
                for (SDFExpression disjunctiveSplitExpression : ExpressionUtils.disjunctiveSplitExpression(new SDFProbabilisticExpression(conjunctiveSplitExpression))) {
                    final List<SDFAttribute> neededAttributes = disjunctiveSplitExpression.getAllAttributes();
                    final VarHelper[] varHelper = new VarHelper[neededAttributes.size()];
                    for (int j = 0; j < neededAttributes.size(); j++) {
                        final SDFAttribute curAttribute = neededAttributes.get(j);
                        varHelper[j++] = new VarHelper(schema.indexOf(curAttribute), 0);
                    }
                    Collection<SDFAttribute> discreteProbabilisticAttributes = SchemaUtils.getProbabilisticAttributes(neededAttributes);
                    discreteDistributionDisjunctiveExpr.add(new DiscreteDistributionPredicateEvaluator(new SDFProbabilisticExpression(disjunctiveSplitExpression), varHelper, SchemaUtils
                            .getAttributePos(schema, discreteProbabilisticAttributes)));
                }
            }
            else {
                for (SDFExpression disjunctiveSplitExpression : ExpressionUtils.disjunctiveSplitExpression(new SDFProbabilisticExpression(conjunctiveSplitExpression))) {
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
