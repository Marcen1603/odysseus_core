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
package cc.kuka.odysseus.ontology.rcp;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import cc.kuka.odysseus.ontology.common.SensorOntologyService;
import de.uniol.inf.is.odysseus.rcp.ImageManager;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 */
public class SensorRegistryPlugIn extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.probabilistic.sensor.rcp"; //$NON-NLS-1$
    public static final String SENSING_DEVICES_VIEW_ID = "de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.views.SensingDevicesView";
    public static final String FEATURES_OF_INTEREST_VIEW_ID = "de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.views.FeaturesOfInterestView";

    public static final String REFRESH_SENSOR_REGISTRY_VIEWS_COMMAND_ID = "de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.commands.RefreshSensorRegistryViewsCommand";
    public static final String CREATE_SENSING_DEVICE_COMMAND_ID = "de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.commands.CreateSensingDeviceCommand";
    public static final String REMOVE_SENSING_DEVICE_COMMMAND_ID = "de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.commands.RemoveSensingDeviceCommand";
    public static final String CREATE_FEATURE_OF_INTEREST_COMMAND_ID = "de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.commands.CreateFeatureOfInterestCommand";
    public static final String REMOVE_FEATURE_OF_INTEREST_COMMMAND_ID = "de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.commands.RemoveFeatureOfInterestCommand";

    // The shared instance
    private static SensorRegistryPlugIn plugin;

    private static SensorOntologyService sensorOntologyService;
    private static ImageManager imageManager;

    /**
     * The constructor
     */
    public SensorRegistryPlugIn() {
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        SensorRegistryPlugIn.plugin = this;
        SensorRegistryPlugIn.imageManager = new ImageManager(context.getBundle());
        SensorRegistryPlugIn.imageManager.register("sensingDevice", "icons/sensingDevice.png");
        SensorRegistryPlugIn.imageManager.register("featureOfInterest", "icons/featureOfInterest.png");
        SensorRegistryPlugIn.imageManager.register("measurementCapability", "icons/measurementCapability.png");
        SensorRegistryPlugIn.imageManager.register("measurementProperty", "icons/measurementProperty.png");
        SensorRegistryPlugIn.imageManager.register("property", "icons/property.png");
        SensorRegistryPlugIn.imageManager.register("condition", "icons/condition.png");
        SensorRegistryPlugIn.imageManager.register("ontology", "icons/ontology.png");
        SensorRegistryPlugIn.imageManager.register("uri", "icons/uri.png");

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void stop(final BundleContext context) throws Exception {
        SensorRegistryPlugIn.imageManager.disposeAll();
        SensorRegistryPlugIn.plugin = null;
        super.stop(context);
    }

    public static ImageManager getImageManager() {
        return SensorRegistryPlugIn.imageManager;
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static SensorRegistryPlugIn getDefault() {
        return SensorRegistryPlugIn.plugin;
    }

    //@SuppressWarnings("static-method")
    protected void bindSensorOntologyService(final SensorOntologyService service) {
        SensorRegistryPlugIn.sensorOntologyService = service;
    }

    //@SuppressWarnings("static-method")
    protected void unbindSensorOntologyService(final SensorOntologyService service) {
        SensorRegistryPlugIn.sensorOntologyService = null;
    }

    public static SensorOntologyService getSensorOntologyService() {
        return SensorRegistryPlugIn.sensorOntologyService;
    }
}
