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

package de.uniol.inf.is.odysseus.probabilistic.sdf.schema;

import java.util.List;
import java.util.Objects;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IExpressionParser;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;

/**
 * Probabilistic expression that have acces from the payload to the multivariate
 * distributions.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class SDFProbabilisticExpression extends SDFExpression {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2862460974715363031L;
    /** The Gaussion Mixture Models. */
    private List<MultivariateMixtureDistribution> distributions;
    private List<Integer> positions;

    /**
     * Constructs a new probabilistic expression from the given expression using
     * the given attribute resolver and expression parser.
     * 
     * @param expression
     *            The expression
     * @param attributeResolver
     *            The attribute resolver
     * @param parser
     *            The expression parser, i.e., MEP
     */
    public SDFProbabilisticExpression(final IExpression<?> expression, final IAttributeResolver attributeResolver, final IExpressionParser parser) {
        super(expression, attributeResolver, parser);
        this.init(expression, null, attributeResolver, parser);
    }

    /**
     * Constructs a new probabilistic expression from the given expression using
     * the given attribute resolver, expression parser, and expression string.
     * 
     * @param expression
     *            The expression
     * @param attributeResolver
     *            The attribute resolver
     * @param parser
     *            The expression parser, i.e., MEP
     * @param string
     *            The expression string
     */
    public SDFProbabilisticExpression(final IExpression<?> expression, final IAttributeResolver attributeResolver, final IExpressionParser parser, final String string) {
        super(expression, attributeResolver, parser, string);
        this.init(expression, null, attributeResolver, parser);
    }

    /**
     * Constructs a new probabilistic expression from the given expression.
     * 
     * @param expression
     *            The expression
     */
    public SDFProbabilisticExpression(final SDFExpression expression) {
        super(expression);
        this.init(expression.getMEPExpression(), expression.getExpressionString(), expression.getAttributeResolver(), expression.getExpressionParser());
    }

    /**
     * Constructs a new probabilistic expression from the given expression
     * string using the given expression parser.
     * 
     * @param string
     *            The expression string
     * @param parser
     *            The expression parser, i.e., MEP
     */
    public SDFProbabilisticExpression(final String string, final IExpressionParser parser) {
        super(string, parser);
        this.init(null, string, null, parser);
    }

    /**
     * 
     * @param uri
     *            The URI
     * @param string
     *            The expression string
     * @param attributeResolver
     *            The attribute resolver
     * @param parser
     *            The expression parser, i.e., MEP
     */
    public SDFProbabilisticExpression(final String uri, final String string, final IAttributeResolver attributeResolver, final IExpressionParser parser) {
        super(uri, string, attributeResolver, parser, AggregateFunctionBuilderRegistry.getAggregatePattern());
        this.init(null, string, attributeResolver, parser);
    }

    /**
     * 
     * @param expression
     *            The expression
     * @param string
     *            The expression string
     * @param attributeResolver
     *            The attribute resolver
     * @param parser
     *            The expression parser, i.e., MEP
     */
    private void init(final IExpression<?> expression, final String string, final IAttributeResolver attributeResolver, final IExpressionParser parser) {
        if (this.getMEPExpression() instanceof AbstractProbabilisticFunction) {
            final AbstractProbabilisticFunction<?> probabilisticExpression = ((AbstractProbabilisticFunction<?>) this.getMEPExpression());
            probabilisticExpression.propagateDistributionReference(probabilisticExpression.getDistributions());
            probabilisticExpression.propagatePositionReference(probabilisticExpression.getPositions());
            this.setDistributions(probabilisticExpression.getDistributions());
            this.setPositions(probabilisticExpression.getPositions());
        }
    }

    /**
     * Bind the given distributions to the list of available distributions.
     * 
     * @param newDistributions
     *            The distributions
     */
    public final void bindDistributions(final List<MultivariateMixtureDistribution> newDistributions) {
        if ((this.getMEPExpression() instanceof Constant) || (this.getMEPExpression() instanceof Variable)) {
            return;
        }
        Objects.requireNonNull(newDistributions);
        this.distributions.clear();
        this.distributions.addAll(newDistributions);
    }

    /**
     * Sets the distributions.
     * 
     * @param distributions
     *            The distributions
     */
    private void setDistributions(final List<MultivariateMixtureDistribution> distributions) {
        Objects.requireNonNull(distributions);
        this.distributions = distributions;
    }

    /**
     * @param pos
     */
    public void bindPositions(List<Integer> positions) {
        if ((this.getMEPExpression() instanceof Constant) || (this.getMEPExpression() instanceof Variable)) {
            return;
        }
        Objects.requireNonNull(positions);
        this.positions.clear();
        this.positions.addAll(positions);
    }

    private void setPositions(final List<Integer> positions) {
        Objects.requireNonNull(positions);
        this.positions = positions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.
     * SDFExpression#clone()
     */
    @Override
    public final SDFProbabilisticExpression clone() {
        return new SDFProbabilisticExpression(this);
    }

}
