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

import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.ListDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.CSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.LineProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.MarkerByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SASizeByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleCSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SizeByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.TextProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.FileHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NonBlockingTcpClientHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NonBlockingTcpHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NonBlockingTcpServerHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NonBlockingUdpClientHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.NonBlockingUdpServerHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TcpSocketHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.TransportHandlerRegistry;

public class Activator implements BundleActivator {

	private static BundleContext bundleContext;
	
	
	public static BundleContext getBundleContext(){
		return bundleContext;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void start(BundleContext context) throws Exception {	
		bundleContext = context;
		
		ProtocolHandlerRegistry.register(new LineProtocolHandler());
		ProtocolHandlerRegistry.register(new CSVProtocolHandler());
		ProtocolHandlerRegistry.register(new SimpleCSVProtocolHandler());
		ProtocolHandlerRegistry.register(new TextProtocolHandler());
		ProtocolHandlerRegistry.register(new SizeByteBufferHandler());
		ProtocolHandlerRegistry.register(new MarkerByteBufferHandler());
		ProtocolHandlerRegistry.register(new SASizeByteBufferHandler());
		
		
		TransportHandlerRegistry.register(new TcpSocketHandler());
		TransportHandlerRegistry.register(new FileHandler());
		TransportHandlerRegistry.register(new NonBlockingTcpHandler());
		
		TransportHandlerRegistry.register(new NonBlockingTcpServerHandler());
		TransportHandlerRegistry.register(new NonBlockingTcpClientHandler());
		TransportHandlerRegistry.register(new NonBlockingUdpServerHandler());
		TransportHandlerRegistry.register(new NonBlockingUdpClientHandler());
		
		DataHandlerRegistry.registerDataHandler(new ListDataHandler());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		bundleContext = null;
	}

}
