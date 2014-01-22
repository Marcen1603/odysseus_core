/**
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
package de.uniol.inf.is.odysseus.ontology.common.model.condition;

import java.net.URI;
import java.util.Objects;

import de.uniol.inf.is.odysseus.ontology.common.model.Property;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ExpressionCondition extends Condition implements ICondition {
    /** The expression. */
    private final String expression;

    /**
     * Class constructor.
     * 
     * @param uri
     *            The URI
     * @param onProperty
     *            The property
     * @param expression
     *            The exression
     */
    public ExpressionCondition(final URI uri, final Property onProperty, final String expression) {
        super(uri, onProperty);
        Objects.requireNonNull(expression);
        this.expression = expression;
    }

    /**
     * @return the expression
     */
    public String getExpression() {
        return this.expression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.getExpression();
    }
}
