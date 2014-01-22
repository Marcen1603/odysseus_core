package de.uniol.inf.is.odysseus.ontology;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.ontology.common.SensorOntologyService;

public class Activator implements BundleActivator {

    private static BundleContext context;
    private static SensorOntologyService sensorOntologyService;

    static BundleContext getContext() {
        return Activator.context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

    protected void bindSensorOntologyService(final SensorOntologyService service) {
        Activator.sensorOntologyService = service;
    }

    protected void unbindSensorOntologyService(final SensorOntologyService service) {
        Activator.sensorOntologyService = null;
    }

    public static SensorOntologyService getSensorOntologyService() {
        return Activator.sensorOntologyService;
    }
}
