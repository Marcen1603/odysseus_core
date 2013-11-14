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
package de.uniol.inf.is.odysseus.ontology.model.condition;

import java.net.URI;

import de.uniol.inf.is.odysseus.ontology.model.Property;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ExpressionCondition extends Condition implements ICondition {
    private final String expression;

    /**
     * Class constructor.
     * 
     * @param uri
     */
    public ExpressionCondition(URI uri, Property onProperty, String expression) {
        super(uri, onProperty);
        this.expression = expression;
    }

    /**
     * @return the expression
     */
    public String getExpression() {
        return this.expression;
    }

}
