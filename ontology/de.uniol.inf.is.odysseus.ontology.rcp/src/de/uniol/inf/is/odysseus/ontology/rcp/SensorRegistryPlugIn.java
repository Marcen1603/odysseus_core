package de.uniol.inf.is.odysseus.ontology.rcp;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.ontology.SensorOntologyService;
import de.uniol.inf.is.odysseus.rcp.ImageManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class SensorRegistryPlugIn extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "de.uniol.inf.is.odysseus.probabilistic.sensor.rcp"; //$NON-NLS-1$
    public static final String SENSOR_REGISTRY_VIEW_ID = "de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.views.SensorRegistryView";

    public static final String REFRESH_SENSOR_REGISTRY_VIEW_COMMAND_ID = "de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.commands.RefreshSensorRegistryViewCommand";
    public static final String CREATE_SENSING_DEVICE_COMMAND_ID = "de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.commands.CreateSensingDeviceCommand";
    public static final String REMOVE_SENSING_DEVICE_COMMMAND_ID = "de.uniol.inf.is.odysseus.probabilistic.sensor.rcp.commands.RemoveSensingDeviceCommand";

    // The shared instance
    private static SensorRegistryPlugIn plugin;

    private static SensorOntologyService sensorOntologyService;
    private static ImageManager imageManager;

    /**
     * The constructor
     */
    public SensorRegistryPlugIn() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        SensorRegistryPlugIn.plugin = this;
        SensorRegistryPlugIn.imageManager = new ImageManager(context.getBundle());
        SensorRegistryPlugIn.imageManager.register("sensingDevice", "icons/sensingDevice.png");
        SensorRegistryPlugIn.imageManager.register("measurementCapability", "icons/measurementCapability.png");
        SensorRegistryPlugIn.imageManager.register("measurementProperty", "icons/measurementProperty.png");
        SensorRegistryPlugIn.imageManager.register("property", "icons/property.png");
        SensorRegistryPlugIn.imageManager.register("condition", "icons/condition.png");
        SensorRegistryPlugIn.imageManager.register("ontology", "icons/ontology.png");

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
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

    protected void bindSensorOntologyService(final SensorOntologyService service) {
        SensorRegistryPlugIn.sensorOntologyService = service;
    }

    protected void unbindSensorOntologyService(final SensorOntologyService service) {
        SensorRegistryPlugIn.sensorOntologyService = null;
    }

    public static SensorOntologyService getSensorOntologyService() {
        return SensorRegistryPlugIn.sensorOntologyService;
    }
}
