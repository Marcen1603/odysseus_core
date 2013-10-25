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
package de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.views;

import org.eclipse.swt.graphics.Image;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.Condition;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.MeasurementCapability;
import de.uniol.inf.is.odysseus.probabilistic.sensor.model.SensingDevice;
import de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.rcp.server.views.AbstractViewLabelProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SensingDeviceContentLabelProvider extends AbstractViewLabelProvider {

    private String operatorImage;

    public SensingDeviceContentLabelProvider(String operatorImage) {
        this.operatorImage = operatorImage;
    }

    @Override
    public Image getImage(Object element) {
        if (element instanceof SensingDevice) {
            return SensorRegistryPlugIn.getImageManager().get(operatorImage);
        }
        return super.getImage(element);
    }

    @Override
    public String getText(Object element) {
        if (element instanceof SensingDevice) {
            SensingDevice sensingDevice = (SensingDevice) element;
            StringBuilder sb = new StringBuilder();
            sb.append(sensingDevice.getName()).append(" [").append(sensingDevice.getUri().toString()).append("]");
            return sb.toString();
        }
        else if (element instanceof MeasurementCapability) {
            MeasurementCapability measurementCapability = (MeasurementCapability) element;
            SDFAttribute attribute = measurementCapability.getAttribute();
            StringBuilder sb = new StringBuilder();
            sb.append(attribute.getAttributeName()).append(" [").append(attribute.getURI().toString()).append("]");
            return sb.toString();
        }
        else if (element instanceof Condition) {
            Condition condition = (Condition) element;
            StringBuilder sb = new StringBuilder();
            sb.append(condition.getAttribute().getAttributeName()).append(" [").append(condition.getAttribute().getURI().toString()).append("]").append(" [").append(condition.getInterval())
                    .append("]");
            return sb.toString();
        }
        return super.getText(element);
    }
}
