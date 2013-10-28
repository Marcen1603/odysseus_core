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
package de.uniol.inf.is.odysseus.ontology.manager;

import java.net.URI;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public interface QueryManager {
    /**
     * Gets all sensing devices from the ontology.
     * 
     * @return A list of URIs
     */
    List<URI> getAllSensingDeviceURIs();

    List<SensingDevice> getAllSensingDevices();

    SensingDevice getSensingDevice(URI uri);

    /**
     * Gets all sensing devices from the ontology.
     * 
     * @return A list of URIs
     */
    List<URI> getAllPropertyURIs();

    /**
     * Gets all sensing devices from the ontology that observe the given
     * property.
     * 
     * @param uri
     *            The URI of the property
     * @return A list of URIs
     */
    List<URI> getSensingDeviceURIsByObservedProperty(URI uri);

    /**
     * Gets all sensing devices from the ontology that observe the given
     * property.
     * 
     * @param uri
     *            The URI of the property
     * @return A list of URIs
     */
    List<SensingDevice> getSensingDevicesByObservedProperty(URI uri);

    List<SensingDevice> getSensingDevicesByObservedProperty(SDFAttribute attribute);

    /**
     * Gets all properties from the ontology that are observed by the given
     * sensing device.
     * 
     * @return A list of URIs
     */
    List<URI> getAllPropertyURIsObservedBySensingDevice(URI uri);

}
