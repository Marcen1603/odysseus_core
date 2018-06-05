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

import java.io.File;
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
	/**
	 * 
	 * @return
	 */
	List<SensingDevice> allSensingDevices();

	/**
	 * 
	 * @param featureOfInterest
	 * @return
	 */
	List<SensingDevice> allSensingDevices(String featureOfInterest);

	/**
	 * 
	 * @return
	 */
	List<FeatureOfInterest> allFeaturesOfInterests();

	/**
	 * 
	 * @return
	 */
	List<Property> allProperties();

	/**
	 * 
	 * @param featureOfInterest
	 * @return
	 */
	List<Property> allProperties(String featureOfInterest);

	/**
	 * @param sensingDevice
	 */
	void create(SensingDevice sensingDevice);

	/**
	 * 
	 * @param featureOfInterest
	 */
	void create(FeatureOfInterest featureOfInterest);

	/**
	 * 
	 * @param featureOfInterest
	 * @param sensingDevice
	 * @param measurementCapability
	 * @return
	 */
	List<SensingDevice> sensingDevices(final String featureOfInterest, final String sensingDevice,
			final String measurementCapability);

	/**
	 * 
	 * @param property
	 * @return
	 */
	List<String> attributes(Property property);

	/**
	 * @param measurementCapability
	 * @param condition
	 */
	void create(MeasurementCapability measurementCapability, Condition condition);

	/**
	 * @param measurementCapability
	 * @param measurementProperty
	 */
	void create(MeasurementCapability measurementCapability, MeasurementProperty measurementProperty);

	/**
	 * @param featureOfInterest
	 * @param property
	 */
	void create(FeatureOfInterest featureOfInterest, Property property);

	/**
	 * @param sensingDevice
	 * @param measurementCapability
	 */
	void create(SensingDevice sensingDevice, MeasurementCapability measurementCapability);

	/**
	 * 
	 * @param property
	 */
	void update(Property property);

	/**
	 * 
	 * @param measurementCapability
	 */
	void update(MeasurementCapability measurementCapability);

	/**
	 * 
	 * @param easurementProperty
	 */
	void update(MeasurementProperty easurementProperty);

	/**
	 * 
	 * @param featureOfInterest
	 */
	void update(FeatureOfInterest featureOfInterest);

	/**
	 * 
	 * @param sensingDevice
	 */
	void update(SensingDevice sensingDevice);

	/**
	 * 
	 * @param property
	 */
	void delete(Property property);

	/**
	 * 
	 * @param measurementCapability
	 */
	void delete(MeasurementCapability measurementCapability);

	/**
	 * 
	 * @param easurementProperty
	 */
	void delete(MeasurementProperty easurementProperty);

	/**
	 * 
	 * @param featureOfInterest
	 */
	void delete(FeatureOfInterest featureOfInterest);

	/**
	 * 
	 * @param sensingDevice
	 */
	void delete(SensingDevice sensingDevice);

	/**
	 * 
	 * @param condition
	 */
	void delete(Condition condition);

	/**
	 * 
	 */
	void clearCache();

	/**
	 * 
	 */
	void reload();

	/**
	 * 
	 * @param file
	 */
	void load(File file);
}
