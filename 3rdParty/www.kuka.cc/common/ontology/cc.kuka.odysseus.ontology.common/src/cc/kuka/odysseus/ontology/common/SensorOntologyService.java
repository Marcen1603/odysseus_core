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
package cc.kuka.odysseus.ontology.common;

import java.util.List;

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
public interface SensorOntologyService {
    List<SensingDevice> getAllSensingDevices();

    List<SensingDevice> getAllSensingDevices(String featureOfInterest);

    List<FeatureOfInterest> getAllFeaturesOfInterest();

    List<Property> getAllProperties();

    List<Property> getAllProperties(String featureOfInterest);

    /**
     * @param sensingDevice
     */
    void createSensingDevice(SensingDevice sensingDevice);

    void createFeatureOfInterest(FeatureOfInterest featureOfInterest);


    List<SensingDevice> getSensingDevices(final String featureOfInterest, final String sensingDevice, final String measurementCapability);

    List<String> getAttributes(Property property);

    /**
     * @param measurementCapability
     * @param condition
     */
    void createCondition(MeasurementCapability measurementCapability, Condition condition);

    /**
     * @param measurementCapability
     * @param measurementProperty
     */
    void createMeasurementProperty(MeasurementCapability measurementCapability, MeasurementProperty measurementProperty);

    /**
     * @param featureOfInterest
     * @param property
     */
    void createProperty(FeatureOfInterest featureOfInterest, Property property);

    /**
     * @param sensingDevice
     * @param measurementCapability
     */
    void createMeasurementCapability(SensingDevice sensingDevice, MeasurementCapability measurementCapability);

    void clearCache();
}
