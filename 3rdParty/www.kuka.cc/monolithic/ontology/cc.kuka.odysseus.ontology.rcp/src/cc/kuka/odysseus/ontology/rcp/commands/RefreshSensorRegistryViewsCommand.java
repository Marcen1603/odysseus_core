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
package cc.kuka.odysseus.ontology.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import cc.kuka.odysseus.ontology.rcp.SensorRegistryPlugIn;
import cc.kuka.odysseus.ontology.rcp.views.FeaturesOfInterestView;
import cc.kuka.odysseus.ontology.rcp.views.SensingDevicesView;
import de.uniol.inf.is.odysseus.rcp.util.ViewHelper;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class RefreshSensorRegistryViewsCommand extends AbstractHandler {

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final SensingDevicesView sensingDevicesPart = ViewHelper.getView(SensorRegistryPlugIn.SENSING_DEVICES_VIEW_ID, event);
        final FeaturesOfInterestView featuresOfInterestPart = ViewHelper.getView(SensorRegistryPlugIn.FEATURES_OF_INTEREST_VIEW_ID, event);
        SensorRegistryPlugIn.getSensorOntologyService().clearCache();

        if (sensingDevicesPart != null) {
            sensingDevicesPart.refresh();
        }
        else {
            return Boolean.FALSE;
        }
        if (featuresOfInterestPart != null) {
            featuresOfInterestPart.refresh();
        }
        else {
            return Boolean.FALSE;

        }
        return Boolean.TRUE;

    }

}
