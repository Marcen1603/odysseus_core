/*
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.generator.probabilistic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
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
        Activator.context = bundleContext;

        bundleContext.registerService(CommandProvider.class.getName(), new ConsoleCommands(), null);

        StreamServer probabilistic1 = new StreamServer(54325, new ProbabilisticProvider());
        probabilistic1.start();
        StreamServer probabilistic2 = new StreamServer(54326, new ProbabilisticProvider());
        probabilistic2.start();
        server.add(probabilistic1);
        server.add(probabilistic2);
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
