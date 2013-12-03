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
package de.uniol.inf.is.odysseus.ontology.rcp.views;

import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.ontology.model.FeatureOfInterest;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.Property;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.model.property.MeasurementProperty;
import de.uniol.inf.is.odysseus.ontology.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.rcp.server.views.AbstractViewLabelProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensingDeviceContentLabelProvider extends
		AbstractViewLabelProvider {

	private final String sensingDeviceImage = "sensingDevice";
	private final String featureOfInterestImage = "featureOfInterest";
	private final String measurementCapabilityImage = "measurementCapability";
	private final String measurementPropertyImage = "measurementProperty";
	private final String conditionImage = "condition";
	private final String propertyImage = "property";

	public SensingDeviceContentLabelProvider() {

	}

	@Override
	public Image getImage(final Object element) {
		if (element instanceof SensingDevice) {
			return SensorRegistryPlugIn.getImageManager().get(
					this.sensingDeviceImage);
		} else if (element instanceof FeatureOfInterest) {
			return SensorRegistryPlugIn.getImageManager().get(
					this.featureOfInterestImage);
		} else if (element instanceof MeasurementCapability) {
			return SensorRegistryPlugIn.getImageManager().get(
					this.measurementCapabilityImage);
		} else if (element instanceof Condition) {
			return SensorRegistryPlugIn.getImageManager().get(
					this.conditionImage);
		} else if (element instanceof MeasurementProperty) {
			return SensorRegistryPlugIn.getImageManager().get(
					this.measurementPropertyImage);
		} else if (element instanceof Property) {
			return SensorRegistryPlugIn.getImageManager().get(
					this.propertyImage);
		}
		return super.getImage(element);
	}

	@Override
	public String getText(final Object element) {
		if (element instanceof SensingDevice) {
			final SensingDevice sensingDevice = (SensingDevice) element;
			final StringBuilder sb = new StringBuilder();
			sb.append(sensingDevice.getName()).append(" [ns:")
					.append(sensingDevice.getUri().getFragment()).append("]");
			return sb.toString();
		} else if (element instanceof FeatureOfInterest) {
			final FeatureOfInterest featureOfInterest = (FeatureOfInterest) element;
			final StringBuilder sb = new StringBuilder();
			sb.append(featureOfInterest.getName()).append(" [ns:")
					.append(featureOfInterest.getUri().getFragment())
					.append("]");
			return sb.toString();
		} else if (element instanceof MeasurementCapability) {
			final MeasurementCapability measurementCapability = (MeasurementCapability) element;
			final StringBuilder sb = new StringBuilder();
			sb.append(measurementCapability.getName()).append(" [ns:")
					.append(measurementCapability.getUri().getFragment())
					.append("]");
			return sb.toString();
		} else if (element instanceof Condition) {
			final Condition condition = (Condition) element;
			final StringBuilder sb = new StringBuilder();
			sb.append(
					String.format(condition.toString(), condition
							.getOnProperty().getName())).append(" [ns:")
					.append(condition.getOnProperty().getUri().getFragment())
					.append("]");
			return sb.toString();
		} else if (element instanceof MeasurementProperty) {
			final MeasurementProperty measurementProperty = (MeasurementProperty) element;
			final StringBuilder sb = new StringBuilder();
			sb.append(measurementProperty.getResource().getLocalName())
					.append(": ").append(measurementProperty.getExpression());
			return sb.toString();
		} else if (element instanceof Property) {
			final Property property = (Property) element;
			final StringBuilder sb = new StringBuilder();
			sb.append(property.getName()).append(" [ns:")
					.append(property.getUri().getFragment()).append("]");
			return sb.toString();
		}
		return super.getText(element);
	}
}
