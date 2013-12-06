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
package de.uniol.inf.is.odysseus.ontology.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class FeatureOfInterest {
    /** The URI. */
    private final URI uri;
    /** The name. */
    private final String name;
    /** The list of properties. */
    private final List<Property> hasProperties = new ArrayList<Property>();

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
