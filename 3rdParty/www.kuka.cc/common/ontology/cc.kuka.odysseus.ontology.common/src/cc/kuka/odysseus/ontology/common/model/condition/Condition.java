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
	private final Property property;
	/** The expression. */
	private final String expression;

	/**
	 * 
	 * Class constructor.
	 *
	 * @param uri
	 * @param property
	 * @param expression
	 */
	public Condition(final URI uri, String name, final Property property, final String expression) {
		super(uri, name);
		Objects.requireNonNull(property, "Property can't be null");
		Objects.requireNonNull(expression, "Expression can't be null");
		this.property = property;
		this.expression = expression;
	}

	/**
	 * @return the onProperty
	 */
	public Property onProperty() {
		return this.property;
	}

	/**
	 * @return the expression
	 */
	public String expression() {
		return this.expression;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.expression();
	}
}
