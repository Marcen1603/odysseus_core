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
package de.uniol.inf.is.odysseus.ontology.utils;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public final class SDFUtils {
    public static String getFeatureOfInterestLabel(final SDFAttribute attribute) {
        Objects.requireNonNull(attribute);
        final String sourceName = attribute.getSourceName();
        if (sourceName != null) {
            final String[] sourceArray = sourceName.split(":");
            if (sourceArray.length >= 2) {
                final int pos = sourceArray[0].indexOf(".");
                if (pos > 0) {
                    return sourceArray[0].substring(pos + 1, sourceArray[0].length());
                }
                else {
                    return sourceArray[0];
                }
            }
        }
        return null;
    }

    public static String getSensingDeviceLabel(final SDFAttribute attribute) {
        Objects.requireNonNull(attribute);
        final String sourceName = attribute.getSourceName();
        if (sourceName != null) {
            final String[] sourceArray = sourceName.split(":");
            if (sourceArray.length >= 2) {
                return sourceArray[1];
            }
        }
        return null;
    }

    private SDFUtils() {
        throw new UnsupportedOperationException();
    }
}
