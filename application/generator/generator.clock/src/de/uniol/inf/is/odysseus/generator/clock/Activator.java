package de.uniol.inf.is.odysseus.generator.clock;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;

public class Activator implements BundleActivator {

    private static BundleContext context;
    private static List<StreamServer> server = new ArrayList<StreamServer>();

    static BundleContext getContext() {
        return context;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        if (bundleContext != null) {
            Activator.context = bundleContext;

            bundleContext.registerService(CommandProvider.class.getName(), new ConsoleCommands(), null);
        }
        StreamServer genericServer = new StreamServer(54325, new ClockProvider());
        genericServer.start();
        server.add(genericServer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

    /**
     * 
     */
    public static void pause() {
        synchronized (server) {
            for (StreamServer s : server) {
                s.pauseClients();
            }
        }
    }

    public static void proceed() {
        synchronized (server) {
            for (StreamServer s : server) {
                s.proceedClients();
            }
        }
    }

    public static void stop() {
        synchronized (server) {
            for (StreamServer s : server) {
                s.stopClients();
            }
        }
    }

    /**
     * 
     */
    public static void printStatus() {
        synchronized (server) {
            for (StreamServer s : server) {
                s.printStats();
            }
        }

    }
}
