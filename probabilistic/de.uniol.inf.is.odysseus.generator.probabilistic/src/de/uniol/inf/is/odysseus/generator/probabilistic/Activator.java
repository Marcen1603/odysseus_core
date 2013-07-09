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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Activator implements BundleActivator {
	private static final int SERVERS = 3;
	private static final int PORT = 65450;
	private static BundleContext context;
	private final List<StreamServer> servers = new ArrayList<StreamServer>();

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
		for (int i = 0; i < Activator.SERVERS; i++) {
			final StreamServer server = new StreamServer(Activator.PORT + i,
					new ProbabilisticDataProvider());
			server.start();
			this.servers.add(server);
		}

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
		for (int i = 0; i < Activator.SERVERS; i++) {
			this.servers.get(i).printStats();
			this.servers.get(i).stopClients();
		}
	}

}
