/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.debschallenge.reader;

import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public static void run(String[] args, IApplicationContext context) {
		try {
			StreamServer server;
			int port = 54321;
			if (args.length >= 1) {
				port = Integer.parseInt(args[0].trim());
			}
			if (args.length >= 2) {
				String file = args[1];
				server = new StreamServer(port, new DataProvider(file, context));
			} else {
				server = new StreamServer(port, new DataProvider());
			}
			server.start();
			server.join();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(BundleContext context) throws Exception {
		Activator.context = context;
		// run();
		//(new RepairTool()).go();

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.context = null;
	}

}
