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
public class SensingDevice {
    private final URI uri;
    private final String name;
    private final List<MeasurementCapability> hasMeasurementCapabilities = new ArrayList<>();

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
     * @param forProperty
     *            the property
     * @return the capabilities
     */
    public List<MeasurementCapability> getHasMeasurementCapabilities(final Property forProperty) {
        final List<MeasurementCapability> propertyCapabilities = new ArrayList<>();
        for (final MeasurementCapability capability : this.hasMeasurementCapabilities) {
            if (forProperty.equals(capability.getForProperty())) {
                propertyCapabilities.add(capability);
            }
        }
        return propertyCapabilities;
    }

    public List<MeasurementCapability> getHasMeasurementCapabilities(final String label) {
        final List<MeasurementCapability> propertyCapabilities = new ArrayList<>();
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
