package de.uniol.inf.is.odysseus.generator.heatmap;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;


public class Activator implements BundleActivator {
    private static BundleContext context;

    static BundleContext getContext() {
        return Activator.context;
    }

    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        System.out.println("Starte Heatmap-Generator");
    	Activator.context = bundleContext;
        final StreamServer server = new StreamServer(54321, new HeatmapDataProvider());
        server.start();
    }
    
    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

}
