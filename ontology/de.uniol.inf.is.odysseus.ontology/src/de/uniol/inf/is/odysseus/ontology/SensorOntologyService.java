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
package de.uniol.inf.is.odysseus.ontology;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.ontology.model.FeatureOfInterest;
import de.uniol.inf.is.odysseus.ontology.model.Property;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.ontology.SensorOntologyServiceImpl;

/**
 * @author Christian Kuka <christian@kuka.cc>
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

	List<SensingDevice> getSensingDevices(SDFAttribute attribute);

	List<SDFAttribute> getAttributes(Property property);

}
