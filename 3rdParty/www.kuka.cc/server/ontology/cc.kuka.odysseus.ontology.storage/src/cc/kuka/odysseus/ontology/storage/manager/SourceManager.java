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
package cc.kuka.odysseus.ontology.storage.manager;

import cc.kuka.odysseus.ontology.common.model.FeatureOfInterest;
import cc.kuka.odysseus.ontology.common.model.MeasurementCapability;
import cc.kuka.odysseus.ontology.common.model.Property;
import cc.kuka.odysseus.ontology.common.model.SensingDevice;
import cc.kuka.odysseus.ontology.common.model.condition.Condition;
import cc.kuka.odysseus.ontology.common.model.property.MeasurementProperty;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public interface SourceManager {
    /**
     * Creates a new feature of interest with it's properties.
     *
     * @param featureOfInterest
     *            The feature of interest.
     */
    void createFeatureOfInterest(FeatureOfInterest featureOfInterest);

    /**
     * Creates a new property in the given feature of interest.
     *
     * @param featureOfInterest
     *            The feature of interest.
     * @param property
     *            The property.
     */
    void createProperty(final FeatureOfInterest featureOfInterest, final Property property);

    /**
     * Creates a new sensing device with it's measurement capabilities.
     *
     * @param sensingDevice
     *            The sensing device.
     */
    void createSensingDevice(final SensingDevice sensingDevice);

    /**
     * Creates a new sensing device with it's measurement capabilities.
     *
     * @param sensingDevice
     *            The sensing device.
     * @param measurementCapability
     *            The measurement capability.
     */
    void createMeasurementCapability(final SensingDevice sensingDevice, final MeasurementCapability measurementCapability);

    /**
     * Creates a new condition for the given measurement capability.
     *
     * @param measurementCapability
     *            The measurement capability.
     * @param condition
     *            The condition.
     */
    void createCondition(final MeasurementCapability measurementCapability, final Condition condition);

    /**
     * Creates a new measurement property for the given measurement capability.
     *
     * @param measurementCapability
     *            The measurement capability.
     * @param measurementProperty
     *            The measurement property.
     */
    void createMeasurementProperty(final MeasurementCapability measurementCapability, final MeasurementProperty measurementProperty);
}
