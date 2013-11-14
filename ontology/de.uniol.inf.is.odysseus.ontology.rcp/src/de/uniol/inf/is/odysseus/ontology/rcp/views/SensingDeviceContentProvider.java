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

import java.util.Collection;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

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
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
    }

    @Override
    public Object[] getElements(final Object inputElement) {
        return this.getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(final Object parentElement) {
        if (parentElement instanceof Collection) {
            return ((Collection<?>) parentElement).toArray();
        }
        if (parentElement instanceof SensingDevice) {
            final SensingDevice sensingDevice = (SensingDevice) parentElement;
            return sensingDevice.getHasMeasurementCapabilities().toArray();
        }
        if (parentElement instanceof MeasurementCapability) {
            final MeasurementCapability measurementCapability = (MeasurementCapability) parentElement;
            return measurementCapability.getInConditions().toArray();
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
            return !((SensingDevice) element).getHasMeasurementCapabilities().isEmpty();
        }
        if (element instanceof MeasurementCapability) {
            return !((MeasurementCapability) element).getInConditions().isEmpty();
        }
        return false;
    }
}
