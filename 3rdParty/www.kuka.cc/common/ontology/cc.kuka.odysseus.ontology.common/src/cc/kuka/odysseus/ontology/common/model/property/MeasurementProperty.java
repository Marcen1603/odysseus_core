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
package cc.kuka.odysseus.ontology.common.model.property;

import java.net.URI;
import java.util.Objects;

import cc.kuka.odysseus.ontology.common.model.Property;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class MeasurementProperty extends Property implements IMeasurementProperty {
	/** The unit. */
	private String unit;
	/** The expression of the property. */
	private final String expression;
	/** The SSN resource. */
	private final String resource;

	/**
	 * Class constructor.
	 *
	 * @param uri
	 *            The URI
	 * @param name
	 *            The name
	 * @param resource
	 *            the SSN resource
	 * @param expression
	 *            The expression
	 *
	 */
	public MeasurementProperty(final URI uri, final String name, final String resource, final String expression) {
		super(uri, name);
		Objects.requireNonNull(resource);
		Objects.requireNonNull(expression);
		this.resource = resource;
		this.expression = expression;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	@Override
	public MeasurementProperty unit(final String unit) {
		this.unit = unit;
		return this;
	}

	/**
	 * @return the unit
	 */
	@Override
	public String unit() {
		return this.unit;
	}

	/**
	 * @return the resource
	 */
	public String resource() {
		return this.resource;
	}

	/**
	 * @return the expression
	 */
	public String expression() {
		return this.expression;
	}

	@Override
	public String toString() {
		return this.expression();
	}
}
