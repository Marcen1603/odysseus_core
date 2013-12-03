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
public class SensingDevice {
    private final URI uri;
    private final String name;
    private final List<MeasurementCapability> hasMeasurementCapabilities = new ArrayList<MeasurementCapability>();

    /**
     * Class constructor.
     * 
     */
    public SensingDevice(final URI uri) {
        this(uri, uri.getFragment());
    }

    /**
     * Class constructor.
     * 
     */
    public SensingDevice(final URI uri, final String name) {
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
     * @return the capabilities
     */
    public List<MeasurementCapability> getHasMeasurementCapabilities() {
        return Collections.unmodifiableList(this.hasMeasurementCapabilities);
    }

    /**
     * @param attribute
     *            the attribute
     * @return the capabilities
     */
    public List<MeasurementCapability> getHasMeasurementCapabilities(final Property forProperty) {
        final List<MeasurementCapability> propertyCapabilities = new ArrayList<MeasurementCapability>();
        for (final MeasurementCapability capability : this.hasMeasurementCapabilities) {
            if (forProperty.equals(capability.getForProperty())) {
                propertyCapabilities.add(capability);
            }
        }
        return propertyCapabilities;
    }

    public List<MeasurementCapability> getHasMeasurementCapabilities(final String label) {
        final List<MeasurementCapability> propertyCapabilities = new ArrayList<MeasurementCapability>();
        for (final MeasurementCapability capability : this.hasMeasurementCapabilities) {
            if (capability.getName().equalsIgnoreCase(label)) {
                propertyCapabilities.add(capability);
            }
        }
        return propertyCapabilities;
    }

    public void addMeasurementCapability(final MeasurementCapability hasMeasurementCapability) {
        this.hasMeasurementCapabilities.add(hasMeasurementCapability);
    }

    public void removeCapability(final MeasurementCapability hasMeasurementCapability) {
        this.hasMeasurementCapabilities.remove(hasMeasurementCapability);
    }

}
