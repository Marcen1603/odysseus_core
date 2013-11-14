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

import de.uniol.inf.is.odysseus.ontology.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;
import de.uniol.inf.is.odysseus.ontology.model.condition.Condition;
import de.uniol.inf.is.odysseus.ontology.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.rcp.server.views.AbstractViewLabelProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensingDeviceContentLabelProvider extends AbstractViewLabelProvider {

    private final String sensingDeviceImage;
    private final String measurementCapabilityImage = "measurementCapability";
    private final String conditionImage = "condition";

    public SensingDeviceContentLabelProvider(final String sensingDeviceImage) {
        this.sensingDeviceImage = sensingDeviceImage;
    }

    @Override
    public Image getImage(final Object element) {
        if (element instanceof SensingDevice) {
            return SensorRegistryPlugIn.getImageManager().get(this.sensingDeviceImage);
        }
        if (element instanceof MeasurementCapability) {
            return SensorRegistryPlugIn.getImageManager().get(this.measurementCapabilityImage);
        }
        if (element instanceof Condition) {
            return SensorRegistryPlugIn.getImageManager().get(this.conditionImage);
        }
        return super.getImage(element);
    }

    @SuppressWarnings("unused")
    @Override
    public String getText(final Object element) {
        if (element instanceof SensingDevice) {
            final SensingDevice sensingDevice = (SensingDevice) element;
            final StringBuilder sb = new StringBuilder();
            // sb.append(sensingDevice.getName()).append(" [").append(sensingDevice.getUri().toString()).append("]");
            return sb.toString();
        }
        else if (element instanceof MeasurementCapability) {
            final MeasurementCapability measurementCapability = (MeasurementCapability) element;
            // final SDFAttribute attribute =
            // measurementCapability.getAttribute();
            final StringBuilder sb = new StringBuilder();
            // sb.append(attribute.getAttributeName()).append(" [").append(attribute.getURI().toString()).append("]");
            return sb.toString();
        }
        else if (element instanceof Condition) {
            final Condition condition = (Condition) element;
            final StringBuilder sb = new StringBuilder();
            // sb.append(condition.getAttribute().getAttributeName()).append(" [").append(condition.getAttribute().getURI().toString()).append("]").append(" [").append(condition.getInterval())
            // .append("]");
            return sb.toString();
        }
        return super.getText(element);
    }
}
