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
package de.uniol.inf.is.odysseus.generator.securitypunctuation;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.generator.StreamServer;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		Integer delay = 50;
		boolean benchmark = true;
		Float spProbability = (float) 0.2;
		
	    StreamServer server1 = new StreamServer(54321, new SecurityPunctuationProvider(true, "attribute", delay, "server1", benchmark, spProbability));
	    StreamServer server2 = new StreamServer(54322, new SecurityPunctuationProvider(true, "attribute", delay, "server2", benchmark, spProbability));
	    StreamServer server3 = new StreamServer(54323, new CSVSPProvider("input_join_1.csv", delay, "server3", benchmark));
	    StreamServer server4 = new StreamServer(54324, new CSVSPProvider("input_join_2.csv", delay, "server4", benchmark));
	    server1.start();
	    server2.start();
	    server3.start();
	    server4.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
