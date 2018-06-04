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
package de.uniol.inf.is.odysseus.core;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.ListDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.CSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.DocumentProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.LineProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.MarkerByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.NoProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SVMProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleCSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.TextProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.DirectoryWatcherTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.FileHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NonBlockingTcpHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TcpSocketHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TimerTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;

public class Activator implements BundleActivator {

	private static BundleContext bundleContext;
	private static Logger LOGGER = LoggerFactory.getLogger("Core");
	
	public static BundleContext getBundleContext(){
		return bundleContext;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void start(BundleContext context) throws Exception {	
				
		bundleContext = context;

		// Get current size of heap
		LOGGER.info("Current size of heap: \t"+humanReadableByteCount(Runtime.getRuntime().totalMemory(), true));
		
		// Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
		LOGGER.info("Maximum size of heap: \t"+humanReadableByteCount(Runtime.getRuntime().maxMemory(), true));
		
		//Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
		LOGGER.info("Free memory of the heap: \t"+humanReadableByteCount(Runtime.getRuntime().freeMemory(), true));
		
		
		LOGGER.info("Running VM with: \t"+System.getProperty("os.arch"));
		
		ProtocolHandlerRegistry.register(new LineProtocolHandler());
		ProtocolHandlerRegistry.register(new DocumentProtocolHandler());
		ProtocolHandlerRegistry.register(new CSVProtocolHandler());
		ProtocolHandlerRegistry.register(new SimpleCSVProtocolHandler());
		ProtocolHandlerRegistry.register(new NoProtocolHandler());
		ProtocolHandlerRegistry.register(new TextProtocolHandler());
		ProtocolHandlerRegistry.register(new MarkerByteBufferHandler());
		ProtocolHandlerRegistry.register(new SVMProtocolHandler());
		
		TransportHandlerRegistry.register(new TcpSocketHandler());
		TransportHandlerRegistry.register(new FileHandler());
		TransportHandlerRegistry.register(new TimerTransportHandler());
        TransportHandlerRegistry.register(new DirectoryWatcherTransportHandler());
		TransportHandlerRegistry.register(new NonBlockingTcpHandler());		
		
		DataHandlerRegistry.registerDataHandler(new ListDataHandler());	
	}
	
	
	private static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		bundleContext = null;
	}

}
