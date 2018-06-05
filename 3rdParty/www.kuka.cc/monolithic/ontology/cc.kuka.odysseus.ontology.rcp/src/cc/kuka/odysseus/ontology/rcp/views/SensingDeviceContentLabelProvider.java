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
package cc.kuka.odysseus.ontology.rcp.views;

import java.net.URI;

import org.eclipse.swt.graphics.Image;

import cc.kuka.odysseus.ontology.common.model.MeasurementCapability;
import cc.kuka.odysseus.ontology.common.model.Property;
import cc.kuka.odysseus.ontology.common.model.SensingDevice;
import cc.kuka.odysseus.ontology.common.model.condition.Condition;
import cc.kuka.odysseus.ontology.common.model.property.MeasurementProperty;
import cc.kuka.odysseus.ontology.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.rcp.views.AbstractViewLabelProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SensingDeviceContentLabelProvider extends AbstractViewLabelProvider {

    private final String sensingDeviceImage = "sensingDevice";
    private final String measurementCapabilityImage = "measurementCapability";
    private final String measurementPropertyImage = "measurementProperty";
    private final String conditionImage = "condition";
    private final String propertyImage = "property";
    private final String uriImage = "uri";

    public SensingDeviceContentLabelProvider() {

    }

    @Override
    public Image getImage(final Object element) {
        if (element instanceof SensingDevice) {
            return SensorRegistryPlugIn.getImageManager().get(this.sensingDeviceImage);
        }
        else if (element instanceof MeasurementCapability) {
            return SensorRegistryPlugIn.getImageManager().get(this.measurementCapabilityImage);
        }
        else if (element instanceof Condition) {
            return SensorRegistryPlugIn.getImageManager().get(this.conditionImage);
        }
        else if (element instanceof MeasurementProperty) {
            return SensorRegistryPlugIn.getImageManager().get(this.measurementPropertyImage);
        }
        else if (element instanceof Property) {
            return SensorRegistryPlugIn.getImageManager().get(this.propertyImage);
        }
        else if (element instanceof URI) {
            return SensorRegistryPlugIn.getImageManager().get(this.uriImage);
        }
        return super.getImage(element);
    }

    @Override
    public String getText(final Object element) {
        if (element instanceof SensingDevice) {
            final SensingDevice sensingDevice = (SensingDevice) element;
            final StringBuilder sb = new StringBuilder();
            sb.append(sensingDevice.name());
            return sb.toString();
        }
        else if (element instanceof MeasurementCapability) {
            final MeasurementCapability measurementCapability = (MeasurementCapability) element;
            final StringBuilder sb = new StringBuilder();
            sb.append(measurementCapability.name());
            return sb.toString();
        }
        else if (element instanceof Condition) {
            final Condition condition = (Condition) element;
            final StringBuilder sb = new StringBuilder();
            sb.append(String.format(condition.toString(), condition.onProperty().name()));
            return sb.toString();
        }
        else if (element instanceof MeasurementProperty) {
            final MeasurementProperty measurementProperty = (MeasurementProperty) element;
            final StringBuilder sb = new StringBuilder();
            sb.append(URI.create(measurementProperty.resource()).getFragment()).append(" = ").append(measurementProperty.expression());
            return sb.toString();
        }
        else if (element instanceof Property) {
            final Property property = (Property) element;
            final StringBuilder sb = new StringBuilder();
            sb.append(property.name());
            return sb.toString();
        }
        else if (element instanceof URI) {
            final URI uri = (URI) element;
            final StringBuilder sb = new StringBuilder();
            sb.append("[ns:").append(uri.getFragment()).append("]");
            return sb.toString();
        }
        else if (element instanceof String) {
            return (String) element;
        }
        return super.getText(element);
    }
}
