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
package de.uniol.inf.is.odysseus.ontology.common;

import java.net.URI;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public final class SDFUtils {
    public static URI getFeatureOfInterestURI(SDFAttribute attribute) {
        String sourceName = attribute.getSourceName();
        if (sourceName != null) {
            String[] sourceArray = sourceName.split(":");
            if (sourceArray.length >= 2) {
                return URI.create(OntologyConstants.FEATURE_OF_INTEREST_NS + "/" + sourceArray[0]);
            }
        }
        return null;
    }

    public static URI getSensingDeviceURI(SDFAttribute attribute) {
        String sourceName = attribute.getSourceName();
        if (sourceName != null) {
            String[] sourceArray = sourceName.split(":");
            if (sourceArray.length >= 2) {
                return URI.create(OntologyConstants.SENSING_DEVICE_NS + "/" + sourceArray[1]);
            }
        }
        return null;
    }

    public static URI getPropertyURI(SDFAttribute attribute) {
        String attributeName = attribute.getAttributeName();
        return URI.create(OntologyConstants.PROPERTY_NS + "/" + attributeName);
    }

    private SDFUtils() {
    }
}
