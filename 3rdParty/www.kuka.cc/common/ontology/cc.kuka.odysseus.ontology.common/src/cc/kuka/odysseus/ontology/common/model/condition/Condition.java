/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.ontology.common.model.condition;

import java.net.URI;
import java.util.Objects;

import cc.kuka.odysseus.ontology.common.model.Property;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class Condition extends Property implements ICondition {
    /** The property. */
    private final Property onProperty;
    /** The expression. */
    private final String expression;

    /**
     * 
     * Class constructor.
     *
     * @param uri
     * @param onProperty
     * @param expression
     */
    public Condition(final URI uri, String name, final Property onProperty, final String expression) {
        super(uri);
        Objects.requireNonNull(onProperty);
        Objects.requireNonNull(expression);

        this.onProperty = onProperty;
        this.expression = expression;
    }

    /**
     * @return the onProperty
     */
    public Property getOnProperty() {
        return this.onProperty;
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
