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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.uniol.inf.is.odysseus.ontology.model.FeatureOfInterest;
import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensingDeviceContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(final Viewer viewer, final Object oldInput,
			final Object newInput) {
	}

	@Override
	public Object[] getElements(final Object inputElement) {
		return this.getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(final Object parentElement) {
		if (parentElement instanceof Collection) {
			return ((Collection<?>) parentElement).toArray();
		} else if (parentElement instanceof SensingDevice) {
			final SensingDevice sensingDevice = (SensingDevice) parentElement;
			return sensingDevice.getHasMeasurementCapabilities().toArray();
		} else if (parentElement instanceof FeatureOfInterest) {
			final FeatureOfInterest featureOfInterest = (FeatureOfInterest) parentElement;
			return featureOfInterest.getHasProperties().toArray();
		} else if (parentElement instanceof MeasurementCapability) {
			final MeasurementCapability measurementCapability = (MeasurementCapability) parentElement;
			List<Object> children = new ArrayList<Object>();
			if (measurementCapability.getInConditions() != null) {
				children.addAll(measurementCapability.getInConditions());
			}
			if (measurementCapability.getHasMeasurementProperties() != null) {
				children.addAll(measurementCapability
						.getHasMeasurementProperties());
			}
			return children.toArray();
		}
		return null;
	}

	@Override
	public Object getParent(final Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(final Object element) {
		if (element instanceof SensingDevice) {
			return !((SensingDevice) element).getHasMeasurementCapabilities()
					.isEmpty();
		} else if (element instanceof FeatureOfInterest) {
			return !((FeatureOfInterest) element).getHasProperties().isEmpty();
		} else if (element instanceof MeasurementCapability) {
			boolean hasChildren = false;
			MeasurementCapability measurementCapability = (MeasurementCapability) element;
			if (measurementCapability.getInConditions() != null) {
				hasChildren = !measurementCapability.getInConditions()
						.isEmpty();
			}
			if (measurementCapability.getHasMeasurementProperties() != null) {
				hasChildren = !measurementCapability
						.getHasMeasurementProperties().isEmpty();
			}
			return hasChildren;
		}
		return false;
	}
}
