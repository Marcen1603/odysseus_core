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
/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.generator.frequentitem;

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
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		
		bundleContext.registerService(CommandProvider.class.getName(), new ConsoleCommands(), null);
		
		StreamServer serverSimple = new StreamServer(54321, new FrequentItemProvider(FrequentItemProvider.DATA_FILE_SIMPLE));
		serverSimple.start();
		server.add(serverSimple);
		StreamServer serverT10I4D = new StreamServer(54322, new FrequentItemProvider(FrequentItemProvider.DATA_FILE_T10I4D100K));
		serverT10I4D.start();
		server.add(serverT10I4D);
		StreamServer serverFCMA= new StreamServer(54323, new FrequentItemProvider(FrequentItemProvider.DATA_FILE_FCMA));
		serverFCMA.start();
		server.add(serverFCMA);
		StreamServer serverABC = new StreamServer(54324, new FrequentItemProvider(FrequentItemProvider.DATA_FILE_ABC));
		serverABC.start();
		server.add(serverABC);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
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
			for(StreamServer s : server){
				s.pauseClients();
			}
		}		
	}
	
	public static void proceed() {
		synchronized (server) {
			for(StreamServer s : server){
				s.proceedClients();
			}
		}		
	}
	
	public static void stop() {
		synchronized (server) {
			for(StreamServer s : server){
				s.stopClients();
			}
		}		
	}

}
