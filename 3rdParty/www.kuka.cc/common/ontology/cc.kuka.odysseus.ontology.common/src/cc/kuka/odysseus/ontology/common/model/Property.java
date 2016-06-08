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
package cc.kuka.odysseus.ontology.common.model;

import java.net.URI;
import java.util.Objects;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class Property {
	/** The URI. */
	private final URI uri;
	/** The name. */
	private final String name;
	/** The unit. */
	private String unit;

	/**
	 * Class constructor.
	 *
	 * @param uri
	 *            The URI
	 */
	public Property(final URI uri) {
		this(uri, uri.getFragment());
	}

	/**
	 * Class constructor.
	 *
	 * @param uri
	 *            The URI
	 * @param name
	 *            The name
	 */
	public Property(final URI uri, final String name) {
		Objects.requireNonNull(uri, "URI can't be null");
		Objects.requireNonNull(name, "Name can't be null");
		this.uri = uri;
		this.name = name;
	}

	/**
	 * @return the uri
	 */
	public URI uri() {
		return this.uri;
	}

	/**
	 * @return the name
	 */
	public String name() {
		return this.name;
	}

	/**
	 * @return the unit
	 */
	public String unit() {
		return this.unit;
	}

	/**
	 * @param unit
	 *            the unit to set
	 */
	public Property unit(final String unit) {
		this.unit = unit;
		return this;
	}

}
