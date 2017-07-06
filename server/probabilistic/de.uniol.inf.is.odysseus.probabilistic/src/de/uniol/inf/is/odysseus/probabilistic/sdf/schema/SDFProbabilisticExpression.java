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

import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepExpressionParser;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;

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
    public SDFProbabilisticExpression(final IMepExpression<?> expression, final IAttributeResolver attributeResolver, final IMepExpressionParser parser) {
        super(expression, attributeResolver, parser);
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
    public SDFProbabilisticExpression(final IMepExpression<?> expression, final IAttributeResolver attributeResolver, final IMepExpressionParser parser, final String string) {
        super(expression, attributeResolver, parser, string);
    }

    /**
     * Constructs a new probabilistic expression from the given expression.
     * 
     * @param expression
     *            The expression
     */
    public SDFProbabilisticExpression(final SDFExpression expression) {
        super(expression);
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
    public SDFProbabilisticExpression(final String string, final IMepExpressionParser parser) {
        super(string, parser);
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
    public SDFProbabilisticExpression(final String uri, final String string, final IAttributeResolver attributeResolver, final IMepExpressionParser parser) {
        super(uri, string, attributeResolver, parser, AggregateFunctionBuilderRegistry.getAggregatePattern());
    }

    /**
     * @param string
     * @param predicate
     * @param object
     * @param instance
     * @param aggregatePattern
     */
    public SDFProbabilisticExpression(final String uri, final String value, final IAttributeResolver attributeResolver, final IMepExpressionParser expressionParser, final Pattern aggregatePattern) {
        super(uri, value, attributeResolver, expressionParser, AggregateFunctionBuilderRegistry.getAggregatePattern());
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
