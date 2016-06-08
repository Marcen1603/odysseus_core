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

import cc.kuka.odysseus.ontology.common.model.FeatureOfInterest;
import cc.kuka.odysseus.ontology.common.model.Property;
import cc.kuka.odysseus.ontology.rcp.SensorRegistryPlugIn;
import de.uniol.inf.is.odysseus.rcp.views.AbstractViewLabelProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class FeatureOfInterestContentLabelProvider extends AbstractViewLabelProvider {

    private final String featureOfInterestImage = "featureOfInterest";
    private final String propertyImage = "property";
    private final String uriImage = "uri";

    public FeatureOfInterestContentLabelProvider() {

    }

    @Override
    public Image getImage(final Object element) {
        if (element instanceof FeatureOfInterest) {
            return SensorRegistryPlugIn.getImageManager().get(this.featureOfInterestImage);
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
        if (element instanceof FeatureOfInterest) {
            final FeatureOfInterest featureOfInterest = (FeatureOfInterest) element;
            final StringBuilder sb = new StringBuilder();
            sb.append(featureOfInterest.name());
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
