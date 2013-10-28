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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensingDevice {
    private final URI uri;
    private final SDFSchema schema;
    private final Map<SDFAttribute, List<MeasurementCapability>> capabilities = new HashMap<SDFAttribute, List<MeasurementCapability>>();

    /**
     * Class constructor.
     * 
     */
    public SensingDevice(final URI uri, final SDFSchema schema) {
        this.uri = uri;
        this.schema = schema.clone();
        for (SDFAttribute attribute : this.schema.getAttributes()) {
            this.capabilities.put(attribute, new ArrayList<MeasurementCapability>());
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.uri.getFragment();
    }

    /**
     * @return the uri
     */
    public URI getUri() {
        return this.uri;
    }

    /**
     * @return the schema
     */
    public SDFSchema getSchema() {
        return this.schema;
    }

    /**
     * @return the capabilities
     */
    public Map<SDFAttribute, List<MeasurementCapability>> getCapabilities() {
        return this.capabilities;
    }

    /**
     * @param attribute
     *            the attribute
     * @return the capabilities
     */
    public List<MeasurementCapability> getCapabilities(final SDFAttribute attribute) {
        return this.capabilities.get(attribute);
    }

    /**
     * @return
     */
    public List<MeasurementCapability> getAllCapabilities() {
        final List<MeasurementCapability> capabilities = new ArrayList<MeasurementCapability>();
        for (final List<MeasurementCapability> attributeCapability : this.capabilities.values()) {
            capabilities.addAll(attributeCapability);
        }
        return capabilities;
    }

    public void addMeasurementCapability(final MeasurementCapability capability) {
        if (!this.capabilities.containsKey(capability.getAttribute())) {
            this.capabilities.put(capability.getAttribute(), new ArrayList<MeasurementCapability>());
        }
        this.capabilities.get(capability.getAttribute()).add(capability);
    }

    public void removeCapability(final MeasurementCapability capability) {
        this.capabilities.get(capability.getAttribute()).remove(capability);
    }

}
