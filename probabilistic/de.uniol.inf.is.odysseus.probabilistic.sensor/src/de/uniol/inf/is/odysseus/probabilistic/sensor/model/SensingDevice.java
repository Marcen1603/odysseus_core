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
package de.uniol.inf.is.odysseus.probabilistic.sensor.model;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensingDevice {
    private final String name;
    private final SDFSchema schema;
    private final List<MeasurementCapability> capabilities = new ArrayList<MeasurementCapability>();

    /**
     * Class constructor.
     * 
     */
    public SensingDevice(String name, SDFSchema schema) {
        this.name = name;
        this.schema = schema;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
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
    public List<MeasurementCapability> getCapabilities() {
        return this.capabilities;
    }

    public void addCapability(MeasurementCapability capability) {
        this.capabilities.add(capability);
    }

    public void removeCapability(MeasurementCapability capability) {
        this.capabilities.remove(capability);
    }
}
