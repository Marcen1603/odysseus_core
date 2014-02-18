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
        if (bundleContext != null) {
            Activator.context = bundleContext;

            bundleContext.registerService(CommandProvider.class.getName(), new ConsoleCommands(), null);
        }
        final StreamServer genericServer = new StreamServer(54325, new ClockProvider());
        genericServer.start();
        Activator.server.add(genericServer);
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

    /**
     * 
     */
    public static void pause() {
        synchronized (Activator.server) {
            for (final StreamServer s : Activator.server) {
                s.pauseClients();
            }
        }
    }

    public static void proceed() {
        synchronized (Activator.server) {
            for (final StreamServer s : Activator.server) {
                s.proceedClients();
            }
        }
    }

    public static void stop() {
        synchronized (Activator.server) {
            for (final StreamServer s : Activator.server) {
                s.stopClients();
            }
        }
    }

    /**
     * 
     */
    public static void printStatus() {
        synchronized (Activator.server) {
            for (final StreamServer s : Activator.server) {
                s.printStats();
            }
        }

    }
}
