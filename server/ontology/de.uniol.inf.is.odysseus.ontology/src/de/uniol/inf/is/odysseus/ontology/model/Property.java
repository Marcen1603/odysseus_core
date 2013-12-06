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

/**
 * @author Christian Kuka <christian@kuka.cc>
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
        this.uri = uri;
        this.name = name;
    }

    /**
     * @return the uri
     */
    public URI getUri() {
        return this.uri;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return this.unit;
    }

    /**
     * @param unit
     *            the unit to set
     */
    public void setUnit(final String unit) {
        this.unit = unit;
    }

}
