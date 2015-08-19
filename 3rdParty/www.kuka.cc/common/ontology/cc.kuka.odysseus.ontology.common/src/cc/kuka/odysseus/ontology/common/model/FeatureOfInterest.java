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
import java.util.Collections;
import java.util.List;

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
    private final List<Property> hasProperties = new ArrayList<>();

    /**
     * Class constructor..
     *
     * @param uri
     *            The URI
     */
    public FeatureOfInterest(final URI uri) {
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
    public FeatureOfInterest(final URI uri, final String name) {
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
    public FeatureOfInterest(final URI uri, final List<Property> hasProperties) {
        this(uri, uri.getFragment(), hasProperties);
    }

    /**
     * Class constructor.
     *
     * @param uri
     *            The URI
     * @param name
     *            The name
     * @param hasProperties
     *            The list of properties
     */
    public FeatureOfInterest(final URI uri, final String name, final List<Property> hasProperties) {
        this.uri = uri;
        this.name = name;
        this.hasProperties.addAll(hasProperties);
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the uri
     */
    public URI getUri() {
        return this.uri;
    }

    /**
     * @return the hasProperties
     */
    public List<Property> getHasProperties() {
        return Collections.unmodifiableList(this.hasProperties);
    }

    /**
     * Adds a new property.
     *
     * @param hasProperty
     *            The property
     */
    public void addProperty(final Property hasProperty) {
        this.hasProperties.add(hasProperty);
    }

    /**
     * Removes a property.
     *
     * @param hasProperty
     *            The property
     */
    public void removeProperty(final Property hasProperty) {
        this.hasProperties.remove(hasProperty);
    }

}
