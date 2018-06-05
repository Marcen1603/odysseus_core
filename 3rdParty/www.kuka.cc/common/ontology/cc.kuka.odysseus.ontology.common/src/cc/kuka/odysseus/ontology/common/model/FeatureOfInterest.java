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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.sun.istack.internal.NotNull;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class FeatureOfInterest {
	/** The URI. */
	private final URI uri;
	/** The name. */
	private final String name;
	/** The list of properties. */
	private final List<Property> properties = new ArrayList<>();

	/**
	 * Class constructor..
	 *
	 * @param uri
	 *            The URI
	 */
	public FeatureOfInterest(@NotNull final URI uri) {
		this(uri, new ArrayList<Property>());
	}

	/**
	 * Class constructor.
	 *
	 * @param uri
	 *            The URI
	 * @param name
	 *            The name
	 */
	public FeatureOfInterest(@NotNull final URI uri, @NotNull final String name) {
		this(uri, name, new ArrayList<Property>());
	}

	/**
	 * Class constructor.
	 *
	 * @param uri
	 *            The URI
	 * @param hasProperties
	 *            The list of properties
	 */
	public FeatureOfInterest(@NotNull final URI uri, @NotNull final List<Property> hasProperties) {
		this(uri, uri.getFragment(), hasProperties);
	}

	/**
	 * Class constructor.
	 *
	 * @param uri
	 *            The URI
	 * @param name
	 *            The name
	 * @param properties
	 *            The list of properties
	 */
	public FeatureOfInterest(@NotNull final URI uri, @NotNull final String name,
			@NotNull final Collection<Property> properties) {
		Objects.requireNonNull(uri, "URI can't be null");
		Objects.requireNonNull(name, "Name can't be null");
		Objects.requireNonNull(properties, "Collection of properties can't be null");
		this.uri = uri;
		this.name = name;
		this.properties.addAll(properties);
	}

	/**
	 * @return the name
	 */
	@NotNull
	public String name() {
		return this.name;
	}

	/**
	 * @return the uri
	 */
	public URI uri() {
		return this.uri;
	}

	/**
	 * @return the hasProperties
	 */
	public List<Property> hasProperties() {
		return Collections.unmodifiableList(this.properties);
	}

	/**
	 * Adds a new property.
	 *
	 * @param property
	 *            The property
	 */
	public FeatureOfInterest add(final Property property) {
		this.properties.add(property);
		return this;
	}

	/**
	 * Removes a property.
	 *
	 * @param property
	 *            The property
	 */
	public FeatureOfInterest remove(final Property property) {
		this.properties.remove(property);
		return this;
	}

}
