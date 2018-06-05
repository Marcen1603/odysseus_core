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
package cc.kuka.odysseus.ontology.rcp.actions;

import java.net.URI;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import cc.kuka.odysseus.ontology.common.SensorOntologyService;
import cc.kuka.odysseus.ontology.common.model.MeasurementCapability;
import cc.kuka.odysseus.ontology.common.model.Property;
import cc.kuka.odysseus.ontology.common.model.SensingDevice;
import cc.kuka.odysseus.ontology.rcp.SensorRegistryPlugIn;
import cc.kuka.odysseus.ontology.rcp.dialogs.AttributeDialog;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class CreateMeasurementCapabilityAction extends Action {
    private Shell shell;
    private SensingDevice sensingDevice;

    /**
     * Class constructor.
     *
     */
    public CreateMeasurementCapabilityAction(Shell shell, SensingDevice sensingDevice) {
        super();
        this.shell = shell;
        this.sensingDevice = sensingDevice;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        final AttributeDialog attributeDialog = new AttributeDialog(this.shell);
        attributeDialog.open();

        if (attributeDialog.getReturnCode() == Window.OK) {
            final SensorOntologyService ontology = SensorRegistryPlugIn.getSensorOntologyService();
            SDFAttribute attribute = attributeDialog.getAttribute();
            Property property = attributeDialog.getProperty();
            final MeasurementCapability measurementCapability = new MeasurementCapability(URI.create(this.sensingDevice.uri().toString() + "/" + attribute.getAttributeName()),
                    attribute.getAttributeName(), property);
            this.sensingDevice.add(measurementCapability);
            ontology.create(this.sensingDevice, measurementCapability);
        }
    }
}
