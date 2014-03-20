/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.base.predicate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.probabilistic.base.common.ProbabilisticBooleanResult;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilisticTimeInterval;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.AbstractRelationalPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticRelationalPredicate extends AbstractRelationalPredicate<ProbabilisticTuple<?>> implements IProbabilisticRelationalPredicate<ProbabilisticTuple<?>> {
    private static final long serialVersionUID = 1222104352250883947L;
    Logger LOG = LoggerFactory.getLogger(ProbabilisticRelationalPredicate.class);

    public ProbabilisticRelationalPredicate(SDFExpression expression) {
        super(new SDFProbabilisticExpression(expression));
    }

    public ProbabilisticRelationalPredicate(SDFProbabilisticExpression expression) {
        super(expression);
    }

    public ProbabilisticRelationalPredicate(ProbabilisticRelationalPredicate predicate) {
        super(predicate);
    }

    /**
     * @param pred
     */
    public ProbabilisticRelationalPredicate(AbstractRelationalPredicate<ProbabilisticTuple<?>> predicate) {
        super(predicate);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List<IPredicate> splitPredicate() {
        List<IPredicate> result = new LinkedList<IPredicate>();
        if (isAndPredicate()) {
            Stack<IExpression<?>> expressionStack = new Stack<IExpression<?>>();
            expressionStack.push(expression.getMEPExpression());

            while (!expressionStack.isEmpty()) {
                IExpression<?> curExpression = expressionStack.pop();
                if (isAndExpression(curExpression)) {
                    expressionStack.push(curExpression.toFunction().getArgument(0));
                    expressionStack.push(curExpression.toFunction().getArgument(1));
                }
                else {
                    SDFExpression expr = new SDFExpression(curExpression, expression.getAttributeResolver(), MEP.getInstance());
                    RelationalPredicate relationalPredicate = new RelationalPredicate(expr);
                    relationalPredicate.init(expression.getSchema(), false);
                    result.add(relationalPredicate);
                }
            }
            return result;

        }
        result.add(this);
        return result;
    }

    @Override
    public boolean evaluate(ProbabilisticTuple<?> input) {
        Object[] values = new Object[this.attributePositions.length];
        IMetaAttribute[] meta = new IMetaAttribute[this.attributePositions.length];
        for (int i = 0; i < values.length; ++i) {
            Object attribute = input.getAttribute(this.attributePositions[i]);
            if (attribute.getClass() == ProbabilisticDouble.class) {
                final int index = ((ProbabilisticDouble) attribute).getDistribution();
                attribute = input.getDistribution(index);
            }
            values[i] = attribute;
            meta[i] = input.getMetadata();
        }
        final SDFProbabilisticExpression probabilisticExpression = (SDFProbabilisticExpression) this.expression;

        probabilisticExpression.bindMetaAttribute(input.getMetadata());
        probabilisticExpression.bindDistributions(Arrays.asList(input.getDistributions()));
        probabilisticExpression.bindAdditionalContent(input.getAdditionalContent());
        probabilisticExpression.bindVariables(this.attributePositions, meta, values);

        final Object expr = probabilisticExpression.getValue();
        if (probabilisticExpression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_RESULT)) {
            final ProbabilisticBooleanResult result = (ProbabilisticBooleanResult) expr;
            return result.getProbability() != 0.0;
        }
        else {
            return (Boolean) expr;
        }
    }

    public ProbabilisticTuple<?> probabilisticEvaluate(ProbabilisticTuple<?> input) {
        @SuppressWarnings("unchecked")
        ProbabilisticTuple<IProbabilistic> output = (ProbabilisticTuple<IProbabilistic>) input.clone();

        Object[] values = new Object[this.attributePositions.length];
        IMetaAttribute[] meta = new IMetaAttribute[this.attributePositions.length];
        for (int i = 0; i < values.length; ++i) {
            Object attribute = input.getAttribute(this.attributePositions[i]);
            if (attribute.getClass() == ProbabilisticDouble.class) {
                final int index = ((ProbabilisticDouble) attribute).getDistribution();
                attribute = input.getDistribution(index);
            }
            values[i] = attribute;
            meta[i] = input.getMetadata();
        }
        final SDFProbabilisticExpression probabilisticExpression = (SDFProbabilisticExpression) this.expression;

        probabilisticExpression.bindMetaAttribute(input.getMetadata());
        probabilisticExpression.bindDistributions(Arrays.asList(input.getDistributions()));
        probabilisticExpression.bindAdditionalContent(input.getAdditionalContent());
        probabilisticExpression.bindVariables(this.attributePositions, meta, values);

        final Object expr = probabilisticExpression.getValue();
        if (expression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_RESULT)) {
            final ProbabilisticBooleanResult result = (ProbabilisticBooleanResult) expr;
            for (final IMultivariateDistribution distr : result.getDistributions()) {
                final MultivariateMixtureDistribution distribution = (MultivariateMixtureDistribution) distr;
                final int index = ((ProbabilisticDouble) input.getAttribute(distribution.getAttribute(0))).getDistribution();
                output.setDistribution(index, distribution);
            }
            output.getMetadata().setExistence(result.getProbability());
            return output;
        }
        return null;
    }

    @Override
    public boolean evaluate(ProbabilisticTuple<?> left, ProbabilisticTuple<?> right) {
        // FIXME 20140319 christian@kuka.cc Restrict tuple before concatenate
        // them
        int distributionsLength = 0;
        int attributesLength = 0;
        if (left != null) {
            distributionsLength = left.getDistributions().length;
            attributesLength = left.getAttributes().length;
        }
        final MultivariateMixtureDistribution[] newDistributions = this.mergeDistributions(left, right, attributesLength);
        final Object[] newAttributes = this.mergeAttributes(left, right, distributionsLength);

        Object[] values = new Object[this.attributePositions.length];
        IMetaAttribute[] meta = new IMetaAttribute[values.length];
        int[] positions = new int[values.length];
        for (int i = 0; i < values.length; ++i) {
            int pos = fromRightChannel[i] ? left.getAttributes().length + this.attributePositions[i] : this.attributePositions[i];
            Object attribute = newAttributes[pos];
            if (attribute.getClass() == ProbabilisticDouble.class) {
                final int index = ((ProbabilisticDouble) attribute).getDistribution();
                attribute = newDistributions[index];
            }
            values[i] = attribute;
            meta[i] = fromRightChannel[i] ? left.getMetadata() : right.getMetadata();
            positions[i] = pos;
        }

        Map<String, Serializable> additionalContent = new HashMap<String, Serializable>();
        additionalContent.putAll(left.getAdditionalContent());
        additionalContent.putAll(right.getAdditionalContent());

        final SDFProbabilisticExpression probabilisticExpression = (SDFProbabilisticExpression) this.expression;
        probabilisticExpression.bindDistributions(Arrays.asList(newDistributions));
        probabilisticExpression.bindAdditionalContent(additionalContent);
        probabilisticExpression.bindVariables(positions, meta, values);
        final Object expr = probabilisticExpression.getValue();
        if (probabilisticExpression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_RESULT)) {
            final ProbabilisticBooleanResult result = (ProbabilisticBooleanResult) expr;
            return result.getProbability() != 0.0;
        }
        else {
            return (Boolean) expr;
        }
    }

    public ProbabilisticTuple<?> probabilisticEvaluate(ProbabilisticTuple<?> left, ProbabilisticTuple<?> right, IProbabilisticTimeInterval metadata) {
        // FIXME 20140319 christian@kuka.cc Restrict tuple before concatenate
        // them
        int distributionsLength = 0;
        int attributesLength = 0;
        if (left != null) {
            distributionsLength = left.getDistributions().length;
            attributesLength = left.getAttributes().length;
        }
        final MultivariateMixtureDistribution[] newDistributions = this.mergeDistributions(left, right, attributesLength);
        final Object[] newAttributes = this.mergeAttributes(left, right, distributionsLength);

        Object[] values = new Object[this.attributePositions.length];
        IMetaAttribute[] meta = new IMetaAttribute[values.length];
        int[] positions = new int[values.length];
        for (int i = 0; i < values.length; ++i) {
            int pos = fromRightChannel[i] ? left.getAttributes().length + this.attributePositions[i] : this.attributePositions[i];
            Object attribute = newAttributes[pos];
            if (attribute.getClass() == ProbabilisticDouble.class) {
                final int index = ((ProbabilisticDouble) attribute).getDistribution();
                attribute = newDistributions[index];
            }
            values[i] = attribute;
            meta[i] = fromRightChannel[i] ? left.getMetadata() : right.getMetadata();
            positions[i] = pos;
        }
        Map<String, Serializable> additionalContent = new HashMap<String, Serializable>();
        additionalContent.putAll(left.getAdditionalContent());
        additionalContent.putAll(right.getAdditionalContent());

        final SDFProbabilisticExpression probabilisticExpression = (SDFProbabilisticExpression) this.expression;

        probabilisticExpression.bindDistributions(Arrays.asList(newDistributions));
        probabilisticExpression.bindAdditionalContent(additionalContent);
        probabilisticExpression.bindVariables(positions, meta, values);

        final Object expr = probabilisticExpression.getValue();
        if (expression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_RESULT)) {
            final ProbabilisticBooleanResult result = (ProbabilisticBooleanResult) expr;
            for (final IMultivariateDistribution distr : result.getDistributions()) {
                final MultivariateMixtureDistribution distribution = (MultivariateMixtureDistribution) distr;
                final int index = ((ProbabilisticDouble) newAttributes[distribution.getAttribute(0)]).getDistribution();
                newDistributions[index] = distribution;
            }
            ProbabilisticTuple<IProbabilistic> outputVal = new ProbabilisticTuple<>(newAttributes, newDistributions, true);
            outputVal.setMetadata(metadata.clone());
            outputVal.getMetadata().setExistence(outputVal.getMetadata().getExistence() * result.getProbability());
            return outputVal;
        }
        return null;
    }

    public boolean evaluate(ProbabilisticTuple<?> input, KeyValueObject<?> additional) {
        Object[] values = new Object[neededAttributes.size()];

        for (int i = 0; i < neededAttributes.size(); ++i) {
            if (!fromRightChannel[i]) {
                values[i] = input.getAttribute(this.attributePositions[i]);
            }
            else {
                values[i] = additional.getAttribute(neededAttributes.get(i).getURI());
            }
        }
        this.expression.bindMetaAttribute(input.getMetadata());
        this.expression.bindAdditionalContent(input.getAdditionalContent());
        this.expression.bindVariables(values);
        return (Boolean) this.expression.getValue();
    }

    @Override
    public ProbabilisticRelationalPredicate clone() {
        return new ProbabilisticRelationalPredicate(this);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ProbabilisticRelationalPredicate)) {
            return false;
        }
        return this.expression.equals(((ProbabilisticRelationalPredicate) other).expression);
    }

    @Override
    public int hashCode() {
        return 23 * this.expression.hashCode();
    }

    private MultivariateMixtureDistribution[] mergeDistributions(final ProbabilisticTuple<?> left, final ProbabilisticTuple<?> right, final int offset) {
        int length = 0;
        int start = 0;
        if (left.getDistributions() != null) {
            length += left.getDistributions().length;
            start = left.getDistributions().length;
        }
        if (right.getDistributions() != null) {
            length += right.getDistributions().length;
        }
        final MultivariateMixtureDistribution[] newDistributions = new MultivariateMixtureDistribution[length];
        if (left.getDistributions() != null) {
            for (int i = 0; i < left.getDistributions().length; i++) {
                newDistributions[i] = left.getDistributions()[i].clone();
            }
            // System.arraycopy(leftDistributions, 0, newDistributions, 0,
            // leftDistributions.length);
        }
        if (right.getDistributions() != null) {
            for (int i = 0; i < right.getDistributions().length; i++) {
                newDistributions[left.getDistributions().length + i] = right.getDistributions()[i].clone();
            }
            // System.arraycopy(rightDistributions, 0, newDistributions,
            // leftDistributions.length, rightDistributions.length);
        }
        for (int i = start; i < length; i++) {
            final int dimension = newDistributions[i].getDimension();

            final int[] newAttrsPos = new int[dimension];
            for (int j = 0; j < dimension; j++) {
                newAttrsPos[j] = newDistributions[i].getAttribute(j) + offset;
            }
            newDistributions[i].setAttributes(newAttrsPos);
        }
        return newDistributions;
    }

    private Object[] mergeAttributes(ProbabilisticTuple<?> left, ProbabilisticTuple<?> right, int offset) {
        int length = 0;
        int start = 0;
        if (left.getAttributes() != null) {
            length += left.getAttributes().length;
            start = left.getAttributes().length;
        }
        if (right.getAttributes() != null) {
            length += right.getAttributes().length;
        }
        final Object[] newAttributes = new Object[length];
        if (left.getAttributes() != null) {
            System.arraycopy(left.getAttributes(), 0, newAttributes, 0, left.getAttributes().length);
        }
        if (right.getAttributes() != null) {
            System.arraycopy(right.getAttributes(), 0, newAttributes, left.getAttributes().length, right.getAttributes().length);
        }
        for (int i = start; i < length; i++) {
            if (newAttributes[i].getClass() == ProbabilisticDouble.class) {
                final ProbabilisticDouble value = ((ProbabilisticDouble) newAttributes[i]);
                newAttributes[i] = new ProbabilisticDouble(value.getDistribution() + offset);
            }
        }
        return newAttributes;
    }

    public static void main(String[] args) {
        SDFAttribute a = new SDFAttribute("", "p_out", SDFDatatype.DOUBLE, null, null, null);
        SDFSchema schema = new SDFSchema("", Tuple.class, a);
        RelationalPredicate pred = new RelationalPredicate(new SDFExpression("p_out <=0 || isNaN(p_out)", MEP.getInstance()));

        System.out.println(pred.toString());
        pred.init(schema, null, false);
        Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
        tuple.setAttribute(0, 8);
        KeyValueObject<IMetaAttribute> additional = new KeyValueObject<IMetaAttribute>();
        additional.setAttribute("b", 5);
        System.out.println(pred.evaluate(tuple, additional));
    }

}
