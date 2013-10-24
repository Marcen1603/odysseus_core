package de.uniol.inf.is.odysseus.probabilistic.sensor.rcp;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.probabilistic.sensor.SensorOntologyService;

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
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
     * )
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static SensorRegistryPlugIn getDefault() {
        return plugin;
    }

    protected void bindSensorOntologyService(SensorOntologyService service) {
        sensorOntologyService = service;
    }

    protected void unbindSensorOntologyService(SensorOntologyService service) {
        sensorOntologyService = null;
    }

    public static SensorOntologyService getSensorOntologyService() {
        return sensorOntologyService;
    }
}
